package com.weiss.remote_connect.client;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.weiss.remote_connect.io.Log;
import com.weiss.remote_connect.packets.FramePacket;
import com.weiss.remote_connect.packets.Packet;
import com.weiss.remote_connect.packets.mouse.MouseClickedPacket;
import com.weiss.remote_connect.packets.mouse.MouseDraggedPacket;
import com.weiss.remote_connect.packets.mouse.MouseEventPacket;
import com.weiss.remote_connect.packets.mouse.MouseMovedPacket;

public class RemoteClient extends JFrame {

	private final Socket client;

	private BufferedImage currentFrame;
	private Point mouseLocation;

	private ExecutorService exec = Executors.newFixedThreadPool(1);

	public RemoteClient(final InetAddress address, final int port) throws IOException {
		Log.info("[Client] Attempting to connect to " + address.getHostAddress() + ":" + port);
		this.client = new Socket();
		this.client.connect(new InetSocketAddress(address, port), 5000);

		final BufferedImage cursorImg = ImageIO.read(this.getClass().getResourceAsStream("/com/weiss/remote_connect/resources/cursor-24.png"));
		
		final JPanel view = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (RemoteClient.this.currentFrame != null) {
					g.drawImage(RemoteClient.this.currentFrame, 0, 0, null);
				}
				if (RemoteClient.this.mouseLocation != null) {
					//g.drawImage(cursorImg, RemoteClient.this.mouseLocation.x, RemoteClient.this.mouseLocation.y, null);
				}

			}
		};
		view.setPreferredSize(new Dimension(1920, 1080));
		
		final MouseAdapter ma = new MouseAdapter() {
			
			private final ExecutorService et = Executors.newSingleThreadExecutor();
			
			@Override
			public void mouseClicked(MouseEvent e) {
				et.execute(new Runnable() {
					@Override
					public void run() {
						sendEvent(new MouseClickedPacket(e.getButton(), e.getX(), e.getY()));
					}
				});
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				et.execute(new Runnable() {
					@Override
					public void run() {
						sendEvent(new MouseDraggedPacket(e.getButton(), e.getX(), e.getY()));
					}
				});
			}
			
			@Override
			public void mouseMoved(MouseEvent e) {
				et.execute(new Runnable() {
					@Override
					public void run() {
						sendEvent(new MouseMovedPacket(e.getX(), e.getY()));
					}
				});
			}
			
			private void sendEvent(final MouseEventPacket packet) {
				try {
					Log.info("[Client] Sending MouseEventPacket: " + packet);
					final DataOutputStream writeOut = new DataOutputStream(RemoteClient.this.client.getOutputStream());
					final ByteArrayOutputStream bOut = new ByteArrayOutputStream();
					final ObjectOutputStream oOut = new ObjectOutputStream(bOut);
					oOut.writeObject(packet);
					
					final byte[] packetBytes = bOut.toByteArray();
					writeOut.write(packetBytes);
					
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		};

		view.addMouseListener(ma);
		view.addMouseMotionListener(ma);
		
		this.setContentPane(view);
		this.setLocationRelativeTo(null);
		this.pack();
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public void start() {
		Executors.newSingleThreadExecutor().execute(new Runnable() {
			@Override
			public void run() {
				try {
					final Future<?> inputThread = RemoteClient.this.exec.submit(new Runnable() {
						@Override
						public void run() {

							try {
								
								final byte[] buffer = new byte[100 * 1024 * 1024];
								final DataInputStream readIn = new DataInputStream(RemoteClient.this.client.getInputStream());

								do {
									//System.out.println("Waiting for bytes");
									final int len = readIn.readInt();
									//System.out.println("Receiving " + len + " bytes");
									readIn.readFully(buffer, 0, len);
									final Object o = RemoteClient.this.deserialize(buffer);

									if (o == null) {
										Log.err("We received a null object.");
										continue;
									}

									if (!(o instanceof Packet)) {
										Log.err("We received an object, but it isn't a Packet: " + o.getClass());
										continue;
									}

									if (o instanceof FramePacket) {
										final FramePacket framePacket = (FramePacket) o;
										final ByteArrayInputStream bIn = new ByteArrayInputStream(framePacket.frameBytes);
										RemoteClient.this.currentFrame = ImageIO.read(bIn);
										RemoteClient.this.mouseLocation = framePacket.mouseLocation;
										SwingUtilities.invokeLater(new Runnable() {
											@Override
											public void run() {
												RemoteClient.this.getContentPane().repaint();
											}
										});
									}
									else {
										Log.err("We received a Packet, but we don't recognize it: " + o.getClass());
									}

									System.gc();

								} while (!RemoteClient.this.client.isClosed());
							} catch (final Exception any) {
								any.printStackTrace();
							}
						}
					});

					inputThread.get();

				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private synchronized Object deserialize(final byte[] bytes) throws IOException, ClassNotFoundException {
		final ByteArrayInputStream bIn = new ByteArrayInputStream(bytes);
		final ObjectInputStream oIn = new ObjectInputStream(bIn);

		return oIn.readObject();
	}

}
