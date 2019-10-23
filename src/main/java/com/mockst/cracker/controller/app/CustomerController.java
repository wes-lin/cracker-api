package com.mockst.cracker.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.mockst.cracker.annotate.Phone;
import com.mockst.cracker.config.WebConfigurer;
import com.mockst.cracker.entity.CustomerEntity;
import com.mockst.cracker.entity.VerificationCodeEntity;
import com.mockst.cracker.repository.CustomerRepository;
import com.mockst.cracker.result.APIResult;
import com.mockst.cracker.result.APIResultUtil;
import com.mockst.cracker.service.CustomerService;
import com.mockst.cracker.service.VerificationCodeService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @Auther: zhiwei
 * @Date: 2019/10/20 16:17
 * @Description:
 */
@RestController
@RequestMapping(WebConfigurer.APP_BASE_PATH+"/customer")
public class CustomerController extends AbstractController {

    @Autowired
    private VerificationCodeService verificationCodeService;
    @Autowired
    private CustomerService customerService;

    /**
     * 注册用户
     * @return
     */
    @RequestMapping("register")
    public APIResult register(@Phone String phone,
                               @RequestParam String password,
                               @RequestParam String smsCode){
        CustomerEntity customerEntity = customerService.findByPhone(phone);
        if (customerEntity!=null){
            return APIResultUtil.responseBusinessFailedResult("该手机号已被使用");
        }
        VerificationCodeEntity verificationCodeEntity = verificationCodeService.checkSmsCode(phone, VerificationCodeEntity.CodeType.register.name(),smsCode);
        if(verificationCodeEntity==null){
            return APIResultUtil.responseBusinessFailedResult("验证码无效");
        }
        verificationCodeEntity.setCodeStatus(VerificationCodeEntity.CodeStatus.expire);
        verificationCodeService.save(verificationCodeEntity);
        customerEntity = customerService.register(phone,password);
        String token = UUID.randomUUID().toString().replace("-","");
        setCurrentUser(token,customerEntity);
        //返回登录信息
        JSONObject data = new JSONObject();
        data.put("token",token);
        data.put("customerId",customerEntity.getId());
        data.put("phone",phone);
        return APIResultUtil.returnSuccessResult(data);
    }

    /**
     * 登录
     * @param phone
     * @param password
     * @return
     */
    @RequestMapping("login")
    public APIResult login(@RequestParam String phone,
                            @RequestParam String password){
        CustomerEntity customerEntity = customerService.findByPhoneAndPassword(phone,password);
        if (customerEntity==null){
            return APIResultUtil.responseBusinessFailedResult("账号或者密码错误");
        }
        if (CustomerEntity.CustomerStatus.freeze.equals(customerEntity.getCustomerStatus())){
            return APIResultUtil.responseBusinessFailedResult("账号已冻结");
        }
        String token = UUID.randomUUID().toString().replace("-","");
        setCurrentUser(token,customerEntity);
        //返回登录信息
        JSONObject data = new JSONObject();
        data.put("token",token);
        data.put("customerId",customerEntity.getId());
        data.put("phone",phone);
        return APIResultUtil.returnSuccessResult(data);
    }

    /**
     * 重置密码
     * @param newPassword
     * @param
     * @return
     */
    @RequestMapping("resetPassword")
    public APIResult resetPassword(@RequestParam String phone,
                                    @RequestParam String newPassword,
                                   @RequestParam String smsCode){
        CustomerEntity customerEntity = customerService.findByPhone(phone);
        if (customerEntity==null){
            return APIResultUtil.responseBusinessFailedResult("账号或者密码错误");
        }
        if (CustomerEntity.CustomerStatus.freeze.equals(customerEntity.getCustomerStatus())){
            return APIResultUtil.responseBusinessFailedResult("账号已冻结");
        }
        VerificationCodeEntity verificationCodeEntity = verificationCodeService.checkSmsCode(phone, VerificationCodeEntity.CodeType.resetPWD.name(),smsCode);
        if(verificationCodeEntity==null){
            return APIResultUtil.responseBusinessFailedResult("验证码无效");
        }
        verificationCodeEntity.setCodeStatus(VerificationCodeEntity.CodeStatus.expire);
        verificationCodeService.save(verificationCodeEntity);
        String md5Password = DigestUtils.md5Hex(DigestUtils.md5Hex(newPassword));
        customerEntity.setPassword(md5Password);
        customerService.save(customerEntity);
        String token = UUID.randomUUID().toString().replace("-","");
        setCurrentUser(token,customerEntity);
        //返回登录信息
        JSONObject data = new JSONObject();
        data.put("token",token);
        data.put("customerId",customerEntity.getId());
        data.put("phone",phone);
        return APIResultUtil.returnSuccessResult(data);
    }

}
