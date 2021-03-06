package net.darklordpotter.ml.query.api.ffdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * 2013-12-23
 *
 * @author Michael Rose <michael@fullcontact.com>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class StoryMeta {
    String status;
    String language;
    String rated;
    long ratings;
    long chapters;
    long favs;
    long follows;
    long reviews;
    long words;
    List<Category> categories = Collections.emptyList();
    List<List<Character>> relationships = Collections.emptyList();
    List<Character> characters;

    ThreadData threadData;
}
