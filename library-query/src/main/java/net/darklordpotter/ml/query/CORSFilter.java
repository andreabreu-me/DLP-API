package net.darklordpotter.ml.query;

import org.codehaus.groovy.runtime.DefaultGroovyMethods;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 2013-01-06
 *
 * @author Michael Rose <lordravenclaw@patronuscharm.net>
 */
public class CORSFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {/* unused */}

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse res = DefaultGroovyMethods.asType(servletResponse, HttpServletResponse.class);
        res.addHeader("Access-Control-Allow-Origin", "*");
        res.addHeader("Access-Control-Allow-Methods", "GET,POST");
        res.addHeader("Access-Control-Allow-Credentials", "true");
        res.addHeader("Access-Control-Allow-Headers", "X-Requested-With,Content-Type");
        res.addHeader("Access-Control-Max-Age", "1800");

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {/* unused */}

}
