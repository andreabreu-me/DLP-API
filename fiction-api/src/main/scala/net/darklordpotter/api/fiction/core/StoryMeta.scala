package net.darklordpotter.api.fiction.core

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * 2014-03-19
 * @author Michael Rose
 */

/*
    String status;
    String language;
    String rated;
    long ratings;
    long chapters;
    long favs;
    long follows;
    long reviews;
    long words;
    List<Category> categories = Collections.emptyList();
    List<List<Character>> relationships = Collections.emptyList();
    List<Character> characters;

    ThreadData threadData;
 */

@JsonIgnoreProperties(ignoreUnknown = true)
case class StoryMeta(
                      status: String,
                      language: String,
                      rated: String,
                      ratings: Long,
                      chapters: Long,
                      favs: Long,
                      follows: Long,
                      reviews: Long,
                      words: Long,
                      categories: Seq[Category] = Seq(),
                      relationships: Seq[Seq[Character]] = Seq(),
                      characters: Seq[Character] = Seq(),
                      threadData: ThreadData) {
  def addThreadData(threadData: ThreadData) = {
    this.copy(threadData = threadData)
  }
}