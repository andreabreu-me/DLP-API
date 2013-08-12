package net.darklordpotter.ml.query;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 2013-01-06
 *
 * @author Michael Rose <lordravenclaw@patronuscharm.net>
 */
public class RateLimitingFilter implements Filter {
    private final long requestLimit;
    private final long expirationTime;
    private LoadingCache<String, AtomicLong> requestLimiter;
    private Logger log = LoggerFactory.getLogger(RateLimitingFilter.class);

    public RateLimitingFilter(long requestLimit, long expirationTime, TimeUnit unit) {
        this.requestLimit = requestLimit;
        this.expirationTime = expirationTime;

        this.requestLimiter = CacheBuilder.newBuilder().expireAfterWrite(expirationTime, unit).build(new CacheLoader<String, AtomicLong>() {
            @Override
            public AtomicLong load(String key) throws Exception {
                return new AtomicLong(0);
            }

        });
    }

    public RateLimitingFilter(long requestLimit, long expirationTime) {
        this(requestLimit, expirationTime, TimeUnit.SECONDS);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {/* unused */}

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        AtomicLong numUserRequests = null;
        try {
            numUserRequests = requestLimiter.get(request.getRemoteAddr());
        } catch (ExecutionException e) {
            throw new IOException(e);
        }

        if (!request.getMethod().equals("GET")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;

        }

        final Long numRequestView = numUserRequests.incrementAndGet();

        log.info("Client [" + request.getRemoteAddr() + "] at " + String.valueOf(numRequestView) + " of " + String.valueOf(requestLimit) + " requests");

        if (numRequestView > requestLimit) {
            numUserRequests.set(numRequestView - 1);// cleanup over-limit

            log.info("Limiting client [" + request.getRemoteAddr() + "]");
            response.sendError(429, "Too many requests.");
        } else {
            response.addHeader("X-Rate-Limit-Remaining", Long.toString(requestLimit - numRequestView));
            filterChain.doFilter(servletRequest, servletResponse);
        }

    }

    @Override
    public void destroy() {/* unused */}
}
