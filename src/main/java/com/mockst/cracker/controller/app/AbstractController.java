package com.mockst.cracker.controller.app;

import com.mockst.cracker.entity.CustomerEntity;
import com.mockst.cracker.service.CustomerService;
import com.mockst.cracker.util.RedisKeyConstants;
import com.mockst.cracker.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @Auther: zhiwei
 * @Date: 2019/10/18 21:00
 * @Description:
 */
public abstract class AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractController.class);

    @Autowired
    public RedisUtil redisUtil;

    @Autowired
    private CustomerService customerService;

    protected String getTokenKey(String token){
        return RedisKeyConstants.getRedisKey(RedisKeyConstants.USER_SESSION_KEY,token);
    }

    /**
     * 设置用户缓存
     * @param token
     * @param userEntity
     */
    protected void setCurrentUser(String token, CustomerEntity userEntity){
        if (userEntity == null){
            LOGGER.warn( " ->> 当前userEntity为空 <<- ");
            return;
        }
        String key = getTokenKey(token);
        redisUtil.set(key,userEntity.getId());
    }

    /**
     * 获取缓存的用户
     * @param request
     * @return
     */
    protected CustomerEntity getCurrentUser(HttpServletRequest request){
        String token = request.getParameter("token");
        String key = getTokenKey(token);
        String userId = (String) redisUtil.get(key);
        if (userId == null){
            LOGGER.warn(" ->> 当前userId不存在 <<- ");
            return null;
        }
        Optional<CustomerEntity> optional = customerService.findById(userId);
        return optional.get();
    }

    /**
     * 清除缓存的用户
     * @param request
     */
    protected void delCurrentUser(HttpServletRequest request){
        String token = request.getParameter("token");
        String key = getTokenKey(token);
        redisUtil.del(key);
    }

}
