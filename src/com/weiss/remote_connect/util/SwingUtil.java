package com.weiss.remote_connect.util;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;

public class SwingUtil {

	public static CardLayout getCardLayout(final JComponent c) {
		if (c.getLayout() instanceof CardLayout) {
			return (CardLayout) c.getLayout();
		}
		throw new IllegalStateException("c is not CardLayout");
	}
	
	public static JComponent[] getAllSubComponents(Container root) {
		List<JComponent> comps = new LinkedList<JComponent>();
		for (Component c : root.getComponents()) {
			try {
				comps.add((JComponent) c);
				comps.addAll(Arrays.asList(getAllSubComponents((JComponent) c)));
			} catch (final ClassCastException e) {
				continue;
			}
		}
		return comps.toArray(new JComponent[comps.size()]);
	}
	
	public static Map<Component, List<MouseListener>> storeMouseListeners(final JComponent root) {
		final Map<Component, List<MouseListener>> map = new HashMap<Component, List<MouseListener>>();
		for (final Component c : getAllSubComponents(root)) {
			map.put(c, new ArrayList<MouseListener>());
			for (final MouseListener m : c.getMouseListeners()) {
				c.removeMouseListener(m);
				map.get(c).add(m);
			}
		}
		return map;
	}
	
	public static void restoreMouseListeners(final Map<Component, List<MouseListener>> map, final JComponent root) {
		for (final Component c : getAllSubComponents(root)) {
			List<MouseListener> listeners = map.get(c);
			if (listeners != null)
				for (final MouseListener ml : map.get(c)) {
					c.addMouseListener(ml);
				}
		}
	}
	
}
