package com.mockst.cracker.entity;

import com.mockst.cracker.entity.em.WordTypeEnum;
import lombok.Data;

import javax.persistence.*;

/**
 * @Auther: zhiwei
 * @Date: 2019/10/27 22:39
 * @Description:
 */
@Data
@Entity
@Table(name = "tb_word_explanation",indexes = {@Index(columnList="keyword")})
@org.hibernate.annotations.Table(appliesTo = "tb_word_explanation",comment = "单词释义")
public class WordExplanationEntity extends AbstractEntity {

    @Column(columnDefinition = "varchar(100) default '' comment '单词内容'")
    private String keyword;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(10) default '' comment '词性'")
    private WordTypeEnum wordType = WordTypeEnum.n;

    @Column(columnDefinition = "varchar(100) default '' comment '释义'")
    private String explanation;
}
