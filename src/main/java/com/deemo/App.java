package com.deemo;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Hello world!
 */
public class App {
    static void main(String[] args) {
        System.out.println("Hello World!");

        Map<String, Integer> map = new HashMap<>();
        map.put("AA", 11);
        map.put("BB", 22);

        Set<String> keySet = map.keySet();
        map.put("CC", 33);
        Set<String> keySet1 = map.keySet();
        Collection<Integer> values = map.values();
    }

    @Test
    void mapKeySetTest() {

    }

}

