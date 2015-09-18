package com.weiss.remote_connect.packets.mouse;

import java.awt.Robot;

public class MouseMovedPacket extends MouseEventPacket {

	public MouseMovedPacket(final int x, final int y) {
		super(MouseEventType.MOUSE_MOVED, x, y);
	}

	@Override
	public void handleEvent(Robot robot) {
		robot.mouseMove(this.x, this.y);
	}
	
}
