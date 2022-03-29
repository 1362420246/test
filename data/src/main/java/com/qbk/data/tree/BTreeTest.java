package com.qbk.data.tree;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.util.*;

/**
 * 深度优先
 * 英文缩写为DFS即Depth First Search.其过程简要来说是对每一个可能的分支路径深入到不能再深入为止，而且每个节点只能访问一次。
 *
 * 广度优先
 *
 * List深拷贝
 *
 * 多路树
 *                  100000
 *                  110000
 *      111000      112000      113000
 *          112100      112200
 */
public class BTreeTest {
    public static void main(String[] args) {
        /*
            {
                "id":"100000",
                "parent":"0",
                "text":"廊坊银行总行",
                "children":[
                    {
                        "id":"110000",
                        "parent":"100000",
                        "text":"廊坊分行",
                        "children":[
                            {
                                "id":"113000",
                                "parent":"110000",
                                "text":"廊坊银行开发区支行",
                                "leaf":true
                            },
                            {
                                "id":"111000",
                                "parent":"110000",
                                "text":"廊坊银行金光道支行",
                                "leaf":true
                            },
                            {
                                "id":"112000",
                                "parent":"110000",
                                "text":"廊坊银行解放道支行",
                                "children":[
                                    {
                                        "id":"112200",
                                        "parent":"112000",
                                        "text":"廊坊银行三大街支行",
                                        "leaf":true
                                    },
                                    {
                                        "id":"112100",
                                        "parent":"112000",
                                        "text":"廊坊银行广阳道支行",
                                        "leaf":true
                                    }
                                ]
                            }
                        ]
                    }
                ]
            }
         */
        String data = "{\n" +
                "    \"id\":\"100000\",\n" +
                "    \"parent\":\"0\",\n" +
                "    \"text\":\"廊坊银行总行\",\n" +
                "    \"children\":[\n" +
                "        {\n" +
                "            \"id\":\"110000\",\n" +
                "            \"parent\":\"100000\",\n" +
                "            \"text\":\"廊坊分行\",\n" +
                "            \"children\":[\n" +
                "                {\n" +
                "                    \"id\":\"113000\",\n" +
                "                    \"parent\":\"110000\",\n" +
                "                    \"text\":\"廊坊银行开发区支行\",\n" +
                "                    \"leaf\":true\n" +
                "                },\n" +
                "                {\n" +
                "                    \"id\":\"111000\",\n" +
                "                    \"parent\":\"110000\",\n" +
                "                    \"text\":\"廊坊银行金光道支行\",\n" +
                "                    \"leaf\":true\n" +
                "                },\n" +
                "                {\n" +
                "                    \"id\":\"112000\",\n" +
                "                    \"parent\":\"110000\",\n" +
                "                    \"text\":\"廊坊银行解放道支行\",\n" +
                "                    \"children\":[\n" +
                "                        {\n" +
                "                            \"id\":\"112200\",\n" +
                "                            \"parent\":\"112000\",\n" +
                "                            \"text\":\"廊坊银行三大街支行\",\n" +
                "                            \"leaf\":true\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"id\":\"112100\",\n" +
                "                            \"parent\":\"112000\",\n" +
                "                            \"text\":\"廊坊银行广阳道支行\",\n" +
                "                            \"leaf\":true\n" +
                "                        }\n" +
                "                    ]\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    ]\n" +
                "}\n";
        //初始化数据
        Node node = JSONObject.parseObject(data, Node.class);
        System.out.println("-----------------排序前：");
        System.out.println(node);

        //排序
        childrenSort(node);
        System.out.println("-----------------排序后：");
        System.out.println(node);
        System.out.println(JSONObject.toJSONString(node));

        System.out.println("-----------------深度优先1：");
        //深度优先（非递归，Stack迭代）
        depthFirstSearch(node);
        System.out.println("-----------------深度优先2：");
        //深度优先（递归）
        depthFirst(node);

        //TODO 广度优先

    }
    /**
     * 排序
     */
    static void childrenSort(Node node){
        List<Node> children = node.getChildren();
        if(!CollectionUtils.isEmpty(children)){
            //排序的四种Comparator比较器的写法
            Collections.sort(children, new Comparator<Node>() {
                @Override
                public int compare(Node o1, Node o2) {
                    return o1.getId()-o2.getId();
                }
            });
            Collections.sort(children, (node1 ,node2) -> node1.getId() - node2.getId());
            Collections.sort(children, Comparator.comparingInt(Node::getId));
            children.sort(Comparator.comparingInt(Node::getId));
            //递归
            for (Node childrenNode : children) {
                childrenSort(childrenNode);
            }
        }
    }
    /**
     * 深度优先（递归）
     * 英文缩写为DFS即Depth First Search.其过程简要来说是对每一个可能的分支路径深入到不能再深入为止，而且每个节点只能访问一次。
     */
    static void depthFirst(Node node){
        System.out.println(node.getId());
        List<Node> children = node.getChildren();
        if(!CollectionUtils.isEmpty(children)){
            //递归
            for (Node childrenNode : children) {
                depthFirst(childrenNode);
            }
        }
    }
    /**
     * 深度优先（非递归，Stack迭代）
     * 英文缩写为DFS即Depth First Search.其过程简要来说是对每一个可能的分支路径深入到不能再深入为止，而且每个节点只能访问一次。
     * 深度优先遍历各个节点，需要使用到栈（Stack）这种数据结构。Stack的特点是是先进后出。
     */
    static void depthFirstSearch(Node node){
        Stack<Node> nodeStack = new Stack<>();
        nodeStack.add(node);
        while (!nodeStack.isEmpty()){
            //移除栈顶部的对象
            node = nodeStack.pop();
            System.out.println(node.getId());
            //子节点
            List<Node> children = node.getChildren();
            if(!CollectionUtils.isEmpty(children)){
                 //深拷贝
                 List<Node> newList = depCopy(children);
                 //翻转子节点 ,需要倒着插入子节点
                 newList.sort((node1 ,node2) -> node2.getId() - node1.getId());
                for (Node child : newList) {
                     nodeStack.push(child);
                }
            }
        }
    }
    /***
     * List深拷贝
     * 对集合进行深拷贝 注意须要对泛型类进行序列化(实现Serializable)
     */
    public static <T> List<T> depCopy(List<T> srcList) {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        try {
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(srcList);
            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream inStream = new ObjectInputStream(byteIn);
            List<T> destList = (List<T>) inStream.readObject();
            return destList;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
@Data
class Node implements Serializable{
    /**
     * 节点编号
     */
    private Integer id;
    /**
     * 父节点
     */
    private Integer parent;
    /**
     * 节点内容
     */
    private String text;
    /**
     * 叶子节点
     */
    private Boolean leaf = false;
    /**
     * 子节点列表
     */
    private List<Node> children;
}