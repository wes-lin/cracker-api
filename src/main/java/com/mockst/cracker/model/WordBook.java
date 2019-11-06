package com.mockst.cracker.model;

import lombok.Data;

/**
 * @Auther: zhiwei
 * @Date: 2019/10/28 20:46
 * @Description:
 */
@Data
public class WordBook {
    private String bookId;
    private String bookName;
    //单词总数
    private long total;
    //已学习数
    private long studyQuantity;
    //满级的单词数
    private long fullLevelQuantity;
    //锁状态
    private String lockStatus;
}
