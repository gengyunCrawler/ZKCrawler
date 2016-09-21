/**
 * <类详细说明:启线程>
 *
 * @Author： Huanghai
 * @Version: 2016-09-21
 **/
public class HelloRunnable implements Runnable{

    @Override
    public void run() {
        System.out.println("should return");
    }

    public static void main(String[] args) {
        new Thread(new HelloRunnable()).start();
    }
}
