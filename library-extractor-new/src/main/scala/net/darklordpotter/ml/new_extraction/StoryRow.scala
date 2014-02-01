package net.darklordpotter.ml.new_extraction

import scala.collection.JavaConverters._

/**
 * 2013-08-15
 * @author Michael Rose <michael@fullcontact.com>
 */
object StoryRow {
//  def fromLine(line: String) = {
//    val columns = line.split('\t')
//    StoryRow(
//      columns(0),
//      columns(1),
//      columns(2),
//      columns(3),
//      columns(4),
//      columns(5),
//      columns(6),
//      columns(7),
//      columns(8),
//      columns(9).split(',').toList.map(_.trim)
//    )
//  }

  def fromMap(map: java.util.Map[_,_]) = {
    StoryRow(
      map.get("pagetext").asInstanceOf[String],
      map.get("dateline").asInstanceOf[Long],
      map.get("threadid").asInstanceOf[Long],
      map.get("title").asInstanceOf[String],
      map.get("votenum").asInstanceOf[Integer],
      map.get("votetotal").asInstanceOf[Integer],
      map.get("replycount").asInstanceOf[Long],
      map.get("lastpost").asInstanceOf[Long],
      map.get("forumname").asInstanceOf[String],
      List()
    )
  }
}
case class StoryRow(var pagetext: String, dateline: Long, threadid: Long, var title: String, votenum: Integer,
                 votetotal: Integer, replycount: Long, lastpost: Long, var forumname: String, tags: List[String]) {

  def applyToEach(transform: (String) => String) {
    pagetext = transform.apply(pagetext)
    title = transform.apply(title)
    forumname = transform.apply(forumname)
  }

}
