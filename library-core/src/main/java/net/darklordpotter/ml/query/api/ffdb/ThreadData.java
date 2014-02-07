package net.darklordpotter.ml.query.api.ffdb;

import lombok.Data;

/**
 * 2014-02-05
 *
 * @author Michael Rose <michael@fullcontact.com>
 */
@Data
public class ThreadData {
    long ffnId;
    long threadId;
    long votenum;
    long votetotal;

    public ThreadData(long ffnId, long threadId, long votenum, long votetotal) {
        this.ffnId = ffnId;
        this.threadId = threadId;
        this.votenum = votenum;
        this.votetotal = votetotal;
    }
}
