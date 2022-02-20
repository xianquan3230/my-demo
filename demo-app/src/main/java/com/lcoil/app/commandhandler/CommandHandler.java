package com.lcoil.app.commandhandler;

/**
 * @Classname CommandHandler
 * @Description TODO
 * @Date 2022/2/19 8:30 PM
 * @Created by l-coil
 */
public interface CommandHandler<T> {
    /**
     * execute command
     *
     * @param command
     */
    void execute(T command);
    /**
     * validate parameters of command
     */
    default boolean validate(T command){
        return true;
    }
}
