package com.weiss.remote_connect.packets.mouse;

import java.awt.Robot;
import java.awt.event.InputEvent;

public class MouseClickedPacket extends MouseEventPacket {

	private final int button;
	
	public MouseClickedPacket(final int button, final int x, final int y) {
		super(MouseEventType.MOUSE_CLICKED, x, y);
		this.button = button;
	}

	@Override
	public void handleEvent(Robot robot) {
		robot.mouseMove(this.x, this.y);
		robot.mousePress(InputEvent.getMaskForButton(this.button));
		robot.mouseRelease(InputEvent.getMaskForButton(this.button));
	}
	
}
