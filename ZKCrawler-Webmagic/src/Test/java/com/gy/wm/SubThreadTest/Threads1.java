package com.gy.wm.SubThreadTest;

/**
 * <类详细说明>
 *
 * @Author： Huanghai
 * @Version: 2016-11-17
 **/
public class Threads1 {

    public static void main(String[] args) {
        SubThread thread = new SubThread();
        thread.start();
        //主线程处理其他工作,让子线程异步去执行.
        mainThreadOtherWork();
        System.out.println("now waiting sub thread done.");
        //主线程其他工作完毕,等待子线程的结束, 调用join系列的方法即可(可以设置超时时间)
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("now all done.");
    }

    private static void mainThreadOtherWork() {
        System.out.println("main thread work start");
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("main thread work done.");
    }

    public static class SubThread extends Thread{
        @Override
        public void run() {
            working();
        }
        private void working() {
            System.out.println("sub thread start working.");
            busy();
            System.out.println("sub thread stop working.");
        }

        private void busy() {
            try {
                sleep(5000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

