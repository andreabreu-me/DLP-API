package net.darklordpotter.ml.extraction.extractors

import net.darklordpotter.ml.extraction.DataExtractor
import net.darklordpotter.ml.core.Story

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * 2013-02-07
 * @author Michael Rose <michael@fullcontact.com>
 */
class AuthorExtractor implements DataExtractor {
    private static Pattern author = Pattern.compile("Author:(.*)")
    Story apply(String pageText, Story result) {
        Matcher m = author.matcher(pageText)


        if (m.find()) {
            result.author = m.group(1).replaceAll("\\(([^\\)]+)\\)", "").trim()
        }

        result
    }
}
