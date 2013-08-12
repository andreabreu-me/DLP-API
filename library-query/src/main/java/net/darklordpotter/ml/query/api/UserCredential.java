package net.darklordpotter.ml.query.api;

import com.google.common.base.Objects;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 2013-06-12
 *
 * @author Michael Rose <lordravenclaw@patronuscharm.net>
 */
public class UserCredential {
    @NotEmpty
    String username;

    @NotEmpty
    String md5password;

    public UserCredential() {
    }

    public UserCredential(String username, String md5password) {
        this.username = username;
        this.md5password = md5password;
    }

    public String getUsername() {
        return username;
    }

    public String getMd5password() {
        return md5password;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("username", username).add("md5password", md5password).toString();
    }
}
