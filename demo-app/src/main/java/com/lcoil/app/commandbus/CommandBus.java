package com.lcoil.app.commandbus;

import com.lcoil.common.command.Command;

/**
 * @Classname CommandBus
 * @Description TODO
 * @Date 2022/2/19 8:16 PM
 * @Created by l-coil
 */
public interface CommandBus {
    boolean send(Command command);
}
