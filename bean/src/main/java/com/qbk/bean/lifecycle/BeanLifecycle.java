package com.qbk.bean.lifecycle;

/**
 *  bean生命周期
 */
public class BeanLifecycle {
    public static void main(String[] args) throws InterruptedException {
        TestBean bean = new TestBean();
        System.out.println(bean);
        bean = null;

        System.gc();

        Thread.sleep(2000);
    }
}
class TestBean{
    @Override
    protected void finalize() throws Throwable {
        System.out.println("被回收了");
        super.finalize();
    }
}