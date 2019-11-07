package com.mockst.cracker.entity;

import com.mockst.cracker.entity.em.RoundStatus;
import lombok.Data;

import javax.persistence.*;

/**
 * @Auther: zhiwei
 * @Date: 2019/10/31 21:39
 * @Description:
 */
@Data
@Entity
@Table(name = "tb_study_round")
@org.hibernate.annotations.Table(appliesTo = "tb_study_round", comment = "学习轮次")
public class StudyRoundEntity extends AbstractEntity {

    @Column(columnDefinition = "varchar(32) default '' comment '所属词本'")
    private String bookId;

    @Column(columnDefinition = "varchar(32) default '' comment '所属用户'")
    private String customerId;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20) default '' comment '轮次状态'")
    private RoundStatus roundStatus;

}
