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
    private static Pattern bbtxtUrl = Pattern.compile("\\[URL=\"(.*)\"\\]")
    public String filter(String text) {
        Matcher urlm = bbtxtUrl.matcher(text)

        if (urlm.find()) {
            urlm.replaceAll(urlm.group(1))
        }
        Matcher m = bbtxt.matcher(text)
        m.replaceAll("")
    }
}
