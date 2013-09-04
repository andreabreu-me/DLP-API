package net.darklordpotter.ml.query.api;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * 2013-06-24
 *
 * @author Michael Rose <lordravenclaw@patronuscharm.net>
 */
@Data
public class User {
    Long userId;
    String username;
    String email;

    String userTitle;
}
