package com.qbk.lua;

import org.redisson.api.RFuture;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * lua脚本
 **/
@RestController
public class LuaController {
    @Autowired
    RedissonClient redissonClient;

    private final String LIMIT_LUA = "local times=redis.call('incr',KEYS[1])\n" +
            "if times==1 then\n" +
            "    redis.call('expire',KEYS[1],ARGV[1])\n" +
            "end\n" +
            "if times > tonumber(ARGV[2]) then\n" +
            "    return 0\n" +
            "end \n" +
            "return 1";

    /**
     *  限流
     */
    @GetMapping("/lua/{id}")
    public String lua(@PathVariable("id")Integer id) throws ExecutionException, InterruptedException {
        //脚本操作对象
        RScript rScript = redissonClient.getScript();
        List<Object> keys = Collections.singletonList("LIMIT:" + id);
        RFuture<Object> future =
                //Lua脚本执行
                rScript.evalAsync(
                        RScript.Mode.READ_WRITE, //脚本模式：读写
                        LIMIT_LUA,//lua脚本
                        RScript.ReturnType.INTEGER,// 返回值类型
                        keys,//key
                        10,3);//参数
        return future.get().toString();
    }
}
