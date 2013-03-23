package net.darklordpotter.ml.extraction

import net.darklordpotter.ml.core.Story

/**
 * 2013-03-06
 * @author Michael Rose <michael@fullcontact.com>
 */
class ExtractionContext {
    Map sourceSet
    String pageText
    Story result

    ExtractionContext() {}

    ExtractionContext(Map sourceSet) {
        this.sourceSet = sourceSet
        this.pageText = sourceSet.pageText
        this.result = new Story(sourceSet.threadId)
    }
}
