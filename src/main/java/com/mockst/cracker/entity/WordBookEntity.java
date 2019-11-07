package com.mockst.cracker.entity;

import com.mockst.cracker.entity.em.BookTypeEnum;
import lombok.Data;

import javax.persistence.*;

/**
 * @Auther: zhiwei
 * @Date: 2019/10/28 22:55
 * @Description:
 */
@Data
@Entity
@Table(name = "tb_word_book")
@org.hibernate.annotations.Table(appliesTo = "tb_word_book", comment = "词本")
public class WordBookEntity extends AbstractEntity {

    @Column(columnDefinition = "varchar(100) default '' comment '词本名'")
    private String bookName;

    @Column(columnDefinition = "varchar(32) default '' comment '所属用户'")
    private String customerId;

    @Column(columnDefinition = "varchar(32) default '' comment '所属场景'")
    private String sceneId;

    /**
     * 锁状态
     */
    public enum LockStatus {
        locked,
        unlocked,
    }

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(10) default '' comment '锁状态'")
    private LockStatus lockStatus = LockStatus.unlocked;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(10) default '' comment '词本类型'")
    private BookTypeEnum bookType = BookTypeEnum.n;

}
