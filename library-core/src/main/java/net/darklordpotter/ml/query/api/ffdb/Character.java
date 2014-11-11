package net.darklordpotter.ml.query.api.ffdb;

import lombok.Data;

import java.util.Map;

/**
 * 2013-12-23
 *
 * @author Michael Rose <michael@fullcontact.com>
 */
@Data
public class Character {
    int characterId;
    int fandomId;
    String name;

    public static Character fromEs(Map<String, Object> source) {
        Character c = new Character();
        c.setCharacterId(Integer.parseInt((String)source.get("_id")));
        c.setFandomId((Integer)source.get("fandom_id"));
        c.setName((String) source.get("name"));

        return c;
    }
}
