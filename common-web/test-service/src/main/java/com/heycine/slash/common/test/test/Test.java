package com.heycine.slash.common.test.test;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Test {

    public void test() {
        ThreadLocal threadLocal = new ThreadLocal();
        threadLocal.set("");

        Thread thread = new Thread(() -> {
            System.out.println("");
        });
        thread.start();

        Map<Integer, Integer> map = new HashMap<>();
        for (Map.Entry<Integer, Integer> integerIntegerEntry : map.entrySet()) {
            integerIntegerEntry.getValue();
        }

    }
}
