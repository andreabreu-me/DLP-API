package net.darklordpotter.ml.query.api

/**
 * 2013-02-11
 * @author Michael Rose <elementation@gmail.com>
 */
class TagResult {
    String tag
    Long count
    TagResult() {}

    TagResult(String tag, Long count) {
        this.tag = tag
        this.count = count
    }
}
