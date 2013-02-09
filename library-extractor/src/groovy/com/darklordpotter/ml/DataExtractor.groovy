package com.darklordpotter.ml

import com.darklordpotter.ml.api.Story

/**
 * 2013-02-07
 * @author Michael Rose <michael@fullcontact.com>
 */
public interface DataExtractor {
    public Story apply(String pageText, Story result)
}