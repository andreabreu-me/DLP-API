package net.darklordpotter.ml.extraction

import com.google.common.base.Splitter
import com.mongodb.BasicDBObject
import net.darklordpotter.ml.core.Story
import net.darklordpotter.ml.extraction.extractors.AuthorExtractor
import net.darklordpotter.ml.extraction.extractors.RatingExtractor
import net.darklordpotter.ml.extraction.extractors.SummaryExtractor
import net.darklordpotter.ml.extraction.extractors.TitleExtractor
import net.darklordpotter.ml.extraction.extractors.UrlExtractor
import net.darklordpotter.ml.extraction.filters.BBTextDataFilter
import com.mongodb.DB
import com.mongodb.DBCollection
import com.mongodb.MongoClient
import groovy.sql.GroovyResultSet
import groovy.sql.Sql
import net.darklordpotter.ml.extraction.filters.QuoteDataFilter
import net.darklordpotter.ml.extraction.utils.LevenshteinDistance
import net.vz.mongodb.jackson.JacksonDBCollection

/**
 * 2013-02-07
 * @author Michael Rose <michael@fullcontact.com>
 */
class DatabaseExtractor {
    static final List<DataFilter> filters = [new BBTextDataFilter(),new QuoteDataFilter()]
    static final List<DataExtractor> extractors = [
             new TitleExtractor()
            ,new AuthorExtractor()
            ,new RatingExtractor()
            ,new SummaryExtractor()
    ]
    static MongoClient client = new MongoClient("localhost")
    static DB db = client.getDB("dlp_library")
    static DBCollection collection = db.getCollection("stories")
    static JacksonDBCollection<Story, String> jacksonDBCollection = JacksonDBCollection.wrap(collection, Story, String)
    static Map<String, Integer> cardinality = new HashMap<>().withDefault { k -> 0 }

    public static void main(String[] args) {
        for (StoryUrlPattern urlPattern : StoryUrlPattern.values()) {
            extractors.add(new UrlExtractor(urlPattern))
        }

        Sql h = Sql.newInstance("jdbc:mysql://localhost/darklord_mainvb", "root", "")
        h.eachRow("""
SELECT
        post.pagetext, post.threadid, thread.title, thread.votenum, thread.votetotal, thread.replycount, thread.lastpost, forum.title as forumname,
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
    """) {
            insertNewRow(extractStoryInformation(it))
        }

        cardinality.sort { it.value }.findAll { it.value > 1 }.each {
            println it
        }

        List<String> names = cardinality.keySet().findAll { LevenshteinDistance.computeDistance(it, "Shezza 88") < 3 }.toList()
        println names.collect {
            cardinality.find { it2->it2.key == it }
        }.sort{it.value}.last()
    }

    static void insertNewRow(Story result) {
        jacksonDBCollection.update(new Story(result.getThreadId()), result, true, false)
        //collection.aggregate(new BasicDBObject("$match"))
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

        if (!result.title) result.title = resultSet.title.replace('&#8216;', '').replace(' - [a-zA-Z0-9]{1,4}', '').replaceAll("â€™", "'")

        result.threadRating = (double)resultSet.votetotal / resultSet.votenum

        if (resultSet.tags) {
            List<String> tags = Splitter.on(",").trimResults().split(resultSet.tags) as List<String>

            result.tags.addAll(tags)
        }
//        result.tags.addAll(
//
//        )

        cardinality[result.author]++
        //println result
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