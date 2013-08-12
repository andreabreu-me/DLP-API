package net.darklordpotter.ml.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.cache.CacheBuilderSpec;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.db.DatabaseConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 2013-03-23
 *
 * @author Michael Rose <lordravenclaw@patronuscharm.net>
 */
public class LibraryConfiguration extends Configuration {
    @JsonProperty
    public String mongoDatabaseName = "dlp_library";

    @JsonProperty
    public String mongoHost = "localhost";

    @JsonProperty
    @Min(1)
    @Max(65535)
    public Integer mongoPort = 27017;

    @Valid
    @NotNull
    @JsonProperty
    private DatabaseConfiguration database = new DatabaseConfiguration();

    @JsonProperty
    public CacheBuilderSpec authenticationCachePolicy;

    public CacheBuilderSpec getAuthenticationCachePolicy() {
        return authenticationCachePolicy;
    }

    public DatabaseConfiguration getDatabaseConfiguration() {
        return database;
    }
}
