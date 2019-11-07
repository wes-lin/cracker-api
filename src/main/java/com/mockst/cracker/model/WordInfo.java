package com.mockst.cracker.model;

import lombok.Data;

import java.util.List;

/**
 * @Auther: zhiwei
 * @Date: 2019/10/30 21:47
 * @Description:
 */
@Data
public class WordInfo {
    private String keyword;
    private String bookType;
    private String bookName;
    private String bookId;
    private String UKVoice;
    private String USVoice;
    private Integer level;
    private String phonetic;
    //释义
    private List<String> explanations;
    //结果
    private String resultId;
}
