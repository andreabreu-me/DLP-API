package net.darklordpotter.ml.new_extraction.filters

import net.darklordpotter.ml.new_extraction.ExtractionAction
import net.darklordpotter.ml.new_extraction.LibraryExtractor.ExtractionContext
import java.net.URLDecoder
import java.nio.charset.Charset

/**
 * 2013-08-19
 * @author Michael Rose <michael@fullcontact.com>
 */
class UrlDecoderFilter extends ExtractionAction {
  def apply(ctx: ExtractionContext) {
    ctx.story.applyToEach { field =>
      try {
        URLDecoder.decode(field, Charset.defaultCharset().toString).replace("&amp;", "&")
      } catch {
        case _:Throwable => field
      }
    }
  }
}