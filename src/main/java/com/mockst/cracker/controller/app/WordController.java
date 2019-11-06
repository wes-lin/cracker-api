package com.mockst.cracker.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.mockst.cracker.config.WebConfigurer;
import com.mockst.cracker.entity.StudyRoundEntity;
import com.mockst.cracker.result.APIResult;
import com.mockst.cracker.result.APIResultUtil;
import com.mockst.cracker.service.StudyRoundService;
import com.mockst.cracker.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @Auther: zhiwei
 * @Date: 2019/10/30 21:43
 * @Description:
 */
@RestController
@RequestMapping(WebConfigurer.APP_BASE_PATH+"/word")
public class WordController extends AbstractController {

    @Autowired
    private WordService wordService;
    @Autowired
    private StudyRoundService studyRoundService;

    /**
     * 创建一轮次
     * @return
     */
    @RequestMapping("createRound")
    public APIResult createRound(@RequestParam String customerId,@RequestParam String bookId){
        StudyRoundEntity studyRoundEntity = studyRoundService.createRound(bookId,customerId);
        JSONObject data = new JSONObject();
        data.put("roundId", Optional.ofNullable(studyRoundEntity).orElse(new StudyRoundEntity()).getId());
        return APIResultUtil.returnSuccessResult(data);
    }

    /**
     * 创建一轮次
     * @return
     */
    @RequestMapping("createWord")
    public APIResult createWord(@RequestParam String roundId){
        return APIResultUtil.returnSuccessResult(studyRoundService.createWord(roundId));
    }

    /**
     * 提交答案
     * @return
     */
    @RequestMapping("submitWord")
    public APIResult submitWord(@RequestParam String resultId,
                                @RequestParam String explanation){
        return APIResultUtil.returnSuccessResult(studyRoundService.submitWord(resultId,explanation));
    }

    /**
     * 获取结果
     * @return
     */
    @RequestMapping("stopRound")
    public APIResult stopRound(@RequestParam String roundId){
        return APIResultUtil.returnSuccessResult(studyRoundService.stopRound(roundId));
    }

}
