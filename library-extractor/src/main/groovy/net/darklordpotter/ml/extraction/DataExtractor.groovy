package net.darklordpotter.ml.extraction

import net.darklordpotter.ml.core.Story

/**
 * 2013-02-07
 * @author Michael Rose <elementation@gmail.com>
 */
public interface DataExtractor {
    public ExtractionContext apply(ExtractionContext context)
}