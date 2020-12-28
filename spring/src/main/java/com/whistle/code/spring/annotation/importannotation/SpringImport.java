package com.whistle.code.spring.annotation.importannotation;

import com.whistle.code.spring.annotation.bean.School;
import com.whistle.code.spring.annotation.bean.User;
import com.whistle.code.spring.annotation.customize.CustomizeBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */

public class SpringImport {
    @Bean
    public User user(){
        return new User();
    }
    @Bean
    public User user2(){
        return new User();
    }



    @CustomizeBean
    public School school(@Autowired User user){
        System.out.println("创建school User");
        final School school = new School();
        school.setUser(user);
        return school;
    }
    @CustomizeBean
    public School school(){
        System.out.println("创建school");
        final School school = new School();
        final User user = new User();
        user.setName("z");
        school.setUser(user);
        return school;
    }

}
