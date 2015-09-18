package com.weiss.remote_connect.packets;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;

import javax.imageio.ImageIO;


public class FramePacket extends Packet {

	private static final long serialVersionUID = 1L;
	
	private static Image cursorImage;
	
	static {
		try {
			cursorImage = ImageIO.read(FramePacket.class.getResourceAsStream("/com/weiss/remote_connect/resources/mac_cursor.png")).getScaledInstance(24, 24, Image.SCALE_SMOOTH);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public transient BufferedImage frame;
	public byte[] frameBytes;
	public final Point mouseLocation;

	public FramePacket(final BufferedImage frame, final Point mouseLocation) {
		this.frame = frame;
		final Graphics g = this.frame.getGraphics().create();
		g.drawImage(cursorImage, mouseLocation.x, mouseLocation.y, null);
		g.dispose();
		this.mouseLocation = mouseLocation;
	}
	
	@Override
	public DatagramPacket get() throws IOException {
		final ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		ImageIO.write(this.frame, "JPG", bOut);
		this.frameBytes = bOut.toByteArray();
		
		final ByteArrayOutputStream objectBytesOut = new ByteArrayOutputStream();
		final ObjectOutputStream oOut = new ObjectOutputStream(objectBytesOut);
		oOut.writeObject(this);
		final byte[] objBytes = objectBytesOut.toByteArray();
		return new DatagramPacket(objBytes, objBytes.length);
	}

}
