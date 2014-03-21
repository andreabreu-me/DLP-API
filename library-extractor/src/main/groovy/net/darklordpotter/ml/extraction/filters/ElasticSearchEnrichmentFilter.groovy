package net.darklordpotter.ml.extraction.filters

import net.darklordpotter.ml.core.Url
import net.darklordpotter.ml.extraction.DataFilter
import net.darklordpotter.ml.extraction.ExtractionContext

/**
 * 2013-12-30
 * @author Michael Rose
 */
class ElasticSearchEnrichmentFilter implements DataFilter {
    @Override
    ExtractionContext filter(ExtractionContext context) {
        Url ffnUrl = context?.result?.getUrl()?.find { it.type == "ffn" }
        if (ffnUrl) {

        }

        return null
    }
}
