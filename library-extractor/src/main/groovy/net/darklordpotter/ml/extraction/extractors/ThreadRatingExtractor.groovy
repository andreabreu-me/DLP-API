package net.darklordpotter.ml.extraction.extractors

import net.darklordpotter.ml.extraction.DataExtractor
import net.darklordpotter.ml.extraction.ExtractionContext

/**
 * 2013-02-07
 * @author Michael Rose <elementation@gmail.com>
 */
class ThreadRatingExtractor implements DataExtractor {
    ExtractionContext apply(ExtractionContext context) {
        Map resultSet = context.sourceSet

        int C = 10
        double m = 3.5

        context.result.threadRating = (double)resultSet.votetotal / resultSet.votenum
        context.result.adjustedThreadRating = (C*m+(double)resultSet.votetotal) / (C+resultSet.votenum)

        context
    }
}
