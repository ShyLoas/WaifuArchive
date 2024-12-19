package Controller;

public class Main {

    MainFrame frame;

    public Main() {
        frame = new MainFrame();
        TimeCount c = new TimeCount();
        c.start();
        new Thread(frame).start();
    }

    //tạo hàm quản lý thời gian trò chơi
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
                } else { //thời gian liên tục giảm khi không dừng
                    frame.time--;
                }
                if (frame.time == 0) {
                    //nếu người chơi hết thời gian, hiển thị thông báo
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
