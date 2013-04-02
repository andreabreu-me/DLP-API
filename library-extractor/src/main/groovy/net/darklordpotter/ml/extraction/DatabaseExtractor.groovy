package net.darklordpotter.ml.extraction

import net.darklordpotter.ml.core.Story
import net.darklordpotter.ml.extraction.extractors.UrlExtractor
import net.darklordpotter.ml.extraction.providers.DLPDataProvider
import net.darklordpotter.ml.extraction.sinks.MongoDBSink

import static net.darklordpotter.ml.extraction.utils.TimingUtil.timeIt
/**
 * 2013-02-07
 * @author Michael Rose <elementation@gmail.com>
 */
class DatabaseExtractor {
    static final List<DataFilter> filters = ExtractorConfiguration.filters
    static final List<DataExtractor> extractors = ExtractorConfiguration.extractors

    protected static void setup() {
        for (StoryUrlPattern urlPattern : StoryUrlPattern.values()) {
            extractors.add(new UrlExtractor(urlPattern))
        }
    }

    static Story extractStoryInformation(Map resultSet) {

        println "${resultSet.title.replace('&#8216;', '')}"

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

        setup()

        DataProvider provider = new DLPDataProvider(args[0], args[1], args[2], args[3])
        DataSink sink = new MongoDBSink("dlp_library", "stories")
        //DataSink sink = new PostgresSink()

        List<Story> extractedStories = timeIt("Extraction") {
            provider.getData().collect { m ->
                timeIt("------------") {
                    extractStoryInformation(m)
                }
            }
        }

        timeIt("Write to DB") {
            for (Story s : extractedStories) {
                sink.insertStory(s)
            }
        }
    }
}