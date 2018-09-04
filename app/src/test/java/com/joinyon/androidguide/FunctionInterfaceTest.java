package com.joinyon.androidguide;


import com.joinyon.androidguide.basejava.lambda.FunctionInterface;

import org.junit.Test;

/**
 * 作者： JoinYon on 2018/8/10.
 * 邮箱：2816886869@qq.com
 */
public class FunctionInterfaceTest {
    @Test
    public void testLambda() {
        //function(() -> System.out.println("Hello World"));
    }

    private void function(FunctionInterface functionInterface) {
        functionInterface.test();
    }
}
