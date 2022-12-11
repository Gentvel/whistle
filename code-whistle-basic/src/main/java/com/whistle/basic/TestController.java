package com.whistle.basic;

import com.whistle.starter.WhistleConstants;
import org.springframework.web.bind.annotation.*;

/**
 * @author Gentvel
 */
@RestController
@RequestMapping("test")
public class TestController {



    @PostMapping
    public String test(@RequestBody String aaa){
        return aaa+"344ggg4";
    }

    @GetMapping
    public String get(){
        return WhistleConstants.getWeakCache().toString();
    }


}
