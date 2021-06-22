package com.qbk.maptest;

import lombok.Data;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * jdk8 hashmap 死循环问题
 */
public class EndlessLoop {

    public static void main(String[] args) {
        Set<Integer> users = new HashSet<>();

        List<User> userIds = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            User user = new User();
            user.setId(i);
            userIds.add(user);
        }

        userIds.parallelStream()
                .forEach(u -> users.add(u.getId()));

        System.out.println(users.size());
    }

    @Data
    static class User {
        private Integer id;
    }
}