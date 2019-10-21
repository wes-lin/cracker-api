package com.mockst.cracker.controller.app;

import com.mockst.cracker.config.WebConfigurer;
import com.mockst.cracker.entity.WordEntity;
import com.mockst.cracker.repository.WordRepository;
import com.mockst.cracker.result.APIResult;
import com.mockst.cracker.result.APIResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Auther: zhiwei
 * @Date: 2019/10/18 21:00
 * @Description:
 */
@RestController
@RequestMapping(WebConfigurer.APP_BASE_PATH+"/home")
public class HomeController extends AbstractController{

    @Autowired
    private WordRepository wordRepository;

    @RequestMapping("index")
    public APIResult home(){
        List<WordEntity> wordEntityList = wordRepository.findAll();
        return APIResultUtil.returnSuccessResult(wordEntityList);
    }
}
