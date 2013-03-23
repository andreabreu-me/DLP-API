package net.darklordpotter.ml.query

import net.darklordpotter.ml.extraction.DataFilter
import net.darklordpotter.ml.extraction.filters.BBTextDataFilter
import org.junit.Before
import org.junit.Test;

/**
 * 2013-02-26
 *
 * @author Michael Rose <michael@fullcontact.com>
 */
public class BBTextDataFilterTest {
    DataFilter filter
    @Before
    public void setUp() throws Exception {
        filter = new BBTextDataFilter()
    }

    @Test
    public void testReplacesUrls() {
        String text = "Link: [URL=\"http://www.fanfiction.net/s/7069833/1/Geminio\"]FF.net[/URL]"

        assert filter.filter(text) == "Link: http://www.fanfiction.net/s/7069833/1/Geminio"
    }
}
