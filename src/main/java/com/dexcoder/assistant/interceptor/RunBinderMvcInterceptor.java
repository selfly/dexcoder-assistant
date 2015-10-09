package com.dexcoder.assistant.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.NDC;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.dexcoder.assistant.result.RunBinder;

/**
* mvc拦截器
*
* Created by liyd on 4/9/14.
*/
public class RunBinderMvcInterceptor implements HandlerInterceptor {

    public boolean preHandle(HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse, Object o) throws Exception {
        NDC.push(String.valueOf(System.nanoTime()));
        RunBinder.clear();
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o,
                           ModelAndView modelAndView) throws Exception {

        //清理资源，防止OOM
        RunBinder.clear();
        NDC.remove();
    }

    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse, Object o, Exception e)
                                                                                               throws Exception {
    }

}
