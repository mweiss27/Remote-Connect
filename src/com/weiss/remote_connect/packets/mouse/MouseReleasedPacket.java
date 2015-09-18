package com.weiss.remote_connect.packets.mouse;

import java.awt.Robot;
import java.awt.event.InputEvent;

public class MouseReleasedPacket extends MouseEventPacket {

	private final int button;
	
	public MouseReleasedPacket(final int button, final int x, final int y) {
		super(MouseEventType.MOUSE_RELEASED, x, y);
		this.button = button;
	}

	@Override
	public void handleEvent(Robot robot) {
		robot.mouseMove(this.x, this.y);
		robot.mouseRelease(InputEvent.getMaskForButton(this.button));
	}
}