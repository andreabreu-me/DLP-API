package net.darklordpotter.ml.extraction.utils

/**
 * 2013-03-05
 * @author Michael Rose <elementation@gmail.com>
 */
class TimingUtil {
    public static <V> V timeIt(String type = "", Closure<V> cl) {
        long nanoStart = System.nanoTime()
        try {
            return cl.call()
        } finally {
            println type + (System.nanoTime() - nanoStart)/1000000.0 + "ms"
        }
    }

}
