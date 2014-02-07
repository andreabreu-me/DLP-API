package net.darklordpotter.ml.query.api.ffdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import org.elasticsearch.search.SearchHit;
import org.joda.time.DateTime;

import java.util.Map;

/**
 * 2013-12-23
 *
 * @author Michael Rose <michael@fullcontact.com>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class StoryHeader {
    @Getter(AccessLevel.NONE)
    static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        mapper.registerModule(new GuavaModule());
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
    }

    double score;

    long storyId;
    String title;
    String author;
    String authorUrl;
    long authorId;
    String summary;

    String url;
    String urlLatest;

    DateTime published;
    DateTime updated;

    StoryMeta meta;





    public static StoryHeader fromSource(SearchHit hit) {
        StoryHeader header = fromSource(hit.getSource());
        header.setScore(hit.score());

        return header;
    }

    public static StoryHeader fromSource(Map<String,Object> source) {
        StoryHeader header = mapper.convertValue(source, StoryHeader.class);

        return header;
    }
}
