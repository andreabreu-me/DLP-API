package net.darklordpotter.ml.query.api.ffdb;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

import java.util.Map;

/**
 * 2013-12-23
 *
 * @author Michael Rose <michael@fullcontact.com>
 */
@Data
public class Fandom {
    int fandomId;
    String name;

    public static Fandom fromEs(Map<String, Object> source) {
        Fandom c = new Fandom();
        c.setFandomId(Integer.parseInt((String)source.get("_id")));
        c.setName((String) source.get("name"));

        return c;
    }
}
