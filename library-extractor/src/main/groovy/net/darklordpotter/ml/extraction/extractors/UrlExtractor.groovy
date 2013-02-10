package net.darklordpotter.ml.extraction.extractors

import net.darklordpotter.ml.core.Story
import net.darklordpotter.ml.core.Url
import net.darklordpotter.ml.extraction.DataExtractor
import net.darklordpotter.ml.extraction.StoryUrlPattern

import java.util.regex.Matcher

/**
 * 2013-02-09
 * @author Michael Rose <michael@fullcontact.com>
 */
class UrlExtractor implements DataExtractor {
    private final StoryUrlPattern storyUrlPattern

    UrlExtractor(StoryUrlPattern storyUrlPattern) {
        this.storyUrlPattern = storyUrlPattern
    }

    Story apply(String pageText, Story result) {
        Matcher matcher = storyUrlPattern.pattern.matcher(pageText)
        if (matcher.find()) {
            result.url << new Url(storyUrlPattern.name().toLowerCase(), matcher.group(0))
        }

        result
    }
}
