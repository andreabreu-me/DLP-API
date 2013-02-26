package net.darklordpotter.ml.extraction.extractors

import net.darklordpotter.ml.core.Story
import net.darklordpotter.ml.extraction.DataExtractor

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * 2013-02-07
 * @author Michael Rose <elementation@gmail.com>
 */
class SummaryExtractor implements DataExtractor {
    private static Pattern summary = Pattern.compile("Summary:(.*)")
    Story apply(String pageText, Story result) {
        Matcher m = summary.matcher(pageText)


        if (m.find()) {
            result.summary = m.group(1).trim()
        }

        result
    }
}
