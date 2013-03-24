package net.darklordpotter.ml.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;

/**
 * 2013-03-23
 *
 * @author Michael Rose <michael@fullcontact.com>
 */
public class LibraryConfiguration extends Configuration {
    @JsonProperty
    public String mongoDatabaseName = "dlp-library";

    @JsonProperty
    public String mongoDsn = "mongodb://localhost/dlp-library";
}
