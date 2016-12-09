package pl.magneztech.opencv;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MyFrame extends JFrame {
    VideoCap videoCap = new VideoCap();
    private JPanel contentPane;

    /**
     * Create the frame.
     */
    public MyFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 650, 490);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        new MyThread().start();
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MyFrame frame = new MyFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void paint(Graphics g) {
        g = contentPane.getGraphics();
        g.drawImage(videoCap.getOneFrame(), 0, 0, this);
    }

    class MyThread extends Thread {
        @Override
        public void run() {
            for (; ; ) {
                repaint();
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                }
            }
        }
    }
}