package net.darklordpotter.ml.extraction

import java.util.regex.Pattern

/**
 * 2013-02-09
 * @author Michael Rose <michael@fullcontact.com>
 */
public enum StoryUrlPattern {
    FFN("http:\\/\\/(?:www\\.)?fanfiction\\.net\\/s\\/([0-9]+)/([0-9]+)/?([a-z\\-_0-9]*)?"),
    PC("http(?:s)?:\\/\\/(?:www\\.)?patronuscharm\\.net\\/s\\/([0-9]+)/([0-9]+)/?"),
    FICWAD("http:\\/\\/(?:www\\.)?ficwad\\.com/story/([0-9]+)/?"),
    FICWAD_OLD("http:\\/\\/(?:www\\.)?ficwad\\.com/viewstory\\.php\\?sid=([0-9]+)"),
    FFA("http:\\/\\/([a-z0-9_-]+).fanficauthors.net/([a-z\\-_0-9]*)"),
    RS("http:\\/\\/(?:www\\.)?restrictedsection.org\\/file\\.php\\?file=([0-9]+)")

    final Pattern pattern

    StoryUrlPattern(String regex) {
        this.pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
    }
}