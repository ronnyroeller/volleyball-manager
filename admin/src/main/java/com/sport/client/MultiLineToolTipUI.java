package com.sport.client;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JToolTip;
import javax.swing.SwingUtilities;
import javax.swing.plaf.metal.MetalToolTipUI;

import org.apache.log4j.Logger;

public class MultiLineToolTipUI extends MetalToolTipUI {

	private static final Logger LOG = Logger.getLogger(MultiLineToolTipUI.class
			.getName());

	private String[] strs;

	public void paint(Graphics g, JComponent c) {
		FontMetrics metrics = g.getFontMetrics();
		Dimension size = c.getSize();
		g.setColor(c.getBackground());
		g.fillRect(0, 0, size.width, size.height);
		g.setColor(c.getForeground());
		if (strs != null) {
			for (int i = 0; i < strs.length; i++) {
				g.drawString(strs[i], 3, (metrics.getHeight()) * (i + 1));
			}
		}
	}

	public Dimension getPreferredSize(JComponent c) {
		FontMetrics metrics = c.getFontMetrics(c.getFont());
		String tipText = ((JToolTip) c).getTipText();
		if (tipText == null) {
			tipText = "";
		}
		BufferedReader br = new BufferedReader(new StringReader(tipText));
		String line;
		int localMaxWidth = 0;
		Vector<String> v = new Vector<String>();
		try {
			while ((line = br.readLine()) != null) {
				int width = SwingUtilities.computeStringWidth(metrics, line);
				localMaxWidth = (localMaxWidth < width) ? width : localMaxWidth;
				v.addElement(line);
			}
		} catch (IOException e) {
			LOG.error("Can't read tip text", e);
		}
		int lines = v.size();
		if (lines < 1) {
			strs = null;
			lines = 1;
		} else {
			strs = new String[lines];
			int i = 0;
			for (Enumeration<String> e = v.elements(); e.hasMoreElements(); i++) {
				strs[i] = (String) e.nextElement();
			}
		}
		int height = metrics.getHeight() * lines;
		return new Dimension(localMaxWidth + 6, height + 4);
	}
}
