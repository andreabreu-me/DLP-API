package net.darklordpotter.ml.core

import groovy.transform.Canonical

/**
 * 2013-02-09
 * @author Michael Rose <elementation@gmail.com>
 */
@Canonical
class Url {
    String type
    String url

    Url() {}

    Url(String type, String url) {
        this.type = type
        this.url = url
    }
}
