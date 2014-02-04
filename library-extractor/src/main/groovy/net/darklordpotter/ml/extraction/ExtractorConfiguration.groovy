package net.darklordpotter.ml.extraction

import net.darklordpotter.ml.extraction.extractors.AuthorExtractor
import net.darklordpotter.ml.extraction.extractors.DateExtractor
import net.darklordpotter.ml.extraction.extractors.RatingExtractor
import net.darklordpotter.ml.extraction.extractors.SummaryExtractor
import net.darklordpotter.ml.extraction.extractors.TagExtractor
import net.darklordpotter.ml.extraction.extractors.ThreadMetadataExtractor
import net.darklordpotter.ml.extraction.extractors.TitleExtractor
import net.darklordpotter.ml.extraction.filters.BBTextDataFilter
import net.darklordpotter.ml.extraction.filters.ElasticSearchEnrichmentFilter
import net.darklordpotter.ml.extraction.filters.QuoteDataFilter

/**
 * 2013-02-10
 * @author Michael Rose <elementation@gmail.com>
 */
class ExtractorConfiguration {
    public static List<DataFilter> getFilters() {
        [new BBTextDataFilter(),new QuoteDataFilter()]
    }

    public static List<DataExtractor> getExtractors() {
        [
                new TitleExtractor()
                ,new AuthorExtractor()
                ,new RatingExtractor()
                ,new SummaryExtractor()
                ,new ThreadMetadataExtractor()
                ,new TagExtractor()
                ,new DateExtractor()
        ]
    }

    public static List<DataFilter> getPostFilters() {
        [new ElasticSearchEnrichmentFilter()]
    }
}
