package org.lucas.example.foundation.basic.kata.function;

import org.junit.jupiter.api.Test;
import org.lucas.example.foundation.basic.kata.function.support.ItResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @create: 2017-11-21
 * @description:
 */
public class LambdaDemo {

    @Test
    public void lambda0Test() {
        Map<String, Integer> map = new HashMap<>(16);
        for (int i = 0; i < 10; i++) {
            map.put("ID:" + i, i);
        }
        ItResult<Iterator<String>, String> result = () -> map.keySet().iterator();
        String s = result.skip(2);
        System.out.println(s);
    }

    @Test
    public void lambda1Test() {
        // 为自己的函数体提供目标类型
        Supplier<Runnable> a = () -> () -> System.out.println("hi");

        Callable<Integer> b = true ? (() -> 23) : (() -> 42);

        // 显式提供表达式的类型，这个特性在无法确认目标类型时非常有用
        Object o = (Runnable) () -> System.out.println("hi");
    }

    @Test
    public void lambda2Test() {
        // 规约处理聚合操作
        List<List> list = new ArrayList();
        int sum = list.stream()
                .mapToInt(List::size)
                .sum();
    }

    @Test
    public void testFlatMap() {
        List<List<String>> eggs = new ArrayList<>();
        List<String> egg1 = new ArrayList<>();
        egg1.add("鸡蛋_1_1");
        egg1.add("鸡蛋_1_2");
        egg1.add("鸡蛋_1_3");
        egg1.add("鸡蛋_1_4");
        egg1.add("鸡蛋_1_5");
        List<String> egg2 = new ArrayList<>();
        egg1.add("鸡蛋_2_1");
        egg1.add("鸡蛋_2_2");
        egg1.add("鸡蛋_2_3");
        egg1.add("鸡蛋_2_4");
        egg1.add("鸡蛋_2_5");
        eggs.add(egg2);
        eggs.add(egg1);
        List<String> list = eggs.stream().flatMap(t -> t.stream().map(y -> y.replace("鸡", "煎"))).collect(Collectors.toList());
        System.out.println(list);
    }

}
