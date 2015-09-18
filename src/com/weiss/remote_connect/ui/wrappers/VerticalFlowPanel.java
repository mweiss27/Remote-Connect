package com.weiss.remote_connect.ui.wrappers;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.security.InvalidParameterException;

import javax.swing.Box;
import javax.swing.JPanel;

/**
 * A JPanel with a nicer implementation of BoxLayout(Y_AXIS) for my purposes
 */
public class VerticalFlowPanel extends JPanel {

	private final GridBagConstraints gbc = new GridBagConstraints();
	private final int flowAlignment;
	private final int verticalSpacing;

	public VerticalFlowPanel(final int alignment, final int verticalSpacing, final Component...components) {
		super(new GridBagLayout());
		this.flowAlignment = alignment;
		this.verticalSpacing = verticalSpacing;

		this.gbc.fill = GridBagConstraints.NONE;
		this.gbc.weightx = 1D;
		this.gbc.weighty = 0D;

		for (final Component c : components) {
			this.add(c);
		}
	}

	@Override
	public Component add(Component comp) {
		return this.add(comp, this.flowAlignment);
	}

	@Override
	public Component add(Component comp, int alignment) {
		this.removeFill();
		if (this.getComponentCount() > 0 && this.verticalSpacing > 0) {
			this.gbc.gridy++;
			this.add(Box.createVerticalStrut(this.verticalSpacing), this.gbc);
		}
		this.gbc.anchor = toGrid(alignment);
		this.gbc.gridy++;
		this.add(comp, this.gbc);
		this.addFill();
		return comp;
	}
	
	public void add(final Component comp, final boolean fillHor) {
		this.gbc.fill = fillHor ? GridBagConstraints.HORIZONTAL : GridBagConstraints.NONE;
		this.add(comp, this.flowAlignment);
		this.gbc.fill = GridBagConstraints.NONE;
	}

	private boolean removeFill() {
		if (this.getComponents().length > 0) {
			Component last = this.getComponent(this.getComponents().length - 1);
			if (last instanceof JPanel && last.getName().equals("fill")) {
				this.remove(last);
				return true;
			}
		}
		return false;
	}

	private void addFill() {
		this.gbc.gridy++;
		this.gbc.weighty = 1D;
		this.add(new JPanel() {{ setName("fill"); setPreferredSize(new Dimension(0, 0)); }}, this.gbc);
		this.gbc.weighty = 0D;
	}

	private static int toGrid(final int flowAlignment) {
		switch (flowAlignment) {
			case FlowLayout.LEFT:
				return GridBagConstraints.NORTHWEST;
			case FlowLayout.CENTER:
				return GridBagConstraints.NORTH;
			case FlowLayout.RIGHT:
				return GridBagConstraints.NORTHEAST;
			default:
				throw new InvalidParameterException("alignment must be LEFT, CENTER, or RIGHT. We received " + flowAlignment);
		}
	}
}

