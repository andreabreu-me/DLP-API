package net.darklordpotter.ml.extraction

import com.netflix.hystrix.HystrixCommand
import com.netflix.hystrix.HystrixCommandGroupKey
import net.darklordpotter.ml.core.Rating
import net.darklordpotter.ml.core.Story
import net.darklordpotter.ml.core.Url
import net.darklordpotter.ml.extraction.extractors.UrlExtractor
import net.darklordpotter.ml.extraction.providers.DLPDataProvider
import net.darklordpotter.ml.extraction.sinks.MongoDBSink
import net.darklordpotter.ml.query.api.ffdb.StoryHeader
import org.elasticsearch.action.get.GetResponse
import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.common.transport.TransportAddress
import rx.Observable
import rx.Observer;
import rx.Subscription
import rx.concurrency.Schedulers;
import rx.util.functions.Func1;
import rx.Observable

import java.util.concurrent.CountDownLatch
import java.util.regex.Matcher
import java.util.regex.Pattern

import static net.darklordpotter.ml.extraction.utils.TimingUtil.timeIt
/**
 * 2013-02-07
 * @author Michael Rose <elementation@gmail.com>
 */
class DatabaseExtractor {
    static final List<DataFilter> filters = ExtractorConfiguration.filters
    static final List<DataExtractor> extractors = ExtractorConfiguration.extractors
    static final List<DataExtractor> postFilters = ExtractorConfiguration.postFilters

    protected static void setup() {
        for (StoryUrlPattern urlPattern : StoryUrlPattern.values()) {
            extractors.add(new UrlExtractor(urlPattern))
        }
    }

    static Story extractStoryInformation(Map resultSet) {
//        println(resultSet.pageText)

        ExtractionContext context = new ExtractionContext(resultSet)

        for (DataFilter filter: filters) {
            if (context) {
                context = filter.filter(context)
            }
        }

        for (DataExtractor extractor : extractors) {
            if (context.pageText) {
                extractor.apply(context)
            }
        }

        println "(${resultSet.threadId}) ${context.result.title} by ${context.result.author}"

        context.result
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            args = ["localhost", "darklord_mainvb", "root", ""]
        }

        println args

        setup()

        DataProvider provider = new DLPDataProvider(args[0], args[1], args[2], args[3])
        DataSink sink = new MongoDBSink("dlp_library", "stories")
        //DataSink sink = new PostgresSink()
        //DataSink sink = new TinkerPopSink()

        CountDownLatch latch = new CountDownLatch(1)

        Observable.from(provider.getData())
        .map({ m ->
//            println "Extracting ${m}"
            extractStoryInformation(m)
        })
        .mapMany({ s ->
            enrichWithElasticSearch(s)
        })
//        .subscribeOn(Schedulers.threadPoolForIO())
        .subscribe({ s ->
            println "Inserting..."
            sink.insertStory(s) }, { t -> t.printStackTrace()}, { latch.countDown() })


        latch.await()
//        List<Story> extractedStories = timeIt("Extraction") {
//            provider.getData().collect { m ->
//                timeIt("------------") {
//                    extractStoryInformation(m)
//                }
//            }
//        }
//
//        timeIt("Write to DB") {
//            for (Story s : extractedStories) {
//                sink.insertStory(s)
//            }
//        }
    }

    static Pattern pattern = Pattern.compile(".*s/(\\d+)/.*")

    static Observable<Story> enrichWithElasticSearch(final Story story) {
        Url ffnUrl = story.getUrl().find({it.type == "ffn"})

        if (ffnUrl) {
            Matcher m = pattern.matcher(ffnUrl.url)
            m.find()

            long idFromUrl = Long.parseLong(m.group(1))

            println "$ffnUrl - $idFromUrl"

            Observable<StoryHeader> headerObservable = Observable
                    .from(esClient.prepareGet("ffn_index", "story", idFromUrl.toString()).execute(), Schedulers.threadPoolForIO())
                    .map({ GetResponse r ->
                        if (r == null) return null;

                        return StoryHeader.fromSource((Map)r.getSource())
                    })

            return headerObservable.map({ StoryHeader s ->
                println "Enriching: ${s}"

                if (s) {
                    story.title = s.title
                    story.author = s.author
                    story.summary = s.summary
                    story.rating = Rating.valueOf(Rating.class, s.meta.rated.replace("K+", "KPLUS"))

                    if (s.meta.favs > 100)
                        story.tags << ">100 favorites"
                    if (s.meta.follows > 1000)
                        story.tags << ">1000 follows"

                    story.tags << "enriched"
                }

                return story
            })
        }

        return Observable.from(story)
    }

    static Client getElasticSearchClient(List<String> hostnames, int port) {
        def transportAddresses = new TransportAddress[hostnames.size()];
        for (int i = 0; i < hostnames.size(); i++) {
            transportAddresses[i] = new InetSocketTransportAddress(hostnames.get(i), port);
        }

        return new TransportClient().addTransportAddresses(transportAddresses);
    }

    static Client esClient = getElasticSearchClient(["localhost"], 9300)

    static class ElasticSearchCommand extends HystrixCommand<StoryHeader> {
        long id

        ElasticSearchCommand(Long id) {
            super(HystrixCommandGroupKey.Factory.asKey("ElasticSearch"))

            this.id = id;
        }

        @Override
        protected StoryHeader run() throws Exception {
            GetResponse response = esClient.prepareGet("ffn_index", "story", id.toString()).get()

            if (response.exists) {
                return StoryHeader.fromSource(response.getSource())
            }

            return null;
        }
    }
}