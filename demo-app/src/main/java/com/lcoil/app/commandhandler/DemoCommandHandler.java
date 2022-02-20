package com.lcoil.app.commandhandler;

import com.alibaba.fastjson.JSONObject;
import com.lcoil.common.command.DemoCommand;
import com.lcoil.common.core.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Classname DemoCommandHandler
 * @Description TODO
 * @Date 2022/2/19 8:47 PM
 * @Created by l-coil
 */
@Component
public class DemoCommandHandler implements CommandHandler<DemoCommand>{
    private static final Logger logger = LoggerFactory.getLogger(DemoCommandHandler.class);
    @Override
    public void execute(DemoCommand command) {
        boolean valid = validate(command);
        System.out.println(StringUtils.format("command:{}", JSONObject.toJSON(command)));
    }

}
