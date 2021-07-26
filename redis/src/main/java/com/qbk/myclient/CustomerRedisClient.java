package com.qbk.myclient;

/**
 *  自定义redis客户端
 **/
public class CustomerRedisClient {

    private CustomerRedisClientSocket customerRedisClientSocket;

    public CustomerRedisClient(String host,int port) {
        customerRedisClientSocket = new CustomerRedisClientSocket(host,port);
    }

    public String set(String key,String value){
        customerRedisClientSocket.send(convertToCommand(CommandConstant.CommandEnum.SET,key.getBytes(),value.getBytes()));
        //在等待返回结果的时候，是阻塞的
        return customerRedisClientSocket.read();
    }

    public String get(String key){
        customerRedisClientSocket.send(convertToCommand(CommandConstant.CommandEnum.GET,key.getBytes()));
        return customerRedisClientSocket.read();
    }

    /**
     * 转换命令
     *
     * 通过Wireshark工具监控数据包内容:
     * *3\r\n$3\r\nSET\r\n$4\r\nname\r\n$3\r\nqbk
     *
     * 其中 3 代表参数个数，set name qbk， 表示三个参数
     * $3 表示属性长度， $ 表示包含3个字符
     * 客户端和服务器发送的命令或数据一律以 \r\n （CRLF回车+换行）结尾
     */
    public static String convertToCommand(CommandConstant.CommandEnum commandEnum,byte[]... bytes){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(CommandConstant.START).append(bytes.length+1).append(CommandConstant.LINE);
        stringBuilder.append(CommandConstant.LENGTH).append(commandEnum.toString().length()).append(CommandConstant.LINE);
        stringBuilder.append(commandEnum.toString()).append(CommandConstant.LINE);
        for (byte[] by:bytes){
            stringBuilder.append(CommandConstant.LENGTH).append(by.length).append(CommandConstant.LINE);
            stringBuilder.append(new String(by)).append(CommandConstant.LINE);
        }
        return stringBuilder.toString();
    }
}
