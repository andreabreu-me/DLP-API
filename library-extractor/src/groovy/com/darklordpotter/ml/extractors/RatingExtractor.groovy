package com.darklordpotter.ml.extractors

import com.darklordpotter.ml.DataExtractor
import com.darklordpotter.ml.api.Rating
import com.darklordpotter.ml.api.Story

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
            String ratingText = m.group(1).trim().replace("-", "").replace("+", "PLUS")
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
