package net.darklordpotter.ml.query.resources

import com.google.common.base.Splitter
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import com.google.common.cache.Weigher
import com.google.common.util.concurrent.UncheckedExecutionException
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import org.slf4j.Logger
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.core.StreamingOutput;
import java.util.Map
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.regex.Matcher
import java.util.regex.Pattern;

/**
 * 2013-02-19
 *
 * @author Michael Rose <michael@fullcontact.com>
 */
@Path("/ffn/{storyId}/{chapterId}")
@Produces(MediaType.APPLICATION_JSON)
public class FFNResource {
    private final Pattern authorPattern = Pattern.compile("Author: <a [^>]+>([^<]+)")
    private final LoadingCache<List<Long>, Document> cache = CacheBuilder.newBuilder()
        .expireAfterWrite(30, TimeUnit.MINUTES)
        .softValues()
        .build(new CacheLoader<List<Long>, Document>() {
        @Override
        Document load(List<Long> key) throws Exception {
            Connection c = Jsoup.connect("http://www.fanfiction.net/s/${key[0]}/${key[1]}/")
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_2) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1359.0 Safari/537.22")

            try {
                c.get()


                println c.response().statusCode()
                if (c.response().statusCode() != 200) throw new WebApplicationException(Response.Status.NO_CONTENT)

                return c.get()
            } catch (SocketTimeoutException e) {
                log.error "Failed to connect to FFN ${e.getMessage()}", e
                throw new WebApplicationException(504)
            }
        }
    })

    ExecutorService cacheLoader = Executors.newCachedThreadPool()

    @GET
    public Map getStory(
            @PathParam("storyId") Long storyId,
            @PathParam("chapterId") Long chapterId) {

        Document doc
        try {
            doc = cache.get([storyId, chapterId])
        } catch (UncheckedExecutionException e) {
            throw e.cause
        }

        String tagString = doc.select("div:nth-child(7)").text()
        Iterable<String> tags = Splitter.on(' - ')
                .omitEmptyStrings()
                .trimResults()
                .split(tagString)

        String author = ""
        println doc.select("#gui_table1i tbody tr td div").first().text()
        Matcher authorMatcher = authorPattern.matcher(doc.html())
        if (authorMatcher.find()) {
            author = authorMatcher.group(1)
        }

        Elements chapters = doc.select("#chap_select").first().select("option")

        if (chapterId < chapters.size()) {
            cacheLoader.submit({ ->
                println "Background loading ${[storyId, chapterId+1]}"
                cache.get([storyId, chapterId+1])
            })
        }

        [
                title: doc.head().getElementsByTag("title").text().split("\\|")[0].trim(),
                author: author,
                chapters: chapters.size(),
                tags: tags,
                summary: doc.select("#gui_table1i tbody tr td div").first().text(),
                storyText: doc.select("#storytext").html()
        ]

    }

    public static void main(String[] args) {
        new FFNResource().getStory(2567419, 19)
    }

    private Logger log = LoggerFactory.getLogger(FFNResource.class)
}
