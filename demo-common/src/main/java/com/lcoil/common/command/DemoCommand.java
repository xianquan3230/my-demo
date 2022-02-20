package com.lcoil.common.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname DemoCommand
 * @Description TODO
 * @Date 2022/2/19 8:46 PM
 * @Created by l-coil
 */
@Data
public class DemoCommand extends BaseCommand{
    private String test;
}
