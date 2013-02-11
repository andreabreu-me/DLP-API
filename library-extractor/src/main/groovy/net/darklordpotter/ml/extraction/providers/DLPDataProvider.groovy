package net.darklordpotter.ml.extraction.providers

import groovy.sql.Sql
import net.darklordpotter.ml.extraction.DataProvider

/**
 * 2013-02-10
 * @author Michael Rose <michael@fullcontact.com>
 */
class DLPDataProvider implements DataProvider {
    private String host
    private String database
    private String user
    private String pass

    public DLPDataProvider(String host, String database, String user, String pass = "") {
        this.host = host
        this.database = database
        this.user = user
        this.pass = pass
    }

    Iterable<Map> getData() {
        Sql h = Sql.newInstance("jdbc:mysql://${host}/${database}", user, pass)

        h.rows(dlpQuery) as Iterable<Map>
    }

    static String getDlpQuery() {
        """
            SELECT
                    post.pagetext, post.threadid, thread.title, thread.votenum, thread.votetotal, thread.replycount,
                    thread.lastpost, forum.title as forumname,
                    GROUP_CONCAT(tag.tagtext) as tags
            FROM post
            LEFT JOIN thread
                    USING (threadid)
            LEFT JOIN forum
                    USING (forumid)
            LEFT JOIN tagthread
                    USING (threadid)
            LEFT JOIN tag
                    USING (tagid)
            WHERE thread.sticky = '0'
            AND thread.visible = '1'
            AND post.parentid = '0'
            AND forum.parentid = '2'
            AND forum.forumid NOT IN (40, 77, 41)
            GROUP BY threadid
            ORDER BY post.dateline ASC
        """.toString()
    }
}
