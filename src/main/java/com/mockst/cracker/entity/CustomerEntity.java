package com.mockst.cracker.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @Auther: zhiwei
 * @Date: 2019/10/18 20:19c
 * @Description:
 */
@Data
@Entity
@Table(name = "tb_customer", uniqueConstraints = {@UniqueConstraint(columnNames = {"phone"}, name = "phone")})
@org.hibernate.annotations.Table(appliesTo = "tb_customer", comment = "客户表")
public class CustomerEntity extends AbstractEntity {

    /**
     * 验证码类型
     */
    public static enum CustomerStatus {
        normal,//注册
        freeze//冻结
    }

    @Column(columnDefinition = "varchar(20) default '' comment '手机号'")
    private String phone;

    @Column(columnDefinition = "varchar(50) default '' comment '密码'")
    private String password;

    @Enumerated(EnumType.ORDINAL)
    @Column(columnDefinition = "int(1) default 0 comment '状态1正常 0冻结'")
    private CustomerStatus customerStatus;

    @Column(columnDefinition = "varchar(100) default '' comment '微信openid'")
    private String openId;

    @Column(columnDefinition = "varchar(100) default '' comment '微信昵称'")
    private String nickname;

    @Column(columnDefinition = "varchar(255) default '' comment '微信头像'")
    private String avatar;

}
