package net.darklordpotter.ml.query.jdbi;

import net.darklordpotter.ml.query.api.User;
import org.skife.jdbi.v2.BeanMapper;

/**
 * 2013-06-24
 *
 * @author Michael Rose <lordravenclaw@patronuscharm.net>
 */
public class UserMapper extends BeanMapper<User> {
    public UserMapper() {
        super(User.class);
    }
}
