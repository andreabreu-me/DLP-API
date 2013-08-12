package net.darklordpotter.ml.query

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper

import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 2013-01-06
 * @author Michael Rose <lordravenclaw@patronuscharm.net>
 */
class BatchFilter implements Filter {
    ObjectMapper objectMapper

    @Override
    void init(FilterConfig filterConfig) throws ServletException { /* unused */ }

    @Override
    void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = servletRequest as HttpServletRequest
        HttpServletResponse res = servletResponse as HttpServletResponse

        if (req.pathInfo == "/batch") {
            objectMapper.readValue(req.inputStream, List).each {

            }
        } else {
            filterChain.doFilter(servletRequest, servletResponse)

        }
    }

    @Override
    void destroy() { /* unused */ }
}
