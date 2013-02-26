package net.darklordpotter.ml.extraction.filters

import net.darklordpotter.ml.extraction.DataFilter

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * 2013-02-09
 * @author Michael Rose <elementation@gmail.com>
 */
class BBTextDataFilter implements DataFilter {
    private static Pattern bbtxt = Pattern.compile("\\[[^\\]]+\\]")
    private static Pattern bbtxtUrl = Pattern.compile("\\[URL=\"([^\\]]+)\"\\]")
    private static Pattern bbtxtColor = Pattern.compile("\\[COLOR=\"([^\\]]+)\"\\]")
    public String filter(String text) {
        Matcher urlm = bbtxtUrl.matcher(text)

        if (urlm.find()) {
            urlm.replaceAll(urlm.group(1))
        }

        Matcher colorm = bbtxtColor.matcher(text)
        if (colorm.find()) {
            colorm.replaceAll("")
        }

        Matcher m = bbtxt.matcher(text)
        if (m.find()) {
            m.replaceAll("")
        }
    }
}
