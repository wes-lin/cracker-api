package com.mockst.cracker.resolver;

import com.alibaba.fastjson.JSONObject;
import com.mockst.cracker.annotate.JsonObjectParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Array;
import java.util.List;

/**
 * @author linzhiwei
 * @Description:json转换处理
 * @date 2019/4/17 16:54
 */
public class JsonObjectResolver implements HandlerMethodArgumentResolver{

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(JsonObjectParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        JsonObjectParam annotation = methodParameter.getParameterAnnotation(JsonObjectParam.class);
        String parameterName = annotation.name();
        if (StringUtils.isBlank(parameterName)){
            parameterName = methodParameter.getParameterName();
        }
        Class clazz = methodParameter.getParameterType();
        String json = nativeWebRequest.getParameter(parameterName);
        if (json==null){
            return null;
        }
        if (clazz.isArray()){
            List list = JSONObject.parseArray(json,clazz.getComponentType());
            if (list==null){
                return null;
            }
            Object objects = Array.newInstance(clazz.getComponentType(),list.size());
            for (int i=0;i<list.size();i++){
                Array.set(objects,i,list.get(i));
            }
            return objects;
        }else {
            return JSONObject.parseObject(json,clazz);
        }
    }

}
