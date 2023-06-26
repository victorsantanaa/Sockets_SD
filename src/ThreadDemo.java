public class ThreadDemo extends Thread {

    private String threadName;

    public ThreadDemo(String nome) {
        threadName = nome;
    }

    @Override
    public void run() {
        System.out.println("Dentro da Thread");
        try {
            for (int i = 4; i > 0; i--) {
                System.out.println("T: " + threadName + " " +i);
                sleep(1000);
            }
        } catch (InterruptedException exception) {
            System.out.println(exception);
        }
    }
}
