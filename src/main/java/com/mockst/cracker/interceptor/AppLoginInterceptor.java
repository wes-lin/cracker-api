package com.mockst.cracker.interceptor;

import com.mockst.cracker.annotate.LoginIgnore;
import com.mockst.cracker.result.APIConstant;
import com.mockst.cracker.result.APIResultUtil;
import com.mockst.cracker.util.RedisKeyConstants;
import com.mockst.cracker.util.RedisUtil;
import com.mockst.cracker.util.RequestUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;

/**
 * @author linzhiwei
 * @Description: 后台登录接口拦截
 * @date 2019/4/10 15:38
 */
@Component
public class AppLoginInterceptor implements HandlerInterceptor {

    private final static Logger LOGGER = LoggerFactory.getLogger(AppLoginInterceptor.class);

    @Autowired
    protected RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getParameter("token");
        if(handler instanceof HandlerMethod){
            HandlerMethod method = (HandlerMethod) handler;
            Annotation annotation = method.getMethodAnnotation(LoginIgnore.class);
            //有注解
            if(annotation!=null){
                return true;
            }
        }
//        if ("OPTIONS".equals(request.getMethod())){
//            return true;
//        }
        if (StringUtils.isBlank(token)) {
            LOGGER.warn("缺少参数token");
            RequestUtil.returnJson(response, APIResultUtil.responseBusinessFailedResult(APIConstant.MISSING_REQUIRED_PARAMETER,"缺少token"));
            return false;
        }
        String key = RedisKeyConstants.getRedisKey(RedisKeyConstants.USER_SESSION_KEY, token);
        String userId = (String) redisUtil.get(key);
        if (null == userId) {
            LOGGER.warn("登录信息过期");
            RequestUtil.returnJson(response,APIResultUtil.responseBusinessFailedResult(APIConstant.SESSION_FAILED,"登录信息过期"));
            return false;
        }
        redisUtil.set(key,userId,7200);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
