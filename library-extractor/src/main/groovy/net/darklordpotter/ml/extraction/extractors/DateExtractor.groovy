package net.darklordpotter.ml.extraction.extractors

import net.darklordpotter.ml.extraction.DataExtractor
import net.darklordpotter.ml.extraction.ExtractionContext

/**
 * 2013-02-07
 * @author Michael Rose <elementation@gmail.com>
 */
class DateExtractor implements DataExtractor {
    ExtractionContext apply(ExtractionContext context) {
        context.result.posted = new Date(context.sourceSet.dateline * 1000) // unix timestamp

        context
    }
}
