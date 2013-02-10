package net.darklordpotter.ml.extraction

import net.darklordpotter.ml.core.Story

/**
 * 2013-02-07
 * @author Michael Rose <michael@fullcontact.com>
 */
public interface DataExtractor {
    public Story apply(String pageText, Story result)
}