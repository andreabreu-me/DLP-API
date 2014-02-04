package net.darklordpotter.ml.extraction.extractors

import net.darklordpotter.ml.core.DLPMetaData
import net.darklordpotter.ml.extraction.DataExtractor
import net.darklordpotter.ml.extraction.ExtractionContext
import org.joda.time.DateTime

/**
 * 2013-02-07
 * @author Michael Rose <elementation@gmail.com>
 */
class ThreadMetadataExtractor implements DataExtractor {
    ExtractionContext apply(ExtractionContext context) {
        Map resultSet = context.sourceSet

        context.result.threadRating = (double)resultSet.votetotal / resultSet.votenum

        context.result.meta.dlp = new DLPMetaData()
        context.result.meta.dlp.posts = resultSet.replycount
        context.result.meta.dlp.views = resultSet.views
        context.result.meta.dlp.posted = new DateTime((long)resultSet.dateline)
        context.result.meta.dlp.lastPost = new DateTime((long)resultSet.lastpost)
        context.result.meta.dlp.forum = resultSet.forumname
        context.result.tags << "forum:" + resultSet.forumname

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
