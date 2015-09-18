package com.weiss.remote_connect.packets.mouse;

import java.awt.Robot;

import com.weiss.remote_connect.packets.Packet;

public abstract class MouseEventPacket extends Packet {

	private static final long serialVersionUID = 1L;

	public final MouseEventType mouseEventType;
	
	public final int x, y;
	
	public MouseEventPacket(final MouseEventType type, final int x, final int y) {
		this.mouseEventType = type;
		this.x = x;
		this.y = y;
	}
	
	public abstract void handleEvent(final Robot robot);
	
	@Override
	public String toString() {
		return this.mouseEventType + ": x: " + this.x + ", y: " + this.y;
	}
}
