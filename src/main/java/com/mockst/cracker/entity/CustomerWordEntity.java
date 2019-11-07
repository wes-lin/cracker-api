package com.mockst.cracker.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * @Auther: zhiwei
 * @Date: 2019/10/27 23:44
 * @Description:
 */
@Data
@Entity
@Table(name = "tb_customer_word", indexes = {@Index(columnList = "keyword")})
@org.hibernate.annotations.Table(appliesTo = "tb_customer_word", comment = "用户单词")
public class CustomerWordEntity extends AbstractEntity {

    @Column(columnDefinition = "varchar(100) default '' comment '单词内容'")
    private String keyword;

    @Column(columnDefinition = "int(4) default 0 comment '级别'")
    private Integer level;

    @Column(columnDefinition = "varchar(32) default '' comment '所属用户'")
    private String customerId;

    @Column(columnDefinition = "varchar(32) default '' comment '所属词本'")
    private String bookId;

}
