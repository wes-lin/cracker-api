package com.mockst.cracker.model;

import lombok.Data;

/**
 * @Auther: zhiwei
 * @Date: 2019/11/3 22:54
 * @Description:
 */
@Data
public class RoundResultInfo {

    //学习数量
    private long studyQuantity;
    //正确数量
    private long rightQuantity;
    //错误数量
    private long errorQuantity;
}
