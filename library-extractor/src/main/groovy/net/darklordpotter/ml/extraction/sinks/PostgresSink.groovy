//package net.darklordpotter.ml.extraction.sinks
//
//import com.google.common.collect.Iterables
//import net.darklordpotter.ml.extraction.DataSink
//import org.postgresql.ds.PGPoolingDataSource
//import org.skife.jdbi.v2.DBI
//import org.skife.jdbi.v2.StatementContext
//import org.skife.jdbi.v2.sqlobject.BindBean
//import org.skife.jdbi.v2.sqlobject.SqlUpdate
//import net.darklordpotter.ml.core.Story
//import org.skife.jdbi.v2.tweak.Argument
//import org.skife.jdbi.v2.tweak.ArgumentFactory
//
//import java.lang.reflect.Array
//import java.sql.PreparedStatement
//import java.sql.SQLException
//
///**
//* 2013-03-10
//* @author Michael Rose <elementation@gmail.com>
//*/
//class PostgresSink implements DataSink {
//    StoryDto storyInterface
//
//    PostgresSink() {
//        PGPoolingDataSource dataSource = new PGPoolingDataSource()
//        dataSource.setDatabaseName("library_dlp")
//        DBI dbi = new DBI(dataSource)
//        dbi.registerArgumentFactory(new PostgresStringSetArgumentFactory())
//
//        storyInterface = dbi.onDemand(StoryDto)
//    }
//
//    void insertStory(Story result) {
//        SqlArray
//        storyInterface.insertStory(result)
//    }
//}
//
//
//interface StoryDto {
//    @SqlUpdate("INSERT INTO stories\n" +
//            "(id, title, tags) " +
//            "VALUES (:threadIdLong, :title, :tags)")
//    void insertStory(@BindBean Story story)
//}