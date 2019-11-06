package com.mockst.cracker.model;

import lombok.Data;

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
    //释义1
    private String explanation1;
    //释义2
    private String explanation2;
    //结果
    private String resultId;
}
