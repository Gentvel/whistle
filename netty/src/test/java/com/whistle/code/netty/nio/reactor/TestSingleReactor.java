package com.whistle.code.netty.nio.reactor;

import com.whistle.code.netty.reactor.single.Reactor;
import org.junit.Test;

/**
 *
 * @author Gentvel
 * @version 1.0.0
 */
public class TestSingleReactor {
    @Test
    public void testSingleReactorServer(){
        Reactor reactor = new Reactor(6888);

        reactor.select();
    }


}
