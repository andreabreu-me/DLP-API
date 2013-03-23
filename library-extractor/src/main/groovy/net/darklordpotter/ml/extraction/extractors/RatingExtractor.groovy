package net.darklordpotter.ml.extraction.extractors

import net.darklordpotter.ml.extraction.DataExtractor
import net.darklordpotter.ml.core.Rating
import net.darklordpotter.ml.extraction.ExtractionContext

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * 2013-02-07
 * @author Michael Rose <elementation@gmail.com>
 */
class RatingExtractor implements DataExtractor {
    private static Pattern rating = Pattern.compile("Rating:(.*)")
    ExtractionContext apply(ExtractionContext context) {
        Matcher m = rating.matcher(context.pageText)

        if (m.find()) {
            String ratingText = m.group(1).replaceAll("\\(([^\\)]+)\\)", "").replace("-", "").replace("+", "PLUS").trim()
            try {
                context.result.rating = Enum.valueOf(Rating, ratingText)
            } catch (IllegalArgumentException e) {
                context.result.rating = Rating.UNKNOWN
            } catch (NullPointerException e) {
                context.result.rating = Rating.UNKNOWN
            }
        }

        context
    }
}
