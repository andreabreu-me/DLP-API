package net.darklordpotter.ml.query.api.subscriptions;

import lombok.Data;

/**
 * 2013-06-24
 *
 * @author Michael Rose <lordravenclaw@patronuscharm.net>
 */
@Data
public class ThreadSubscription {
    Long subscribeThreadId;
    Long userId;
    Long threadId;
    Short emailUpdate;
    Long folderId;
    Boolean canView;
}
