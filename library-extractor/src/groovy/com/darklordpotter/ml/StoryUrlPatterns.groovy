package com.darklordpotter.ml

import java.util.regex.Pattern

/**
 * 2013-02-09
 * @author Michael Rose <michael@fullcontact.com>
 */
public enum StoryUrlPatterns {
    FFN("http:\\/\\/(?:www\\.)?fanfiction\\.net\\/s\\/([0-9]+)/([0-9]+)/?([a-z\\-0-9]*)?"),
    PC("http(?:s)?:\\/\\/(?:www\\.)?patronuscharm\\.net\\/s\\/([0-9]+)/([0-9]+)/?")

    final Pattern pattern

    StoryUrlPatterns(String regex) {
        this.pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
    }
}