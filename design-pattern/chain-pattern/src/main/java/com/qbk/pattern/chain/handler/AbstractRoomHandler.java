package com.qbk.pattern.chain.handler;


/**
 * 责任链方式二：节点控制模式
 * 链的每个节点都包含对下一个节点的引用 ，每个节点调用完成之后，内部逻辑决定是否继续调用下一个节点。
 */
public abstract class AbstractRoomHandler {

    /**
     * 下一个处理器
     */
    protected AbstractRoomHandler nextHandler;

    /**
     * 设置下一个处理器
     */
    public void setNextHanlder(AbstractRoomHandler successor) {
        this.nextHandler = successor;
    }

    /**
     * 处理请求
     */
    public abstract boolean handleRequest(String request);
}
