package com.whistle.basic;

import com.whistle.starter.WhistleConstants;
import com.whistle.starter.response.Result;
import org.springframework.web.bind.annotation.*;

/**
 * @author Gentvel
 */
@RestController
@RequestMapping("test")
public class TestController {



    @GetMapping("ok")
    public Result<?> ok(){
        return Result.ok();
    }

    @GetMapping("fail")
    public Result<?> fail(){
        return Result.fail();
    }


}
