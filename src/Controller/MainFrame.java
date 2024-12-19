package Controller;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class MainFrame extends JFrame implements ActionListener, Runnable {

    private int maxTime = 10;
    public int time = maxTime;
    private int row = 8, col = 8;
    public JLabel lbScore;
    private JProgressBar progressTime;
    private ButtonEvent graphicsPanel;
    private JButton btNewGame;
    private JPanel mainPanel;

    private boolean pause = false;
    private boolean resume = false;

    public MainFrame() {
        add(mainPanel = createMainPanel());
        setTitle("Waifu Archie");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 650);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(createGraphicPanel(), BorderLayout.CENTER);
        panel.add(createControlPanel(), BorderLayout.WEST);
        panel.add(createStatusPanel(), BorderLayout.PAGE_END);
        return panel;
    }

    private JPanel createGraphicPanel() {
        graphicsPanel = new ButtonEvent(this, row, col);
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(198, 231, 222));
        panel.add(graphicsPanel);
        return panel;
    }

    private JPanel createControlPanel() {
        lbScore = new JLabel("0");
        progressTime = new JProgressBar(0, 100);
        progressTime.setValue(100);

        JPanel panelLeft = new JPanel(new GridLayout(0, 1, 5, 5));
        panelLeft.add(new JLabel("Score: "));
        panelLeft.add(new JLabel("Time: "));

        JPanel panelCenter = new JPanel(new GridLayout(0, 1, 5, 5));
        panelCenter.add(lbScore);
        panelCenter.add(progressTime);

        JPanel panelScoreAndTime = new JPanel(new GridLayout(5, 0));
        panelScoreAndTime.add(panelLeft, BorderLayout.WEST);
        panelScoreAndTime.add(panelCenter, BorderLayout.CENTER);

        JPanel panelControl = new JPanel(new BorderLayout(10, 10));
        panelControl.setBorder(new EmptyBorder(10, 3, 5, 3));
        panelControl.add(panelScoreAndTime, BorderLayout.CENTER);
        panelControl.add(btNewGame = createButton("New Game"), BorderLayout.PAGE_END);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder("Status"));
        panel.add(panelControl, BorderLayout.PAGE_START);

        return panel;
    }

    private JButton createButton(String buttonName) {
        JButton btn = new JButton(buttonName);
        btn.addActionListener(this);
        return btn;
    }

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(new Color(184, 246, 233));

        return panel;
    }

    public void newGame() {
        time = maxTime;
        graphicsPanel.removeAll();
        mainPanel.add(createGraphicPanel(), BorderLayout.CENTER);
        mainPanel.validate();
        mainPanel.setVisible(true);
        lbScore.setText("0");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btNewGame) {
            showDialogNewGame("Your game hasn't done. Do you want to create a new game?", "Warning", 0);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            progressTime.setValue((int) ((double) time / maxTime * 100));
        }
    }

    public boolean showDialogNewGame(String message, String title, int t) {
        pause = true;
        resume = false;
        int select = JOptionPane.showOptionDialog(null, message, title,
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                null, null);
        if (select == 0) {
            pause = false;
            newGame();
            return true;
        } else {
            if (t == 1) {
                System.exit(0);
                return false;
            } else {
                resume = true;
                return true;
            }
        }
    }

    public JProgressBar getProgressTime() {
        return progressTime;
    }

    public void setProgressTime(JProgressBar progressTime) {
        this.progressTime = progressTime;
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public boolean isResume() {
        return resume;
    }

    public void setResume(boolean resume) {
        this.resume = resume;
    }

}
