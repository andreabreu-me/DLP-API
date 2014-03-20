//package net.darklordpotter.api.fiction.core;
//
///**
// * 2014-02-05
// *
// * @author Michael Rose <michael@fullcontact.com>
// */
//public class ThreadData {
//    long ffnId;
//    long threadId;
//    long votenum;
//    long votetotal;
//
//    public ThreadData(long ffnId, long threadId, long votenum, long votetotal) {
//        this.ffnId = ffnId;
//        this.threadId = threadId;
//        this.votenum = votenum;
//        this.votetotal = votetotal;
//    }
//
//    public long getFfnId() {
//        return this.ffnId;
//    }
//
//    public long getThreadId() {
//        return this.threadId;
//    }
//
//    public long getVotenum() {
//        return this.votenum;
//    }
//
//    public long getVotetotal() {
//        return this.votetotal;
//    }
//
//    public void setFfnId(long ffnId) {
//        this.ffnId = ffnId;
//    }
//
//    public void setThreadId(long threadId) {
//        this.threadId = threadId;
//    }
//
//    public void setVotenum(long votenum) {
//        this.votenum = votenum;
//    }
//
//    public void setVotetotal(long votetotal) {
//        this.votetotal = votetotal;
//    }
//
//    public boolean equals(Object o) {
//        if (o == this) return true;
//        if (!(o instanceof ThreadData)) return false;
//        final ThreadData other = (ThreadData) o;
//        if (!other.canEqual((Object) this)) return false;
//        if (this.ffnId != other.ffnId) return false;
//        if (this.threadId != other.threadId) return false;
//        if (this.votenum != other.votenum) return false;
//        if (this.votetotal != other.votetotal) return false;
//        return true;
//    }
//
//    public int hashCode() {
//        final int PRIME = 59;
//        int result = 1;
//        final long $ffnId = this.ffnId;
//        result = result * PRIME + (int) ($ffnId >>> 32 ^ $ffnId);
//        final long $threadId = this.threadId;
//        result = result * PRIME + (int) ($threadId >>> 32 ^ $threadId);
//        final long $votenum = this.votenum;
//        result = result * PRIME + (int) ($votenum >>> 32 ^ $votenum);
//        final long $votetotal = this.votetotal;
//        result = result * PRIME + (int) ($votetotal >>> 32 ^ $votetotal);
//        return result;
//    }
//
//    public boolean canEqual(Object other) {
//        return other instanceof ThreadData;
//    }
//
//    public String toString() {
//        return "net.darklordpotter.api.fiction.core.ThreadData(ffnId=" + this.ffnId + ", threadId=" + this.threadId + ", votenum=" + this.votenum + ", votetotal=" + this.votetotal + ")";
//    }
//}
