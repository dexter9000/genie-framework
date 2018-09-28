package com.genie.security.filter;

import com.genie.security.util.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(filterName="securityFilter",urlPatterns="/*")
public class SecurityFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(SecurityFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("Init SecurityFilter");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        SecurityUtils.setCurrentUserLogin(((HttpServletRequest) request).getHeader("Authorization"));
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
