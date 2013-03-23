package net.darklordpotter.ml.extraction.extractors

import com.google.common.base.Splitter
import net.darklordpotter.ml.extraction.DataExtractor
import net.darklordpotter.ml.extraction.ExtractionContext

/**
 * 2013-02-07
 * @author Michael Rose <elementation@gmail.com>
 */
class TagExtractor implements DataExtractor {
    static Splitter SPLITTER = Splitter.on(",").trimResults()
    ExtractionContext apply(ExtractionContext context) {
        Map resultSet = context.sourceSet



        if (resultSet.tags) {
            Iterable<String> tags = SPLITTER.split(resultSet.tags)

            for (String tag : tags) {
                context.result.tags.add(tag)
            }
        }
        context
    }
}
