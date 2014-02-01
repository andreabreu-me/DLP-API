package net.darklordpotter.library.analytics

import com.twitter.scalding._

class S3Job(args : Args) extends Job(args) {
   // tr.userid,tr.threadid,tr.vote,UNIX_TIMESTAMP(tr.date) as date,t.forumid
  var lines = TextLine(args("input"))

  lines.flatMap('line -> 'word) { line : String => line.split("\\s+") }
    .write( Csv(args("output")) )
 }