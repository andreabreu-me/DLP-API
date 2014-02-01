package net.darklordpotter.ml.extraction.filters

import net.darklordpotter.ml.extraction.DataFilter
import net.darklordpotter.ml.extraction.ExtractionContext

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * 2013-02-09
 * @author Michael Rose <elementation@gmail.com>
 */
class BBTextDataFilter implements DataFilter {
    private static Pattern bbtxt = Pattern.compile("\\[[^\\]]+\\]")
    private static Pattern bbtxtUrl = Pattern.compile("\\[URL=\"([^\\]]+)\"\\]([^\\[]+)")
    private static Pattern bbtxtColor = Pattern.compile("\\[COLOR=\"([^\\]]+)\"\\]")
    public ExtractionContext filter(ExtractionContext context) {
        Matcher urlm = bbtxtUrl.matcher(context.pageText)

        if (urlm.find()) {
            String url = urlm.group(1)
            context.pageText = urlm.replaceAll(url)
        }

        Matcher colorm = bbtxtColor.matcher(context.pageText)
        if (colorm.find()) {
            context.pageText = colorm.replaceAll("")
        }

        Matcher m = bbtxt.matcher(context.pageText)
        if (m.find()) {
            context.pageText = m.replaceAll("")
        }

        return context
    }
}
