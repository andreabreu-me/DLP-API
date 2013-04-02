package net.darklordpotter.ml.extraction.extractors

import net.darklordpotter.ml.extraction.DataExtractor
import net.darklordpotter.ml.extraction.ExtractionContext

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * 2013-02-07
 * @author Michael Rose <elementation@gmail.com>
 */
class AuthorExtractor implements DataExtractor {
    private static Pattern author = Pattern.compile("Author:(.*)")
    ExtractionContext apply(ExtractionContext context) {
        Matcher m = author.matcher(context.pageText)


        if (m.find()) {
            context.result.author = m.group(1).replaceAll("\\(([^\\)]+)\\)", "").trim()
        }

        if (context.result.author.contains("http://")) {
            context.result.author = context.result.author.split("/").last()?.replaceAll("_", " ")
        }

        if (context.result.author.trim().isEmpty()) {
            //context.result.title
        }

        context
    }
}
