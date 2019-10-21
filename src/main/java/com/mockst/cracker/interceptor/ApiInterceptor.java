package com.mockst.cracker.interceptor;

import com.mockst.cracker.result.APIConstant;
import com.mockst.cracker.result.APIResultUtil;
import com.mockst.cracker.util.DateUtils;
import com.mockst.cracker.util.RequestUtil;
import com.mockst.cracker.util.ValidateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@Component
public class ApiInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiInterceptor.class);

    private static final String API_KEY = "SXOQpJpTMyXcwPwXdC0ELIRhSLJYS76p";//通讯key


    @Value("${spring.profiles.active}")
    private String active;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if ("OPTIONS".equals(request.getMethod())){
            return true;
        }

        if ("local".equals(active)){
            return true;
        }
        if ("dev".equals(active)){
            return true;
        }
        String uri = request.getRequestURI();
        String userAgent = request.getHeader("User-Agent");
        LOGGER.info("客户端userAgent:{}", userAgent);
        LOGGER.info("请求uri:{}", uri);
        Map<String, Object> params = RequestUtil.getParamterMap(request);
        String version = (String) params.get("version");//版本号,必填,1.0
        String signType = (String) params.get("signType");//签名方式，目前默认使用MD5，必填
        String signTime = (String) params.get("signTime");//签名时间，格式：yyyyMMddHHmmss  注意：5分钟内有效，必填
        String sign = (String) params.get("sign");//签名字符串，必填
        String format = (String) params.get("format");//仅支持json 必填
        String charset = (String) params.get("charset");//请求使用编码格式目前默认使用utf-8，必填

        if (StringUtils.isBlank(version)) {
            LOGGER.warn("version为空");
            RequestUtil.returnJson(response, APIResultUtil.responseBusinessFailedResult(APIConstant.BUSINESS_PARAMETER_ERROR,"缺少版本号"));
            return false;
        }

        if (StringUtils.isBlank(signType)) {
            LOGGER.warn("signType为空");
            RequestUtil.returnJson(response, APIResultUtil.responseBusinessFailedResult(APIConstant.BUSINESS_PARAMETER_ERROR,"signType为空"));
            return false;
        }

        if (!"md5".equalsIgnoreCase(signType)) {
            LOGGER.warn("signType不为md5 signType:{}", signType);
            RequestUtil.returnJson(response, APIResultUtil.responseBusinessFailedResult(APIConstant.BUSINESS_PARAMETER_ERROR,"signType不为md5"));
            return false;
        }

        if (StringUtils.isNotBlank(charset) && !"utf-8".equalsIgnoreCase(charset)) {
            LOGGER.warn("charset不为utf-8,chatset:{}", charset);
            RequestUtil.returnJson(response, APIResultUtil.responseBusinessFailedResult(APIConstant.BUSINESS_PARAMETER_ERROR,"charset不为utf-8"));
            return false;
        }

        if (StringUtils.isNotBlank(format) && !"json".equalsIgnoreCase(format)) {
            LOGGER.warn("format不为json,format:{}", format);
            RequestUtil.returnJson(response, APIResultUtil.responseBusinessFailedResult(APIConstant.BUSINESS_PARAMETER_ERROR,"format不为json"));
            return false;
        }

        if (StringUtils.isBlank(signTime)) {
            LOGGER.warn("signTime为空");
            RequestUtil.returnJson(response, APIResultUtil.responseBusinessFailedResult(APIConstant.BUSINESS_PARAMETER_ERROR,"signTime为空"));
            return false;
        }

        if (StringUtils.isBlank(sign)) {
            LOGGER.warn("sign为空");
            RequestUtil.returnJson(response, APIResultUtil.responseBusinessFailedResult(APIConstant.BUSINESS_PARAMETER_ERROR,"sign为空"));
            return false;
        }
        try {
            DateUtils.str2Date(signTime, DateUtils.DATETIME_PATTERN_14);
        } catch (Exception e) {
            LOGGER.error("签名时间转换异常，详情:{}", ExceptionUtils.getStackTrace(e));
            RequestUtil.returnJson(response, APIResultUtil.responseBusinessFailedResult(APIConstant.BUSINESS_PARAMETER_ERROR,"签名时间转换异常"));
            return false;
        }

        long signTimeLong = Long.valueOf(signTime);
        String currentDateStr = DateUtils.date2Str(new Date(), DateUtils.DATETIME_PATTERN_14);
        long currentDateLong = Long.valueOf(currentDateStr);
        int diff = 5 * 60;//5分钟

        if (currentDateLong - signTimeLong > diff) {
            LOGGER.warn("signTime 超过5分钟，signTime:{},currentTime:{}", signTime, currentDateStr);
            RequestUtil.returnJson(response, APIResultUtil.responseBusinessFailedResult(APIConstant.BUSINESS_PARAMETER_ERROR,"signTime 超过5分钟"));
            return false;
        }
        //验证签名
        boolean result = ValidateUtils.verifySign(API_KEY, sign, params);
        if (!result) {
            LOGGER.warn("签名验证失败 sign:{}", sign);
            RequestUtil.returnJson(response, APIResultUtil.responseBusinessFailedResult(APIConstant.BUSINESS_PARAMETER_ERROR,"签名验证失败"));
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

}
