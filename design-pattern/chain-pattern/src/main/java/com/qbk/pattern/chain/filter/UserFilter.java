package com.qbk.pattern.chain.filter;

/**
 * 责任链方式一：外部控制模式
 * 对于外部控制的方式，这种方式比较简单，链的每个节点只需要专注于各自的逻辑即可，
 * 而当前节点调用完成之后是否继续调用下一个节点，这个则由外部控制逻辑进行。
 */
public interface UserFilter {

    String USERNAME = "qbk";

    String PASSWORD = "123";

    /**
     * 校验
     */
    boolean check(String username ,String password);
}
