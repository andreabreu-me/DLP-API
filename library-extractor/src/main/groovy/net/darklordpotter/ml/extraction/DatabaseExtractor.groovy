package net.darklordpotter.ml.extraction

import com.google.common.base.Splitter
import net.darklordpotter.ml.core.Story
import net.darklordpotter.ml.extraction.extractors.UrlExtractor
import net.darklordpotter.ml.extraction.providers.DLPDataProvider
import net.darklordpotter.ml.extraction.sinks.MongoDBSink
import net.darklordpotter.ml.extraction.utils.LevenshteinDistance

/**
 * 2013-02-07
 * @author Michael Rose <elementation@gmail.com>
 */
class DatabaseExtractor {
    static final List<DataFilter> filters = ExtractorConfiguration.filters
    static final List<DataExtractor> extractors = ExtractorConfiguration.extractors


    static Map<String, Integer> cardinality = new HashMap<>().withDefault { k -> 0 }


    protected static void setup() {
        for (StoryUrlPattern urlPattern : StoryUrlPattern.values()) {
            extractors.add(new UrlExtractor(urlPattern))
        }
    }

    static Story extractStoryInformation(Map resultSet) {

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

        result.posted = new Date(resultSet.dateline * 1000) // unix timestamp

        cardinality[result.author]++

        result
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            args = ["localhost", "darklord_mainvb", "root", ""]
        }

        setup()

        DataProvider provider = new DLPDataProvider(args[0], args[1], args[2], args[3])
        DataSink sink = new MongoDBSink("dlp_library", "stories")

        provider.getData().each { m ->
            Story story = extractStoryInformation(m)

            sink.insertStory(story)
        }

        cardinality.sort { it.value }.findAll { it.value > 1 }.each {
            println it
        }

        List<String> names = cardinality.keySet().findAll { LevenshteinDistance.computeDistance(it, "Shezza 88") < 3 }.toList()
        println names.collect {
            cardinality.find { it2->it2.key == it }
        }.sort{it.value}.last()
    }
}