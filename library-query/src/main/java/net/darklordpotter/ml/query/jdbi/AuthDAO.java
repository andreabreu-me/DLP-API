package net.darklordpotter.ml.query.jdbi;

import net.darklordpotter.ml.query.api.User;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

/**
 * 2013-06-12
 *
 * @author Michael Rose <lordravenclaw@patronuscharm.net>
 */
@RegisterMapper(UserMapper.class)
public interface AuthDAO {
    @SqlQuery("SELECT MD5(CONCAT(userid,salt)) FROM user WHERE" +
            " (LOWER(username) = LOWER(:username) OR LOWER(email) = LOWER(:username))" +
            " AND MD5(CONCAT(:md5password, salt)) = password")
    public String authUser(@Bind("username") String username, @Bind("md5password") String md5password);

    @SqlQuery("SELECT userid,email,username,usertitle FROM user WHERE MD5(CONCAT(userid,salt)) = :token")
    public User getUserFromToken(@Bind("token") String token);
}
