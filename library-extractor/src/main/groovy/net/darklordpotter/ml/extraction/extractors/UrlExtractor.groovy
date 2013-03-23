package net.darklordpotter.ml.extraction.extractors

import net.darklordpotter.ml.core.Url
import net.darklordpotter.ml.extraction.DataExtractor
import net.darklordpotter.ml.extraction.ExtractionContext
import net.darklordpotter.ml.extraction.StoryUrlPattern

import java.util.regex.Matcher

/**
 * 2013-02-09
 * @author Michael Rose <elementation@gmail.com>
 */
class UrlExtractor implements DataExtractor {
    private final StoryUrlPattern storyUrlPattern

    UrlExtractor(StoryUrlPattern storyUrlPattern) {
        this.storyUrlPattern = storyUrlPattern
    }

    ExtractionContext apply(ExtractionContext context) {
        Matcher matcher = storyUrlPattern.pattern.matcher(context.pageText)
        if (matcher.find()) {
            context.result.url << new Url(storyUrlPattern.name().toLowerCase(), matcher.group(0))
        }

        context
    }
}
