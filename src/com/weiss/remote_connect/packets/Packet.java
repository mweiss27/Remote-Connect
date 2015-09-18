package com.weiss.remote_connect.packets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;

public abstract class Packet implements Serializable {

	private static final long serialVersionUID = 1L;

	public DatagramPacket get() throws IOException {

		final ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		final ObjectOutputStream oOut = new ObjectOutputStream(bOut);
		oOut.writeObject(this);

		final byte[] bytes = bOut.toByteArray();

		return new DatagramPacket(bytes, bytes.length);
	}

}
