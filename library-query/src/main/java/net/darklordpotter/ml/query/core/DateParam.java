package net.darklordpotter.ml.query.core;

import com.yammer.dropwizard.jersey.params.AbstractParam;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import javax.ws.rs.WebApplicationException;

public class DateParam extends AbstractParam<DateTime> {
    public static final DateTimeFormatter ISO_BASIC = ISODateTimeFormat.dateOptionalTimeParser();

    public DateParam(String param) throws WebApplicationException {
        super(param);
    }

    @Override
    protected DateTime parse(String param) {
        return ISO_BASIC.parseDateTime(param);
    }
}