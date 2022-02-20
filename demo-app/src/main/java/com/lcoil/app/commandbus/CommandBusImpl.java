package com.lcoil.app.commandbus;

import com.lcoil.app.commandhandler.CommandHandler;
import com.lcoil.common.command.Command;
import com.lcoil.common.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @Classname CommandBusImpl
 * @Description TODO
 * @Date 2022/2/19 8:22 PM
 * @Created by l-coil
 */
@Component(value = "commandBusImpl")
public class CommandBusImpl implements CommandBus {

    private static final Logger logger = LoggerFactory.getLogger(CommandBusImpl.class);
    // command -> commandHandler
    private static final String SUFFIX = "Handler";
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public boolean send(Command command) {
        Class<?> commandClass = command.getClass();
        try {
            CommandHandler<Object> handler = (CommandHandler<Object>) applicationContext.getBean(
               composeHandlerBeanName(commandClass.getSimpleName()));
            handler.execute(command);
        } catch (Exception e) {
            if(e instanceof BusinessException){
                throw e;
            } else {
                logger.error(e.getMessage(),e);
            }
            throw new BusinessException(e.getMessage());
        }
        return false;
    }

    private String composeHandlerBeanName(String commandClassSimpleName){
        String handlerClassName = commandClassSimpleName + SUFFIX;
        char[] charArray = handlerClassName.toCharArray();
        charArray[0] = Character.toLowerCase(charArray[0]);
        return String.valueOf(charArray);
    }
}
