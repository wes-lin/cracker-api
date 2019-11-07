package com.mockst.cracker.interceptor;

import com.mockst.cracker.util.NetUtils;
import com.mockst.cracker.util.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 日志MDC拦截器
 */
@Component
public class ThreadContextMDCInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadContextMDCInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        MDC.put("traceId", UUID.randomUUID().toString().replaceAll("-", ""));//日志跟踪id
        MDC.put("accessStartTime", System.currentTimeMillis() + "");//访问时间

        String uri = request.getRequestURI();//请求uri
        Integer serverPort = request.getLocalPort();    //取得服务器端口
        if (serverPort == null) {
            serverPort = 0;
        }

        String serverIp = NetUtils.getLocalHost();
        String clientIp = RequestUtil.getClientIpAddr(request);

        LOGGER.info("serverIp:{},serverPort:{},clientIp:{}", serverIp, serverPort, clientIp);

        LOGGER.info("请求uri:{}", uri);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        long accessEndTime = System.currentTimeMillis();//当前时间
        String uri = request.getRequestURI();
        String accessStartTimeStr = MDC.get("accessStartTime");
        String traceId = MDC.get("traceId");
        if (accessStartTimeStr != null && !"".equalsIgnoreCase(accessStartTimeStr)) {
            long accessStartTime = Long.valueOf(accessStartTimeStr);
            //执行uri多少毫秒
            LOGGER.info("run traceId:{} uri:{}, {} msec finished", traceId, uri, (accessEndTime - accessStartTime));
        }
        //清除
        MDC.clear();

    }
}
