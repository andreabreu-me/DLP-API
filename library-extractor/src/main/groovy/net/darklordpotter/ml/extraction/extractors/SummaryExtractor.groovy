package net.darklordpotter.ml.extraction.extractors

import net.darklordpotter.ml.extraction.DataExtractor
import net.darklordpotter.ml.extraction.ExtractionContext

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * 2013-02-07
 * @author Michael Rose <elementation@gmail.com>
 */
class SummaryExtractor implements DataExtractor {
    private static Pattern summary = Pattern.compile("Summary:(.*)")
    ExtractionContext apply(ExtractionContext context) {
        Matcher m = summary.matcher(context.pageText)


        if (m.find()) {
            context.result.summary = m.group(1).trim()
        }

        context
    }
}
