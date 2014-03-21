package net.darklordpotter.ml.new_extraction.extractors

import org.scalatest.FunSuite;

/**
 * 2013-08-24
 *
 * @author Michael Rose
 */
class TitleExtractorTest extends FunSuite {
  val titleExtractor = new TitleExtractor

  test("Should extract title, author, and rating from a traditional thread title") {
    val res = titleExtractor.applyRegexAll(titleExtractor.threadTitle, "Rectifier by Niger Aquila - K")
    assert(res === List("Rectifier", "Niger Aquila", "K"))
  }

}
