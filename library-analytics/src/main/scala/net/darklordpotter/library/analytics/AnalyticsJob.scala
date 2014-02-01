package net.darklordpotter.library.analytics

import com.twitter.scalding._

class AnalyticsJob(args : Args) extends Job(args) {
   // tr.userid,tr.threadid,tr.vote,UNIX_TIMESTAMP(tr.date) as date,t.forumid
   val input = Csv( "/tmp/library_latest.csv", skipHeader = true)

  input.write( Csv(args("output")) )
 }