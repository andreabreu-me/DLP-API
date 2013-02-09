package com.darklordpotter.ml.extractors

import com.darklordpotter.ml.DataExtractor
import com.darklordpotter.ml.api.Story

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * 2013-02-09
 * @author Michael Rose <michael@fullcontact.com>
 */
class UrlExtractor implements DataExtractor {
    private final Pattern urlPattern

    UrlExtractor(Pattern urlPattern) {
        this.urlPattern = urlPattern
    }

    Story apply(String pageText, Story result) {
        Matcher matcher = urlPattern.matcher(pageText)
        if (matcher.find()) {
            result.url << matcher.group(0)
        }

        result
    }
}
