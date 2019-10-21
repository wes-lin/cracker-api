package com.mockst.cracker.controller.app;

import com.mockst.cracker.annotate.Phone;
import com.mockst.cracker.config.WebConfigurer;
import com.mockst.cracker.entity.VerificationCodeEntity;
import com.mockst.cracker.result.APIResult;
import com.mockst.cracker.result.APIResultUtil;
import com.mockst.cracker.service.VerificationCodeService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: zhiwei
 * @Date: 2019/10/19 13:00
 * @Description:
 */
@Validated
@RestController
@RequestMapping(WebConfigurer.APP_BASE_PATH+"/common")
public class CommonController extends AbstractController {

    @Autowired
    private VerificationCodeService verificationCodeService;

    /**
     * 发送验证码
     */
    @RequestMapping("sendSmsCode")
    public APIResult sendSmsCode(@Phone String phone , @RequestParam String codeType){
        if (verificationCodeService.isSend(phone,codeType)){
            return APIResultUtil.responseBusinessFailedResult("10分钟内已发送短信");
        }
        VerificationCodeEntity verificationCode = new VerificationCodeEntity();
        verificationCode.setCodeType(VerificationCodeEntity.CodeType.valueOf(codeType));
        verificationCode.setCodeStatus(VerificationCodeEntity.CodeStatus.unexpired);
        verificationCode.setPhone(phone);
        verificationCode.setCode(RandomStringUtils.randomNumeric(6));
        verificationCodeService.save(verificationCode);
        return APIResultUtil.returnSuccessResult(verificationCode.getCode());
    }

    /**
     * 校验验证码
     * @param phone
     * @param codeType
     * @return
     */
    @RequestMapping("checkSmsCode")
    public APIResult checkSmsCode(@Phone String phone , @RequestParam String codeType,@RequestParam String smsCode){
        VerificationCodeEntity verificationCodeEntity = verificationCodeService.checkSmsCode(phone,codeType,smsCode);
        if (verificationCodeEntity==null){
            return APIResultUtil.responseBusinessFailedResult("验证码无效");
        }
        return APIResultUtil.returnSuccessResult(null);
    }


}
