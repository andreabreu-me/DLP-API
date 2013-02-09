package com.darklordpotter.ml

import com.darklordpotter.ml.api.Story
import com.darklordpotter.ml.extractors.AuthorExtractor
import com.darklordpotter.ml.extractors.RatingExtractor
import com.darklordpotter.ml.extractors.TitleExtractor
import com.darklordpotter.ml.extractors.UrlExtractor
import com.darklordpotter.ml.filters.BBTextDataFilter
import com.mongodb.DB
import com.mongodb.DBCollection
import com.mongodb.MongoClient
import groovy.sql.GroovyResultSet
import groovy.sql.Sql
import net.vz.mongodb.jackson.JacksonDBCollection

/**
 * 2013-02-07
 * @author Michael Rose <michael@fullcontact.com>
 */
class DatabaseExtractor {
    static final List<DataFilter> filters = [new BBTextDataFilter()]
    static final List<DataExtractor> extractors = [
             new TitleExtractor()
            ,new AuthorExtractor()
            ,new RatingExtractor()
            ,new UrlExtractor(StoryUrlPatterns.FFN.pattern)
            ,new UrlExtractor(StoryUrlPatterns.PC.pattern)
    ]
    static MongoClient client = new MongoClient("localhost")
    static DB db = client.getDB("dlp_library")
    static DBCollection collection = db.getCollection("stories")
    static JacksonDBCollection<Story, String> jacksonDBCollection = JacksonDBCollection.wrap(collection, Story, String)

    public static void main(String[] args) {
        Sql h = Sql.newInstance("jdbc:mysql://localhost/darklord_mainvb", "root", "")
        h.eachRow("""
SELECT
        post.pagetext, post.threadid, thread.title, thread.votenum, thread.votetotal, thread.replycount, thread.lastpost
FROM post
LEFT JOIN thread
        USING (threadid)
LEFT JOIN forum
        USING (forumid)
WHERE thread.sticky = '0'
AND thread.visible = '1'
AND post.parentid = '0'
AND forum.parentid = '2'
AND forum.forumid NOT IN (40, 77, 41)
GROUP BY threadid
ORDER BY post.dateline ASC
    """) {
            insertNewRow(extractStoryInformation(it))
        }
    }

    static void insertNewRow(Story result) {
        jacksonDBCollection.update(result, result, true, false)
    }

    static Story extractStoryInformation(GroovyResultSet resultSet) {
        println "${resultSet.title.replace('&#8216;', '')}"

        String pageText = resultSet.pagetext
        for (DataFilter filter: filters) {
            pageText = filter.filter(pageText)
        }

        Story result = new Story(resultSet.threadId)
        for (DataExtractor extractor : extractors) {
            result = extractor.apply(pageText, result)
        }

        if (!result.title) result.title = resultSet.title.replace('&#8216;', '').replace(' - [a-zA-Z0-9]{1,4}', '')


        println result
        result
    }

}




/*
while ($dlpthread = $vbulletin->db->fetch_array($query)) {
	preg_match('/http:\/\/www\.fanfiction\.net\/s\/([0-9]*)\//i', $dlpthread['pagetext'], $matches);
	$ffnid[$dlpthread['threadid']] =  $matches[1];
        preg_match('/http:\/\/www\.patronuscharm\.net\/s\/([0-9]*)\//i', $dlpthread['pagetext'], $matches);
        $pcid[$dlpthread['threadid']] =  $matches[1];
	$threads[$dlpthread['threadid']] = $dlpthread['title'];
	$other[$dlpthread['threadid']]['replies'] = $dlpthread['replycount'];
	$other[$dlpthread['threadid']]['lastpost'] = $dlpthread['lastpost'];
	if ($dlpthread['votenum'] > 0) {
		$other[$dlpthread['threadid']]['rating'] = $dlpthread['votetotal'] / $dlpthread['votenum'];
	}
}

$vbulletin->db->free_result($query);

foreach ($threads as $threadid => $title) {
	$title = str_replace('&#8216;', '', $title);*/
//	$title = preg_replace('/[^a-z0-9\s]*/i', '', html_entity_decode($title, ENT_QUOTES));
//$tsort[$threadid] = $title;
//}