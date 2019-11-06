package com.mockst.cracker.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Auther: zhiwei
 * @Date: 2019/11/5 21:44
 * @Description:
 */
@Data
@Entity
@Table(name = "tb_study_round_word_pool")
@org.hibernate.annotations.Table(appliesTo = "tb_study_round_word_pool",comment = "轮次单词池")
public class StudyRoundWordPoolEntity implements Serializable{

    @Id
    @Column(columnDefinition = "varchar(32) default '' comment '所属轮次'")
    private String roundId;

    @Id
    @Column(columnDefinition = "varchar(100) default '' comment '单词内容'")
    private String keyword;

}
