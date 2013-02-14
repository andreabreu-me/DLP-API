package net.darklordpotter.ml.extraction.extractors

import net.darklordpotter.ml.extraction.DataExtractor
import net.darklordpotter.ml.core.Rating
import net.darklordpotter.ml.core.Story

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * 2013-02-07
 * @author Michael Rose <michael@fullcontact.com>
 */
class RatingExtractor implements DataExtractor {
    private static Pattern rating = Pattern.compile("Rating:(.*)")
    Story apply(String pageText, Story result) {
        Matcher m = rating.matcher(pageText)

        if (m.find()) {
            String ratingText = m.group(1).replaceAll("\\(([^\\)]+)\\)", "").replace("-", "").replace("+", "PLUS").trim()
            try {
                result.rating = Enum.valueOf(Rating, ratingText)
            } catch (IllegalArgumentException e) {
                result.rating = Rating.UNKNOWN
            } catch (NullPointerException e) {
                result.rating = Rating.UNKNOWN
            }
        }

        result
    }
}
