package com.lcoil.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname CheckController
 * @Description TODO
 * @Date 2022/2/19 10:31 PM
 * @Created by l-coil
 */
@RestController
@RequestMapping()
public class CheckController {

    @GetMapping("checks")
    public String check(){
        return "ok";
    }

}
