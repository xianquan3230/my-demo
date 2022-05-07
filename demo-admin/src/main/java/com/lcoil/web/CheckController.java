package com.lcoil.web;

import com.lcoil.app.commandbus.CommandBus;
import com.lcoil.common.command.DemoCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Classname CheckController
 * @Description TODO
 * @Date 2022/2/19 10:31 PM
 * @Created by l-coil
 */
@RestController
@RequestMapping("/test")
public class CheckController {

    @Autowired
    private CommandBus commandBus;

    @GetMapping("/checks")
    public String check(){
        return "ok";
    }

    @PostMapping("/demoCommand")
    public String check(@RequestBody DemoCommand demoCommand){
        commandBus.send(demoCommand);
        return "ok";
    }

}
