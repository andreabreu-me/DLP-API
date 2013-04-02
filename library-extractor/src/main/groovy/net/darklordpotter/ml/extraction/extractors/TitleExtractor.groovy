package net.darklordpotter.ml.extraction.extractors

import net.darklordpotter.ml.extraction.DataExtractor
import net.darklordpotter.ml.extraction.ExtractionContext

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * 2013-02-07
 * @author Michael Rose <elementation@gmail.com>
 */
class TitleExtractor implements DataExtractor {
    private static Pattern title = Pattern.compile("Title:(.*)")
    ExtractionContext apply(ExtractionContext context) {
        Matcher m = title.matcher(context.pageText)


        if (m.find()) {
            context.result.title = m.group(1).trim()
        }

        if (!context.result.title) {
            context.result.title =
                context.sourceSet.title.replace('&#8216;', '')
                            .replace(' - [a-zA-Z0-9]{1,4}', '')
                            .replaceAll("â€™", "'")
                            .replaceAll("&quot;", "")
        }

        if (context.result.title.contains("http://")) {
            context.result.title = context.result.title.split("/").last()?.replaceAll("_", " ")
        }


        context
    }
}
