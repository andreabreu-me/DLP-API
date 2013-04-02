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

        context.result.threadRating = (double)resultSet.votetotal / resultSet.votenum



        context.result.adjustedThreadRating = bayesianAverage((double)resultSet.votetotal, (double)resultSet.votenum)

        context
    }

    private double bayesianAverage(double voteTotal, double voteNum) {
        // http://fulmicoton.com/posts/bayesian_rating
        int C = 10
        double m = 4.0
        return (C*m+(double)voteTotal) / (C+voteNum)
    }
}
