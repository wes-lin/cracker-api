package com.mockst.cracker.entity.em;

/**
 * @Auther: zhiwei
 * @Date: 2019/10/29 20:01
 * @Description:
 */
public enum BookTypeEnum {
    n,//名词
    v,//动词
    adj,//形容词
    adv,//副词
    n_v,//名 动词
    n_adj,//名词 形容词
    v_adj,//动词 形容词
    multi,//多种
    ;

    public static String getTitle(BookTypeEnum bookTypeEnum) {
        if (bookTypeEnum == n) {
            return "名词";
        } else if (bookTypeEnum == v) {
            return "动词";
        } else if (bookTypeEnum == adj) {
            return "形容词";
        } else if (bookTypeEnum == adv) {
            return "副词";
        } else if (bookTypeEnum == n_v) {
            return "名词/动词";
        } else if (bookTypeEnum == n_adj) {
            return "名词/形容词";
        } else if (bookTypeEnum == v_adj) {
            return "动词/形容词";
        } else if (bookTypeEnum == multi) {
            return "多词性";
        }
        return "";
    }
}
