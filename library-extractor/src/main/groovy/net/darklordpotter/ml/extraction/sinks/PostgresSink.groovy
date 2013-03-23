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
// * 2013-03-10
// * @author Michael Rose <michael@fullcontact.com>
// */
//class PostgresSink implements DataSink {
//    StoryDto storyInterface
//
//    PostgresSink() {
//        PGPoolingDataSource dataSource = new PGPoolingDataSource()
//        dataSource.setDatabaseName("library_dlp")
//        DBI dbi = new DBI(dataSource)
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
//    @SqlUpdate("INSERT INTO stories (id, title) VALUES (:threadIdLong, :title)")
//    void insertStory(@BindBean Story story)
//}
//
//public class PostgresIntegerArrayArgumentFactory implements ArgumentFactory<SqlArray<Integer>>
//{
//    public boolean accepts(Class<?> expectedType, Object value, StatementContext ctx)
//    {
//        return value instanceof SqlArray && ((SqlArray)value).getType().isAssignableFrom(Integer.class);
//    }
//
//    public Argument build(Class<?> expectedType, final SqlArray<Integer> value, StatementContext ctx)
//    {
//        return new Argument() {
//            public void apply(int position, PreparedStatement statement, StatementContext ctx) throws SQLException {
//                // in postgres no need to (and in fact cannot) free arrays
//                Array ary = ctx.getConnection()
//                        .createArrayOf("integer", value.getElements());
//                statement.setArray(position, ary);
//            }
//        };
//    }
//}
//
//public class SqlArray<T>
//{
//    private final Object[] elements;
//    private final Class<T> type;
//
//    public SqlArray(Class<T> type, Collection<T> elements) {
//        this.elements = Iterables.toArray(elements, Object.class);
//        this.type = type;
//    }
//
//    public static <T> SqlArray<T> arrayOf(Class<T> type, T... elements) {
//        return new SqlArray<T>(type, asList(elements));
//    }
//
//    public static <T> SqlArray<T> arrayOf(Class<T> type, Iterable<T> elements) {
//        return new SqlArray<T>(type, elements);
//    }
//
//    public Object[] getElements()
//    {
//        return elements;
//    }
//
//    public Class<T> getType()
//    {
//        return type;
//    }
//}