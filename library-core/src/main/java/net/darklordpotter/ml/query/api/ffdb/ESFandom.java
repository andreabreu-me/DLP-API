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
public class ESFandom {
    int fandomId;
    String name;

    @JsonSetter("fandom_id")
    public void setFandomId(int fandomId) {
      this.fandomId = fandomId;
    }

    @JsonSetter("fandom_name")
    public void setName(String name) {
      this.name = name;
    }

}
