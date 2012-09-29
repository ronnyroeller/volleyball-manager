package com.sport.client;

import java.awt.Frame;
import java.lang.Thread.UncaughtExceptionHandler;

import javax.swing.JFrame;

import org.apache.log4j.Logger;

/**
 * Handles all exceptions that weren't handle by the Administrator
 * 
 * @author Ronny
 * 
 */
public class DefaultExceptionHandler implements UncaughtExceptionHandler {

	private static final Logger LOG = Logger
			.getLogger(DefaultExceptionHandler.class.getName());

	public void uncaughtException(Thread t, Throwable e) {
		LOG.error("Uncatched error", e);
	}

	private Frame findActiveFrame() {
		Frame[] frames = JFrame.getFrames();
		for (int i = 0; i < frames.length; i++) {
			if (frames[i].isVisible()) {
				return frames[i];
			}
		}
		return null;
	}

}
