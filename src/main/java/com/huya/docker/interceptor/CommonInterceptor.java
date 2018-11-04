package com.huya.docker.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

public class CommonInterceptor implements HandlerInterceptor {
    private static Logger logger = LoggerFactory.getLogger(CommonInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        logger.info("******************************************* request log info start *************************************************");
        logger.info("http servlet request uri is {}", httpServletRequest.getRequestURI());
        logger.info("----------header info-----------");
        Enumeration<String> headerEnumeration = httpServletRequest.getHeaderNames();
        while(headerEnumeration.hasMoreElements()){
            String headName = headerEnumeration.nextElement();
            String headValue = httpServletRequest.getHeader(headName);
            logger.info(headName + ":" + headValue);
        }
        logger.info("----------header info-----------");
        logger.info("http servlet request query string is {}", httpServletRequest.getQueryString());
        logger.info("----------parameter info-----------");
        Enumeration<String> parameterEnumeration = httpServletRequest.getParameterNames();
        while(parameterEnumeration.hasMoreElements()){
            String parameterName = parameterEnumeration.nextElement();
            String parameterValue = httpServletRequest.getParameter(parameterName);
            logger.info(parameterName + ":" + parameterValue);
        }
        logger.info("----------parameter info-----------");
        logger.info("----------attribute info-----------");
        Enumeration<String> attributeEnumeration = httpServletRequest.getAttributeNames();
        while(attributeEnumeration.hasMoreElements()){
            String attributeName = attributeEnumeration.nextElement();
            Object attributeValue = httpServletRequest.getAttribute(attributeName);
            logger.info(attributeName + ":" + attributeValue.toString());
        }
        logger.info("----------attribute info-----------");
        logger.info("******************************************* request log info end *************************************************");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
