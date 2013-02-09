package com.darklordpotter.ml.extractors

import com.darklordpotter.ml.DataExtractor
import com.darklordpotter.ml.api.Story

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * 2013-02-07
 * @author Michael Rose <michael@fullcontact.com>
 */
class AuthorExtractor implements DataExtractor {
    private static Pattern author = Pattern.compile("Author:(.*)")
    Story apply(String pageText, Story result) {
        Matcher m = author.matcher(pageText)


        if (m.find()) {
            result.author = m.group(1).trim()
        }

        result
    }
}
