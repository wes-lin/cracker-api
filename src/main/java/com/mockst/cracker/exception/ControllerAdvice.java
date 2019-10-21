package com.mockst.cracker.exception;

import com.mockst.cracker.result.APIConstant;
import com.mockst.cracker.result.APIResult;
import com.mockst.cracker.result.APIResultUtil;
import com.mockst.cracker.util.RequestUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author linzhiwei
 * @Description:异常处理
 * @date 2019/4/15 13:38
 */
@RestControllerAdvice
public class ControllerAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerAdvice.class);

    @ExceptionHandler
    public APIResult handlerException(HttpServletRequest request, Exception e){
        Map<String,Object> paramsMap = RequestUtil.getParamterMap(request);
        LOGGER.info("请求url:{}",request.getRequestURI());
        LOGGER.error("请求参数:{},异常详情:{}",paramsMap.toString(), ExceptionUtils.getStackTrace(e));
        return APIResultUtil.responseBusinessFailedResult(APIConstant.BUSINESS_PROCESSING_FAILED,"系统异常");
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public APIResult requestParameterHandler(HttpServletRequest request,MissingServletRequestParameterException e){
        Map<String,Object> paramsMap = RequestUtil.getParamterMap(request);
        LOGGER.info("请求url:{}",request.getRequestURI());
        LOGGER.error("请求参数:{},异常详情:{}",paramsMap.toString(), ExceptionUtils.getStackTrace(e));
        LOGGER.error("缺少参数：{} 类型：{}",e.getParameterName(),e.getParameterType());
        String code = APIConstant.MISSING_REQUIRED_PARAMETER;
        String message = "缺少必要参数"+e.getParameterName();
        return APIResultUtil.responseBusinessFailedResult(code,message);
    }

    @ExceptionHandler
    public APIResult validationExceptionHandler(ConstraintViolationException exception){
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation s:violations){
            errors.add(s.getMessage());
        }
        return APIResultUtil.responseBusinessFailedResult(APIConstant.BUSINESS_PARAMETER_ERROR,errors.get(0));
    }

    @ExceptionHandler(value = BusinessException.class)
    public APIResult businessExceptionHandler(HttpServletRequest request,BusinessException e){
        Map<String,Object> paramsMap = RequestUtil.getParamterMap(request);
        LOGGER.info("请求url:{}",request.getRequestURI());
        LOGGER.error("请求参数:{},异常详情:{}",paramsMap.toString(),ExceptionUtils.getStackTrace(e));
        String code = e.getCode()==null?APIConstant.BUSINESS_PROCESSING_FAILED:e.getCode();
        return APIResultUtil.responseBusinessFailedResult(code, e.getMessage());
    }
}
