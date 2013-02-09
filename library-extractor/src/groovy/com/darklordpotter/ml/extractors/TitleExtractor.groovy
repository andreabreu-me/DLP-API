package com.darklordpotter.ml.extractors

import com.darklordpotter.ml.DataExtractor
import com.darklordpotter.ml.api.Story

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * 2013-02-07
 * @author Michael Rose <michael@fullcontact.com>
 */
class TitleExtractor implements DataExtractor {
    private static Pattern title = Pattern.compile("Title:(.*)")
    Story apply(String pageText, Story result) {
        Matcher m = title.matcher(pageText)


        if (m.find()) {
            result.title = m.group(1).trim()
        }

        result
    }
}
