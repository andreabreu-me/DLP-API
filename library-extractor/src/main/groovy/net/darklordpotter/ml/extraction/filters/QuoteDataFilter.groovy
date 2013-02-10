package net.darklordpotter.ml.extraction.filters

import net.darklordpotter.ml.extraction.DataFilter

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * 2013-02-09
 * @author Michael Rose <michael@fullcontact.com>
 */
class QuoteDataFilter implements DataFilter {
    public String filter(String text) {
        text.replaceAll("â€™", "'")
            .replaceAll("&#8217;", "'")
            .replaceAll("&#8220;", '"')
            .replaceAll("&#8221;", '"')
    }
}
