package com.weiss.remote_connect.packets.mouse;

import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

public class MouseDraggedPacket extends MouseEventPacket {

	public final int mouseButton;
	
	public MouseDraggedPacket(final int mouseButton, final int x, final int y) {
		super(MouseEventType.MOUSE_DRAGGED, x, y);
		this.mouseButton = mouseButton;
	}

	@Override
	public void handleEvent(Robot robot) {
		robot.mousePress(InputEvent.getMaskForButton(MouseEvent.BUTTON1));
		robot.mouseMove(this.x, this.y);
		robot.mouseRelease(InputEvent.getMaskForButton(MouseEvent.BUTTON1));
	}
	
}
