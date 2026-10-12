/*
 * Copyright (c) 2025 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.page.validators;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WholescriptsEmojiValidation {

	public static boolean emojiFinder(final String text) {

		boolean emojiPresent = false;

		final String decodedString = URLDecoder.decode(text, StandardCharsets.UTF_8);

		// Regex pattern to match common emojis
		final String emojiRegex = "[\\p{So}\\p{Cn}]"; // Matches symbols and unassigned code points (includes emojis)
		// More specific range for common emojis
		final String specificEmojiRegex = "[\\uD83C-\\uD83E][\\uDD00-\\uDFFF]|[\\uD83D-\\uD83E][\\uDC00-\\uDFFF]|[\\u2600-\\u26FF]|[\\u2700-\\u27BF]";

		final Pattern pattern1 = Pattern.compile(emojiRegex);
		final Matcher matcher1 = pattern1.matcher(decodedString);

		final Pattern pattern2 = Pattern.compile(specificEmojiRegex);
		final Matcher matcher2 = pattern2.matcher(decodedString);


		while (matcher1.find() || matcher2.find()) {
			emojiPresent = true;
			break;
		}
		return emojiPresent;
	}

}
