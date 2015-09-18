package com.weiss.remote_connect.ui.wrappers;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.JPanel;

/**
 * A JPanel with a nicer implementation of FlowLayout for my purposes
 */
public class FlowPanel extends JPanel {

	private static int staId = 0;
	private int insId;
	private int horizontal = 0;
	private GridBagConstraints gbc = new GridBagConstraints();
	protected JPanel fill = new JPanel(); 

	/**
	 * 
	 * @param alignment FlowLayout.LEFT,RIGHT,CENTER
	 * @param horizontal Horizontal spacing between components
	 * @param vertical Vertical spacing between components
	 * @param components Any components you wish to add after instantiation
	 */
	public FlowPanel(int alignment, int horizontal, Component...components) {
		super(alignment == FlowLayout.CENTER ? new FlowLayout() : new GridBagLayout());
		gbc.anchor = (alignment == FlowLayout.LEFT ? GridBagConstraints.WEST : alignment == FlowLayout.RIGHT ? GridBagConstraints.EAST : GridBagConstraints.CENTER);
		gbc.gridx = gbc.gridy = 0;

		this.horizontal = horizontal;
		this.insId = staId++;

		gbc.weightx = 1D;
		this.add(this.fill, gbc);

		for (Component c : components) 
			this.add(c);
	}

	@Override
	public Component add(Component comp) {
		this.add(comp, false);

		return comp;
	}

	public void add(final Component comp, final boolean fillHor) {
		switch(this.gbc.anchor) {
			case GridBagConstraints.WEST:
				if (Arrays.asList(this.getComponents()).contains(this.fill)) {
					this.remove(this.fill);
				}

				gbc.weightx = fillHor ? 1D : 0D;
				super.add(comp, gbc);

				if (!fillHor) {
					if (this.horizontal > 0 && this.getComponentCount() > 0) {
						gbc.gridx++;
						super.add(Box.createHorizontalStrut(this.horizontal), gbc);
					}

					gbc.gridx++;
					gbc.weightx = 1D;
					super.add(this.fill, gbc);
					gbc.weightx = 0D;
				}

				break;
			case GridBagConstraints.CENTER:
				super.add(comp);
				if (this.horizontal > 0 && this.getComponentCount() > 0) {
					gbc.gridx++;
					super.add(Box.createHorizontalStrut(this.horizontal), gbc);
				}
				break;
			case GridBagConstraints.EAST:

				if (this.horizontal > 0 && this.getComponentCount() > 0) {
					gbc.gridx++;
					super.add(Box.createHorizontalStrut(this.horizontal), gbc);
				}

				gbc.gridx++;
				gbc.weightx = 0D;
				super.add(comp, gbc);

				break;
		}
	}

	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		if (this.fill != null) {
			this.fill.setBackground(bg);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FlowPanel) {
			FlowPanel other = (FlowPanel) obj;
			return this.insId == other.insId;
		}
		return false;
	};

	@Override
	public int hashCode() {
		return this.insId;
	}

}

