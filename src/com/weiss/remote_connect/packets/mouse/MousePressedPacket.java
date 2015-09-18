package com.weiss.remote_connect.packets.mouse;

import java.awt.Robot;
import java.awt.event.InputEvent;

public class MousePressedPacket extends MouseEventPacket {

	private final int button;
	
	public MousePressedPacket(final int button, final int x, final int y) {
		super(MouseEventType.MOUSE_PRESSED, x, y);
		this.button = button;
	}

	@Override
	public void handleEvent(Robot robot) {
		robot.mouseMove(this.x, this.y);
		robot.mousePress(InputEvent.getMaskForButton(this.button));
	}
}
