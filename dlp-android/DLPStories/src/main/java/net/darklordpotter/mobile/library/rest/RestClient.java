
package net.darklordpotter.mobile.library.rest;

import com.googlecode.androidannotations.annotations.rest.Get;
import com.googlecode.androidannotations.annotations.rest.Rest;
import net.darklordpotter.mobile.library.api.Story;
import net.darklordpotter.mobile.library.api.StoryList;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.List;

@Rest(rootUrl = "http://api.darklordpotter.net", converters = MappingJackson2HttpMessageConverter.class)
public interface RestClient {


    @Get("/stories")
    public abstract Story[] main();

}
