package net.darklordpotter.ml.extraction.filters

import net.darklordpotter.ml.extraction.DataFilter
import net.darklordpotter.ml.extraction.ExtractionContext

/**
 * 2013-02-09
 * @author Michael Rose <elementation@gmail.com>
 */
class QuoteDataFilter implements DataFilter {
    public ExtractionContext filter(ExtractionContext context) {
        context.pageText = context.pageText.replaceAll("‘", "'")
            .replaceAll("’", "'")
            .replaceAll("&#8211;", "–")
            .replaceAll("&#8217;", "'")
            .replaceAll("&#8220;", '"')
            .replaceAll("&#8221;", '"')

        context
    }
}
