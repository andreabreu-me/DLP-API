package net.darklordpotter.ml.query

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.spi.LoggerFactoryBinder

import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong

/**
 * 2013-01-06
 * @author Michael Rose <elementation@gmail.com>
 */
class RateLimitingFilter implements Filter {
    private final long requestLimit
    private final long expirationTime

    private LoadingCache<String, AtomicLong> requestLimiter

    RateLimitingFilter(long requestLimit, long expirationTime, TimeUnit unit = TimeUnit.SECONDS) {
        this.requestLimit = requestLimit
        this.expirationTime = expirationTime

        this.requestLimiter = CacheBuilder.newBuilder()
                .expireAfterWrite(expirationTime, unit)
                .build(new CacheLoader<String, AtomicLong>() {
            @Override
            AtomicLong load(String key) throws Exception {
                return new AtomicLong(0)
            }
        })
    }

    @Override
    void init(FilterConfig filterConfig) throws ServletException { /* unused */ }

    @Override
    void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest
        HttpServletResponse response = (HttpServletResponse)servletResponse
        AtomicLong numUserRequests = requestLimiter.get(request.getRemoteAddr())

        if (request.getMethod() != "GET") {
            filterChain.doFilter(servletRequest, servletResponse)
            return
        }

        Long numRequestView = numUserRequests.incrementAndGet()

        log.info  "Client [${request.getRemoteAddr()}] at ${numRequestView} of ${requestLimit} requests"

        if (numRequestView > requestLimit) {
            numUserRequests.set(numRequestView - 1) // cleanup over-limit

            log.info "Limiting client [${request.getRemoteAddr()}]"
            response.sendError(429, "Too many requests.")
        } else {
            response.addHeader("X-Rate-Limit-Remaining", (requestLimit - numRequestView).toString())
            filterChain.doFilter(servletRequest, servletResponse)
        }
    }

    @Override
    void destroy() { /* unused */ }

    private Logger log = LoggerFactory.getLogger(RateLimitingFilter.class)
}
