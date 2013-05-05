package net.darklordpotter.ml.query.resources;

import com.google.common.base.Splitter;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.UncheckedExecutionException;
import net.darklordpotter.ml.query.api.Story;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 2013-02-19
 *
 * @author Michael Rose <elementation@gmail.com>
 */
@Path("/ffn/{storyId}/{chapterId}")
@Produces(MediaType.APPLICATION_JSON)
public class FFNResource {
    private ExecutorService cacheLoader = Executors.newCachedThreadPool();
    private Logger log = LoggerFactory.getLogger(FFNResource.class);
    private final Pattern authorPattern = Pattern.compile("Author: <a [^>]+>([^<]+)");
    private final LoadingCache<List<Long>, Document> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .softValues()
            .build(new CacheLoader<List<Long>, Document>() {
                @Override
                public Document load(final List<Long> key) throws Exception {
                    Connection c = Jsoup.connect("http://www.fanfiction.net/s/" + key.get(0) + "/" + key.get(1) + "/")
                            .userAgent(UA_STRING);

                    try {
                        c.get();

                        if (c.response().statusCode() != 200)
                            throw new WebApplicationException(Response.Status.NO_CONTENT);

                        return c.get();
                    } catch (SocketTimeoutException e) {
                        log.error("Failed to connect to FFN " + e.getMessage(), e);
                        throw new WebApplicationException(504);
                    }

                }

            });

    private static final String UA_STRING = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_2) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1359.0 Safari/537.22";

    @GET
    public Story getStory(@PathParam("storyId") final Long storyId, @PathParam("chapterId") final Long chapterId) {
        Document doc;
        try {
            doc = cache.get(Lists.newArrayList(storyId, chapterId));
        } catch (ExecutionException e) {
            log.error("Error retrieving story {}", storyId);

            throw new WebApplicationException(e);
        }


        String tagString = doc.select("div:nth-child(7)").text();
        Iterable<String> tags = Splitter.on(" - ").omitEmptyStrings().trimResults().split(tagString);

        String author = "";

        Matcher authorMatcher = authorPattern.matcher(doc.html());
        if (authorMatcher.find()) {
            author = authorMatcher.group(1);
        }


        Elements chapters = doc.select("#chap_select").first().select("option");

        if (chapterId < chapters.size()) {
            cacheLoader.submit(new Runnable() {
                public void run() {
                    log.info("Background loading {}", (Arrays.asList(storyId, chapterId + 1)).toString());
                    try {
                        cache.get(Lists.newArrayList(storyId, chapterId + 1));
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }

            });
        }


        return new Story()
                .title(doc.head().getElementsByTag("title").text().split("\\|")[0].trim())
                .author(author)
                .chapters(chapters.size())
                .tags(Lists.newArrayList(tags))
                .summary(doc.select("#gui_table1i tbody tr td div").first().text())
                .storyText(doc.select("#storytext").html());
    }

    public static void main(String[] args) {
        new FFNResource().getStory(2567419l, 19l);
    }
}
