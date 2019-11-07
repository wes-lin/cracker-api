package com.mockst.cracker.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * @Auther: zhiwei
 * @Date: 2019/10/27 23:05
 * @Description:
 */
@Data
@Entity
@Table(name = "tb_word_scene")
@org.hibernate.annotations.Table(appliesTo = "tb_word_scene", comment = "单词场景")
public class WordSceneEntity extends AbstractEntity {

    @Column(columnDefinition = "varchar(100) default '' comment '场景名称'")
    private String sceneName;

    @Column(columnDefinition = "int(4) default 0 comment '排序'")
    private Integer sort;

}
