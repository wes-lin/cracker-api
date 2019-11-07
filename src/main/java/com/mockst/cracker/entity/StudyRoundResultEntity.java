package com.mockst.cracker.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Auther: zhiwei
 * @Date: 2019/10/31 21:50
 * @Description:
 */
@Data
@Entity
@Table(name = "tb_study_round_result")
@org.hibernate.annotations.Table(appliesTo = "tb_study_round_result", comment = "学习轮次结果")
public class StudyRoundResultEntity extends AbstractEntity {

    @Column(columnDefinition = "varchar(32) default '' comment '所属轮次'")
    private String roundId;

    @Column(columnDefinition = "bit(1) default 1 comment '是否正确'")
    private boolean isRight = true;

    @Column(columnDefinition = "varchar(100) default '' comment '提交释义'")
    private String submitExplanation;

    @Column(columnDefinition = "varchar(100) default '' comment '单词内容'")
    private String keyword;

    @Column(columnDefinition = "int(4) default 0 comment '级别'")
    private Integer level;
}
