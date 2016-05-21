package com.zhihu.inter.impl;

import com.zhihu.inter.Queue;

/**
 * 队列
 */
public class UserQueue extends Queue{

    private static final String TYPE = "VISITEDQUEUE";

    private static UserQueue queue = new UserQueue();

    private UserQueue() {

    }

    @Override
    public String getType() {
        return TYPE;
    }

    public static Queue getInstance(){
        return queue;
    }
}
