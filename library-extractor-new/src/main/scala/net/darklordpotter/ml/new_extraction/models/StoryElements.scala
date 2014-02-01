package net.darklordpotter.ml.new_extraction.models

/**
 * 2013-08-15
 * @author Michael Rose <michael@fullcontact.com>
 */
case class Author(value: String) extends StoryElement
case class Rating(rating: String) extends StoryElement
case class Title(title: String) extends StoryElement
case class Summary(title: String) extends StoryElement
case class Url(urlType: String,link: String) extends StoryElement
case class Tags(tags: List[String]) extends StoryElement