package net.darklordpotter.ml.query

import javax.servlet.*
import javax.servlet.http.HttpServletResponse

/**
 * 2013-01-06
 * @author Michael Rose <elementation@gmail.com>
 */
class CORSFilter implements Filter {

    @Override
    void init(FilterConfig filterConfig) throws ServletException { /* unused */ }

    @Override
    void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse res = servletResponse as HttpServletResponse
        res.addHeader("Access-Control-Allow-Origin", "*")
        res.addHeader("Access-Control-Allow-Credentials", "true")
        res.addHeader("Access-Control-Allow-Headers", "X-Requested-With")

        filterChain.doFilter(servletRequest, servletResponse)
    }

    @Override
    void destroy() { /* unused */ }
}
