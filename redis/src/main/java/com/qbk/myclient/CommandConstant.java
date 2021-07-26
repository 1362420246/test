package com.qbk.myclient;

/**
 * redis 协议
 **/
public class CommandConstant {

    /**
     * 开始
     */
    public static final String START="*";

    /**
     * 长度
     */
    public static final String LENGTH="$";

    /**
     * 换行
     */
    public static final String LINE="\r\n";

    public enum CommandEnum{
        SET,
        GET
    }
}
