package net.darklordpotter.ml.query.api;

import lombok.Data;
import org.joda.time.DateTime;

import java.util.Date;

/**
 * 2013-11-29
 *
 * @author Michael Rose <michael@fullcontact.com>
 */

public class ThreadRating {
    private long threadRateId;
    private long threadId;
    private long userId;
    private int vote;
    private String ipaddress;
    private Date date;

    public long getThreadRateId() {
        return threadRateId;
    }

    public void setThreadRateId(long threadRateId) {
        this.threadRateId = threadRateId;
    }

    public long getThreadId() {
        return threadId;
    }

    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
