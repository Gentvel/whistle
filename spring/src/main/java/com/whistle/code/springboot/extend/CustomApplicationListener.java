package com.whistle.code.springboot.extend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Component;

/**
 * 准确的说，这个应该不算spring&springboot当中的一个扩展点，ApplicationListener可以监听某个事件的event，
 * 触发时机可以穿插在业务方法执行过程中，用户可以自定义某个业务事件。
 * 但是spring内部也有一些内置事件，这种事件，可以穿插在启动调用中。
 * 我们也可以利用这个特性，来自己做一些内置事件的监听器来达到和前面一些触发点大致相同的事情。
 * @author Gentvel
 * @version 1.0.0
 */
@Component
public class CustomApplicationListener implements ApplicationListener {
    Logger logger = LoggerFactory.getLogger(CustomApplicationListener.class);

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if(event instanceof ContextRefreshedEvent){
            logger.info("ApplicationContext 被初始化或刷新时，该事件被发布。这也可以在ConfigurableApplicationContext接口中使用 refresh()方法来发生。\r\n此处的初始化是指：所有的Bean被成功装载，后处理Bean被检测并激活，所有Singleton Bean 被预实例化，ApplicationContext容器已就绪可用。\n");
        }

        if(event instanceof ContextStartedEvent){
            logger.info("当使用 ConfigurableApplicationContext （ApplicationContext子接口）接口中的 start() 方法启动 ApplicationContext时，该事件被发布。你可以调查你的数据库，或者你可以在接受到这个事件后重启任何停止的应用程序。");
        }

        if(event instanceof ContextStoppedEvent){
            logger.info("当使用 ConfigurableApplicationContext接口中的 stop()停止ApplicationContext 时，发布这个事件。你可以在接受到这个事件后做必要的清理的工作");
        }

        if(event instanceof ContextClosedEvent){
            logger.info("当使用 ConfigurableApplicationContext接口中的 close()方法关闭 ApplicationContext 时，该事件被发布。一个已关闭的上下文到达生命周期末端；它不能被刷新或重启");
        }

//        if(event instanceof RequestHandledEvent){
//            logger.info("这是一个 web-specific 事件，告诉所有 bean HTTP 请求已经被服务。只能应用于使用DispatcherServlet的Web应用。在使用Spring作为前端的MVC控制器时，当Spring处理用户请求结束后，系统会自动触发该事件")
//        }
    }
}