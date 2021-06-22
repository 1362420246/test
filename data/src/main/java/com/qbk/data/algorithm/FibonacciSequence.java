package com.qbk.data.algorithm;

/**
 * 递归
 *
 * 斐波那契数列（Fibonacci sequence），又称黄金分割数列、因数学家莱昂纳多·斐波那契（Leonardoda Fibonacci）以兔子繁殖为例子而引入，
 * 故又称为“兔子数列”
 *
 * 数列：0、1、1、2、3、5、8、13、21、34、55、89、144……
 *
 * 在数学上，斐波那契数列以如下被以递推的方法定义：F(1)=1，F(2)=1, F(n)=F(n - 1)+F(n - 2)（n ≥ 3，n ∈ N*）
 */
public class FibonacciSequence {

    public static void main(String[] args) {
        int i = sequence(13);
        System.out.println(i);
    }

    /**
     * 求第n位的值
     */
    private static int sequence(int n){
        if(n <= 1){
            return 0;
        }else if (n == 2){
            return 1;
        }
        return sequence(n - 1) + sequence( n - 2);
    }
}
