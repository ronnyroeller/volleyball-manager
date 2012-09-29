package com.sport.core.helper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.sport.core.helper.Messages;

public class MessagesTest {

	@Test
	public void testGetStringEnglish() {
		assertEquals("Couldn't read resource file (default language)", "Report software error",
				Messages.getString("aboutdialog_errorreport"));
	}

}
