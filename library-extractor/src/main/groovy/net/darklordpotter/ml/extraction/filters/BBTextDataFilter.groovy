package net.darklordpotter.ml.extraction.filters

import net.darklordpotter.ml.extraction.DataFilter

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * 2013-02-09
 * @author Michael Rose <michael@fullcontact.com>
 */
class BBTextDataFilter implements DataFilter {
    private static Pattern bbtxt = Pattern.compile("\\[[^\\]]+\\]")
    public String filter(String text) {
        Matcher m = bbtxt.matcher(text)

        m.replaceAll("")
    }
}
