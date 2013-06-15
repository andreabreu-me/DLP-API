package net.darklordpotter.ml.query.jdbi;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

/**
 * 2013-06-12
 *
 * @author Michael Rose <michael@fullcontact.com>
 */
public interface AuthDAO {
    @SqlQuery("SELECT MD5(CONCAT(userid,salt)) FROM user WHERE" +
            " (LOWER(username) = LOWER(:username) OR LOWER(email) = LOWER(:username))" +
            " AND MD5(CONCAT(:md5password, salt)) = password")
    public String authUser(@Bind("username") String username, @Bind("md5password") String md5password);
}
