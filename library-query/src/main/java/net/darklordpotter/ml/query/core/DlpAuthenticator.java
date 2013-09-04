package net.darklordpotter.ml.query.core;

import com.google.common.base.Optional;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;
import com.yammer.dropwizard.auth.basic.BasicCredentials;
import net.darklordpotter.ml.query.api.User;
import net.darklordpotter.ml.query.jdbi.AuthDAO;

/**
 * 2013-06-24
 *
 * @author Michael Rose <lordravenclaw@patronuscharm.net>
 */
public class DlpAuthenticator implements Authenticator<BasicCredentials, User> {
    private final AuthDAO dao;

    public DlpAuthenticator(AuthDAO dao) {
        this.dao = dao;
    }

    @Override
    public Optional<User> authenticate(BasicCredentials basicCredentials) throws AuthenticationException {
        User principal = dao.getUserFromToken(basicCredentials.getPassword());
        System.out.println(basicCredentials.getPassword());
        System.out.println(principal);

        return Optional.fromNullable(principal);
    }
}
