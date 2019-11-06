package com.mockst.cracker.entity;

import com.mockst.cracker.entity.em.BookTypeEnum;
import lombok.Data;

import javax.persistence.*;

/**
 * @Auther: zhiwei
 * @Date: 2019/10/18 22:27
 * @Description:
 */
@Data
@Entity
@Table(name = "tb_word",uniqueConstraints={@UniqueConstraint(columnNames={"keyword"},name = "keyword")})
@org.hibernate.annotations.Table(appliesTo = "tb_word",comment = "单词表")
public class WordEntity extends AbstractEntity {

    @Column(columnDefinition = "varchar(100) default '' comment '单词内容'")
    private String keyword;

    @Column(columnDefinition = "varchar(100) default '' comment '音标'")
    private String phonetic;

    @Column(name ="UK_voice",columnDefinition = "varchar(255) default '' comment '英式发音'")
    private String UKVoice;

    @Column(name = "US_voice",columnDefinition = "varchar(255) default '' comment '美式发音'")
    private String USVoice;

    @Column(columnDefinition = "varchar(32) default '' comment '所属场景'")
    private String sceneId;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(10) default '' comment '词本类型'")
    private BookTypeEnum bookType = BookTypeEnum.n;

}
