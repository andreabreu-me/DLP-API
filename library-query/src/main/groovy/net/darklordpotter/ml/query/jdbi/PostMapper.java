package net.darklordpotter.ml.query.jdbi;

import net.darklordpotter.ml.query.api.Post;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 2013-03-27
 *
 * @author Michael Rose <michael@fullcontact.com>
 */
public class PostMapper implements ResultSetMapper<Post> {
    public Post map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        Post p = new Post();
        p.setPostId(resultSet.getLong("postid"));
        p.setThreadId(resultSet.getLong("threadid"));
        p.setUsername(resultSet.getString("username"));
        p.setUserId(resultSet.getLong("threadid"));
        p.setTitle(resultSet.getString("title"));
        p.setDateline(resultSet.getLong("dateline"));
        p.setPageText(resultSet.getString("pagetext"));
        return p;
    }
}
