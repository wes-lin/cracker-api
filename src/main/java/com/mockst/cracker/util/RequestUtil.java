package com.mockst.cracker.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 获取请求参数工具类
 */
public abstract class RequestUtil {

    public static void returnJson(HttpServletResponse response, Object result) throws Exception{
        String json = JSONObject.toJSONString(result);
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0L);
        writer = response.getWriter();
        writer.print(json);
    }

    public static Map<String, Object> getParamterMap(HttpServletRequest request) {
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        Map<String, String[]> map = request.getParameterMap();
        int len;
        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            len = entry.getValue().length;
            if (len == 1) {
                params.put(entry.getKey(), entry.getValue()[0]);
            } else if (len > 1) {
                String[] vs = new String[entry.getValue().length];
                for (int i=0;i<vs.length;i++){
                    vs[i] = entry.getValue()[i];
                }
                params.put(entry.getKey(), vs);
            }
        }
        return params;
    }

    public static String getEncoderValue(String v){
        String r ;
        try {
            r = URLEncoder.encode(v,"utf-8");
        } catch (UnsupportedEncodingException e) {
            r = v;
        }
        return r;
    }

    /**
     * 获取访问者IP<br>
     * 在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效。<br>
     * 本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)， 如果还不存在则调用Request .getRemoteAddr()。
     *
     * @param request
     * @return
     */
    public static String getClientIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个IP值，第一个为真实IP。
            int index = ip.indexOf(',');
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        } else {
            return request.getRemoteAddr();
        }
    }

    public static String getScheme(HttpServletRequest request) {
        String scheme = request.getScheme();
        return scheme;
    }

    public static String postFormData(String orderPushPath, Map<String,String> stringStringMap) {
        return postData(orderPushPath, stringStringMap, "application/x-www-form-urlencoded", "POST");
    }

    private static String postData(String httpUrl, Map<String,String> paramsMap,String contentType,String method) {
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (method == null || "".equals(method)){
                conn.setRequestMethod("POST");
            }else{
                conn.setRequestMethod(method);
            }


            if (contentType == null || "".equals(contentType)){
                contentType = "application/x-www-form-urlencoded";
            }
            /**
             * conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
             **/
            //conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Content-Type", contentType);

            conn.setConnectTimeout(10 * 1000);
            conn.setReadTimeout(10 * 1000);
            conn.setDoOutput(true);
            // 返回参数
            OutputStream out = conn.getOutputStream();
            StringBuffer paramsSb = new StringBuffer();
            String paramsStr = "";
            if ("application/json".equals(contentType)){
                JSONObject json = (JSONObject) JSONObject.toJSON(paramsMap);
                paramsStr = json.toJSONString();
            }else{
                for(Map.Entry<String, String> entry:paramsMap.entrySet()){
                    paramsSb.append(entry.getKey() + "=" + entry.getValue().toString());
                    paramsSb.append("&");
                }
                paramsStr = paramsSb.toString().substring(0,paramsSb.toString().length()-1);
            }
            System.out.println("请求参数:"+paramsStr);
            out.write(paramsStr.getBytes());

            out.flush();
            int responseCode = conn.getResponseCode();
            InputStream in;
            if (responseCode == 200) {
                in = conn.getInputStream();
            } else {
                in = conn.getErrorStream();
            }
            String responseStr = StreamUtils.copyToString(in, Charset.forName("UTF-8"));
            return responseStr;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
