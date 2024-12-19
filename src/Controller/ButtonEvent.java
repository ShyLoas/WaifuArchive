package Controller;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class ButtonEvent extends JPanel implements ActionListener {

    private int row, col;
    private int bound = 2;
    private int size = 65, score = 0;
    private JButton[][] btn;
    private Point p1 = null;
    private Point p2 = null;
    private Controller algorithm;
    private MainFrame frame;
    private PointLine line;
    private int item;

    public ButtonEvent(MainFrame frame, int row, int col) {
        this.frame = frame;
        this.row = row + 2;
        this.col = col + 2;
        item = row * col / 2; //đảm bảo các icon luôn chia hết cho 2 nhằm xác định 2 icon trùng nhau

        setLayout(new GridLayout(row, col, bound, bound));
        setBackground(Color.decode("#C6E7DE"));
        setPreferredSize(new Dimension((size + bound) * col, (size + bound) * row));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setAlignmentY(JPanel.CENTER_ALIGNMENT);

        newGame(); //bắt đầu trò chơi mới
    }

    //tạo nút cho mảng
    private void addArrayButton() {
        btn = new JButton[row][col];
        for (int i = 1; i < row - 1; i++) {
            for (int j = 1; j < col - 1; j++) {
                btn[i][j] = createButton(i + "," + j);
                Icon icon = getIcon(algorithm.getMatrix()[i][j]);
                btn[i][j].setIcon(icon);
                add(btn[i][j]);
            }
        }
    }

    //hàm lấy hình ảnh cho icon từ folder sources
    private Icon getIcon(int index) {
        int width = 70, height = 70;
        Image image = new ImageIcon(getClass().getResource(
                "/sources/" + index + ".png")).getImage();
        Icon icon = new ImageIcon(image.getScaledInstance(width, height,
                image.SCALE_SMOOTH));
        return icon;
    }

    //hàm tạo nút
    private JButton createButton(String action) {
        JButton bt = new JButton();
        bt.setActionCommand(action);
        bt.setBorder(null);
        bt.addActionListener(this);
        return bt;
    }

    //đổi màu nền cho trùng với background khi hai nút chính xác
    public void execute(Point p1, Point p2) {
        setDisable(btn[p1.x][p1.y]);
        setDisable(btn[p2.x][p2.y]);
    }

    //hàm set nút trùng với màu nền
    private void setDisable(JButton btn) {
        btn.setIcon(null);
        btn.setBackground(Color.decode("#C6E7DE"));
        btn.setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String btnIndex = e.getActionCommand();
        int indexDot = btnIndex.lastIndexOf(",");
        int x = Integer.parseInt(btnIndex.substring(0, indexDot));
        int y = Integer.parseInt(btnIndex.substring(indexDot + 1, btnIndex.length()));
        if (p1 == null) { //nếu nút được chọn
            p1 = new Point(x, y); //tạo điểm cho nút đó
            btn[p1.x][p1.y].setBorder(new LineBorder(Color.RED)); //set màu viền thành đỏ để người chơi dễ xasc định 
        } else {
            p2 = new Point(x, y); //tạo điểm cho nút 2 sau khi nút 1 được chọn
            line = algorithm.checkTwoPoint(p1, p2); //kiểm tra xem hai nút đó có trùng hay không
            if (line != null) {
                algorithm.getMatrix()[p1.x][p1.y] = 0;
                algorithm.getMatrix()[p2.x][p2.y] = 0;
                execute(p1, p2); //xoá hai điểm trong ma trận
                line = null;
                score += 20;
                item--;
                frame.time++;
                frame.lbScore.setText(score + "");
            }
            btn[p1.x][p1.y].setBorder(null);
            p1 = null;
            p2 = null;
            if (item == 0) { //nếu không còn icon nào, game sẽ hiện thông báo
                if (frame.showDialogNewGame("You are winner!\nDo you want play again?", "Win", 1) == true) {
                }
            }
        }
    }

    //hàm tạo game mới
    public void newGame() {
        algorithm = new Controller(this.frame, this.row, this.col);
        addArrayButton();
    }
}
