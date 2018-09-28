package com.genie.core.utils.mdc;

import com.genie.core.config.BaseConstants;
import com.genie.core.utils.TraceLogUtils;
import org.slf4j.MDC;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TraceInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // "traceId"
        MDC.put(BaseConstants.LOG_TRACE_ID, TraceLogUtils.getTraceId());
        return true;
    }
}
