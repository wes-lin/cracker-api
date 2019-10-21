package com.mockst.cracker.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @Auther: zhiwei
 * @Date: 2019/10/18 22:27
 * @Description:
 */
@Data
@Entity
@Table(name = "tb_word")
@org.hibernate.annotations.Table(appliesTo = "tb_word",comment = "单词表")
public class WordEntity extends AbstractEntity {

    /**
     * 词性
     */
    public static enum WordType {
        vt,
        v
    }

    @Column(columnDefinition = "varchar(100) default '' comment '单词内容'")
    private String keyword;

    @Column(columnDefinition = "varchar(100) default '' comment '音标'")
    private String phonetic;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(10) default '' comment '词性'")
    private WordType accountType = WordType.vt;
}
