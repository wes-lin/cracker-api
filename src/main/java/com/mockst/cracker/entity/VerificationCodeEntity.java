package com.mockst.cracker.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @Auther: zhiwei
 * @Date: 2019/10/19 12:09
 * @Description:
 */
@Data
@Entity
@Table(name = "tb_verification_code")
@org.hibernate.annotations.Table(appliesTo = "tb_verification_code", comment = "验证码")
public class VerificationCodeEntity extends AbstractEntity {

    /**
     * 验证码类型
     */
    public static enum CodeType {
        register,//注册
        resetPWD//重置密码
    }

    /**
     * 验证码状态
     */
    public static enum CodeStatus {
        unexpired,//未过期
        expire//过期
    }

    @Column(columnDefinition = "varchar(20) default '' comment 'code码'")
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(10) default '' comment '验证码类型'")
    private CodeType codeType = CodeType.register;

    @Enumerated(EnumType.ORDINAL)
    @Column(columnDefinition = "int(1) default 0 comment '验证码状态 0有效，1无效'")
    private CodeStatus codeStatus = CodeStatus.expire;

    @Column(columnDefinition = "varchar(20) default '' comment '手机号'")
    private String phone;

}
