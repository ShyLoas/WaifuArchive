package Controller;

public class Main {

    MainFrame frame;

    public Main() {
        frame = new MainFrame();
        TimeCount c = new TimeCount();
        c.start();
        new Thread(frame).start();
    }

    class TimeCount extends Thread {

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (frame.isPause()) {
                    if (frame.isResume()) {
                        frame.time--;
                    }
                } else {
                    frame.time--;
                }
                if (frame.time == 0) {
                    if (frame.showDialogNewGame("Full Time\nPlay again?", "Lose", 1) == true) {
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
    }
}
