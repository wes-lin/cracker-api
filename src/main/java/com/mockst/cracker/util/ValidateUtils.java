/**
 * 文件名[fileName]：ValidateUtils.java
 * 
 * 版本信息[CopyRight]：TzxHr 日期：2014-9-23 Copyright 足下 Corporation 2014 版权[CopyRight]所有
 * 
 */
package com.mockst.cracker.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.Charsets;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.HibernateValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @类描述 数据合法性校验工具类
 */
public class ValidateUtils
{
    private static Logger LOGGER = LoggerFactory.getLogger(ValidateUtils.class);

    /**
     * @功能描述 校验手机号合法性
     * @param mobile 手机号
     * @return 合法返回true，否则返回false
     */
    public static boolean validateMobile(String mobile)
    {
        if (StringUtils.isBlank(mobile))
        {
            return false;
        }
        String regex = "^(1[3-9]{1}\\d{9})$";
        if (mobile.matches(regex))
        {
            return true;
        }
        return false;
    }

    /**
     * 银行卡校验
     * @param bankCardNumber
     * @return
     */
    public static String validateBankCardNumber(String bankCardNumber){
        if (StringUtils.isBlank(bankCardNumber)||bankCardNumber.length()<16||bankCardNumber.length()>19){
            return "银行卡号位数必须在16~19之间，请完整输入银行卡号！";
        }
        if (!isDigit(bankCardNumber)){
            return "银行卡号必须全为数字，请检查后重新输入！";
        }
        //Luhm 校验算法获得校验位
        String nonCheckCodeCardId = bankCardNumber.substring(0, bankCardNumber.length() - 1);
        char bankCardCheckCode = getBankCardCheckCode(nonCheckCodeCardId);
        if (bankCardNumber.charAt(bankCardNumber.length()-1) !=bankCardCheckCode){
            return "银行卡号不合法，请检查后重新输入！";
        }
        return null;
    }


    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId){
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for(int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if(j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char)((10 - luhmSum % 10) + '0');
    }


    /**
     * 判断一个字符串是否是数字。
     *
     * @param string
     * @return
     */
    public static boolean isNumber(String string) {
        if (string == null){
            return false;
        }
        Pattern pattern = Pattern.compile("^-?\\d+(\\.\\d+)?$");
        return pattern.matcher(string).matches();
    }


    /**
     * 校验字符串--(1-32位字符，仅支持纯数字的组合)
     * @param str
     * @return
     */
    public static boolean validateInAcctNo(String str){
        if (StringUtils.isBlank(str))
        {
            return false;
        }
        String regex = "^[0-9]{1,32}$";
        if (str.matches(regex))
        {
            return true;
        }
        return false;
    }


    /**
     * 验证用户名，支持中英文（包括全角字符）、数字、下划线和减号 （全角及汉字算两位）,长度为4-20位,中文按二位计数
     * @param userName
     * @return
     */
    public static boolean validateUserName(String userName) {
        String validateStr = "^[\\w\\-－＿[０-９]\u4e00-\u9fa5\uFF21-\uFF3A\uFF41-\uFF5A]+$";
        boolean rs = false;
        rs = matcher(validateStr, userName);
        if (rs) {
            int strLenth = getStrLength(userName);
            if (strLenth < 4 || strLenth > 20) {
                rs = false;
            }
        }
        return rs;
    }

    /**
     * 获取字符串的长度，对双字符（包括汉字）按两位计数
     *
     * @param value
     * @return
     */
    public static int getStrLength(String value) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        for (int i = 0; i < value.length(); i++) {
            String temp = value.substring(i, i + 1);
            if (temp.matches(chinese)) {
                valueLength += 2;
            } else {
                valueLength += 1;
            }
        }
        return valueLength;
    }

    private static boolean matcher(String reg, String string) {
        boolean tem = false;
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(string);
        tem = matcher.matches();
        return tem;
    }
    
    /**
     * @功能描述 校验邮箱合法性
     * @author 赖文华[lwh] EmailProperty:laiwenhua@xmgz.com Tel:15959396972
     * @param email 邮箱
     * @return 合法返回true，否则返回false
     * @createDate 2014-9-23 下午3:29:19
     */
    public static boolean validateEmail(String email)
    {
        if (StringUtils.isBlank(email))
        {
            return false;
        }
        String regex = "^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$";
        if (email.matches(regex))
        {
            return true;
        }
        return false;
    }
    /**
     * @功能描述 校验密码合法性
     * @param password 密码
     * @return 合法返回true，否则返回false
     */
    public static boolean validatePassword(String password)
    {
        if (StringUtils.isBlank(password))
        {
            return false;
        }
        String reg = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";
        if (password.matches(reg))
        {
            return true;
        }
        return false;
    }
    /**
     * 是符合邮箱格式
     *
     * @param str 待验证的字符串
     * @return 如果是符合邮箱格式的字符串,返回<b>true</b>,否则为<b>false</b>
     */
    public static boolean isEmail(String str) {
        String regex = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        return matcher(regex, str);
    }
    /**
     * 是否都为数字
     *
     * @param str 待验证的字符串
     * @return 如果是符合邮箱格式的字符串,返回<b>true</b>,否则为<b>false</b>
     */
    public static boolean isDigit(String str) {
        String regex = "[0-9]{1,}";
        return matcher(regex, str);
    }
    /**
     * @功能描述 校验身份证号码合法性
     * @author 赖文华[lwx] EmailProperty:laiwenhua@xmgz.com Tel:15959396972
     * @param idcard 身份证号
     * @return 合法则返回null，否则返回错误信息
     * @createDate 2014-7-25 上午10:42:41
     */
    public static String validateIDCard(String idcard)
    {
        if (idcard == null)
        {
            return "身份证号为空！";
        }
        String[] errors = { "身份证号码位数不对!", "身份证号码出生日期超出范围或含有非法字符!", "身份证号码校验错误!", "身份证地区非法!" };
        String[] valCodeArr = { "1", "0", "x", "9", "8", "7", "6", "5", "4", "3", "2" };
        String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2" };
        String Ai = null;
        // ================ 号码的长度 15位或18位 ================
        if (idcard.length() != 15 && idcard.length() != 18)
        {
            LOGGER.info(errors[0]);
            return errors[0];
        }
        // ================ 数字 除最后以为都为数字 ================
        if (idcard.length() == 18)
        {
            Ai = idcard.substring(0, 17);
        }
        else if (idcard.length() == 15)
        {
            Ai = idcard.substring(0, 6) + "19" + idcard.substring(6, 15);
        }
        if (null == Ai || !StringUtils.isNumeric(Ai))
        {
            LOGGER.info(errors[1]);
            return errors[1];
        }
        int birthday;
        String ereg;
        // ================ 号码长度为15位 ================
        if (15 == idcard.length())
        {
            birthday = Integer.parseInt(idcard.substring(6, 8));
            if ((birthday + 1900) % 4 == 0 || ((birthday + 1900) % 100 == 0 && (birthday + 1900) % 4 == 0))
            {
                // 测试出生日期的合法性
                ereg = "^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$";
            }
            else
            {
                // 测试出生日期的合法性
                ereg = "^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$";
            }
            if (idcard.matches(ereg))
            {
                // 验证通过!
                return null;
            }
            else
            {
                LOGGER.info(errors[1]);
                return errors[1];
            }
        }
        // ================ 号码长度为18位 ================
        // ================ 地区码时候有效 ================
        Map<String, String> local = getAreaCode();
        if (local.get(Ai.substring(0, 2)) == null)
        {
            LOGGER.info(errors[3]);
            return errors[3];
        }
        // ================ 出生年月是否有效 ================
        birthday = Integer.parseInt(idcard.substring(6, 10));
        if (birthday % 4 == 0 || (birthday % 100 == 0 && birthday % 4 == 0))
        {
            // 闰年出生日期的合法性正则表达式
            ereg = "^[1-9][0-9]{5}[0-9]{2}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$";
        }
        else
        {
            // 平年出生日期的合法性正则表达式
            ereg = "^[1-9][0-9]{5}[0-9]{2}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$";
        }
        if (!idcard.matches(ereg))
        {
            LOGGER.info(errors[1]);
            return errors[1];
        }
        // ================ 判断最后一位的值 ================
        int totalmulAiWi = 0;
        for (int i = 0; i < 17; i++)
        {
            totalmulAiWi = totalmulAiWi + Integer.parseInt(String.valueOf(Ai.charAt(i))) * Integer.parseInt(Wi[i]);
        }
        int modValue = totalmulAiWi % 11;
        String checkStr = idcard.substring(17, 18);
        if (!checkStr.equalsIgnoreCase(valCodeArr[modValue]))
        {
            LOGGER.info(errors[2]);
            return errors[2];
        }
        // 验证通过!
        return null;
    }

    /**
     * @功能描述 获取地区编码键值对
     * @author 赖文华[lwx] EmailProperty:laiwenhua@xmgz.com Tel:15959396972
     * @return 地区编码键值对
     * @createDate 2014-7-25 上午10:41:26
     */
    private static Map<String, String> getAreaCode()
    {
        Map<String, String> hashtable = new HashMap<String, String>();
        hashtable.put("11", "北京");
        hashtable.put("12", "天津");
        hashtable.put("13", "河北");
        hashtable.put("14", "山西");
        hashtable.put("15", "内蒙古");
        hashtable.put("21", "辽宁");
        hashtable.put("22", "吉林");
        hashtable.put("23", "黑龙江");
        hashtable.put("31", "上海");
        hashtable.put("32", "江苏");
        hashtable.put("33", "浙江");
        hashtable.put("34", "安徽");
        hashtable.put("35", "福建");
        hashtable.put("36", "江西");
        hashtable.put("37", "山东");
        hashtable.put("41", "河南");
        hashtable.put("42", "湖北");
        hashtable.put("43", "湖南");
        hashtable.put("44", "广东");
        hashtable.put("45", "广西");
        hashtable.put("46", "海南");
        hashtable.put("50", "重庆");
        hashtable.put("51", "四川");
        hashtable.put("52", "贵州");
        hashtable.put("53", "云南");
        hashtable.put("54", "西藏");
        hashtable.put("61", "陕西");
        hashtable.put("62", "甘肃");
        hashtable.put("63", "青海");
        hashtable.put("64", "宁夏");
        hashtable.put("65", "新疆");
        hashtable.put("71", "台湾");
        hashtable.put("81", "香港");
        hashtable.put("82", "澳门");
        hashtable.put("91", "国外");
        return hashtable;
    }

    /**
     * @功能描述 检查是否包含Emoji表情
     * @author 赖文华[lwh] EmailProperty:laiwenhua@xmgz.com Tel:15959396972
     * @param text 待检查文本
     * @return 包含Emoji表情则返回true，否则返回false
     * @createDate 2014-11-25 上午8:42:05
     */
    public static boolean containsEmoji(String text)
    {
        if (StringUtils.isBlank(text))
        {
            return false;
        }
        int len = text.length();
        for (int i = 0; i < len; i++)
        {
            if (isEmojiChar(text.charAt(i)))
            {
                return true;
            }
        }
        return false;
    }

    private static boolean isEmojiChar(char c)
    {
        return (c == 0x0) || (c == 0x9) || (c == 0xA) || (c == 0xD) || ((c >= 0x20) && (c <= 0xD7FF))
                || ((c >= 0xE000) && (c <= 0xFFFD)) || ((c >= 0x10000) && (c <= 0x10FFFF));
    }

    /**
     * 验证签名串是否匹配
     * @param key
     * @param sign
     * @param params
     * @return
     */
    public static boolean verifySign(String key, String sign,Map<String, Object> params){
        String s = sign(params,key);
        LOGGER.info("生成的签名字符串：{}", s);
        LOGGER.info("sign:{}",sign);
        if (!StringUtils.equalsIgnoreCase(s, sign)){
            return false;
        }
        return true;
    }

    /**
     * 计算签名
     * @param params 请求参数
     * @param key 秘钥
     * @return 签名字符串（32位MD5）
     */
    public static String sign(Map<String, Object> params, String key) {
        String text = createLinkString(params);
        //暂时判断有包含base64 文件上传时不打印日志
        if (params.containsKey("fileData")){
            LOGGER.info("有上传base64 图片不打印日志");
        }else {
            LOGGER.info("待签名字符串（未拼接key）：{}", text);
        }

        String toSignStr = text + key;
        return DigestUtils.md5Hex(toSignStr.getBytes(Charsets.UTF_8));
    }

    /**
     * 把所有Key排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, Object> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        // 排序
        Collections.sort(keys);
        StringBuilder prestr = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            // 过滤sign参数
            if (key.equals("sign")) {
                continue;
            }
            String value;
            if (null == params.get(key)) {
                value = StringUtils.EMPTY;
            } else {
                value = params.get(key).toString();
            }
//            if (i == keys.size() - 1) {
//                // 拼接时，不包括最后一个&字符
//                prestr.append(key).append("=").append(value);
//            } else {
//                prestr.append(key).append("=").append(value).append("&");
//            }
            prestr.append(key).append("=").append(value).append("&");
        }
        return prestr.deleteCharAt(prestr.length() - 1).toString();
    }

    private static Validator validator;

    static {
        ValidatorFactory validatorFactory = Validation.byProvider( HibernateValidator.class )
                .configure()
                .failFast( true )
                .buildValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public static Validator validator(){
        return validator;
    }

    public static List<String> validateObject(Object object){
        Set<ConstraintViolation<Object>> ss = validator.validate(object);
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation s:ss){
            errors.add(s.getMessage());
        }
        return errors;
    }
    
    public static void main(String[] args) {
        String a = "aaaaaRegister";
        String b = "Register";
        a.contains(b);
        a.matches(b);
        if (a.contains(b)){
            System.out.println("包含");
        }else {
            System.out.println("不包含");
        }
        
    }
}
