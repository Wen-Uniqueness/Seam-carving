import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

import edu.princeton.cs.algs4.Picture;
public class Gui extends JFrame {
    private JLabel imageLabel;
    private JButton button;
    private Rectangle selectionRect;
    private Point startPoint;
    private Point endPoint;

    private Picture picture;

    private JPanel panel;

    public Gui() {
        // 创建并设置窗口
        setTitle("Image Display Demo");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 创建一个面板
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        // 创建按钮用于打开文件选择器
        button = new JButton("Choose Image");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseImage();
            }
        });
        panel.add(button, BorderLayout.SOUTH);
        // 创建标签用于显示图片
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(imageLabel, BorderLayout.CENTER);



        // 将面板添加到窗口
        add(panel);
        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startPoint = e.getPoint();
                selectionRect = new Rectangle(startPoint);
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                endPoint = e.getPoint();
                // 在这里处理用户选择的区域，例如获取选择区域的坐标或尺寸
            }
        });
        imageLabel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                endPoint = e.getPoint();
                if (startPoint != null) {
                    int x = Math.min(startPoint.x, endPoint.x);
                    int y = Math.min(startPoint.y, endPoint.y);
                    int width = Math.abs(startPoint.x - endPoint.x);
                    int height = Math.abs(startPoint.y - endPoint.y);
                    selectionRect.setBounds(x, y, width, height);
                    repaint();
                }
            }
        });
    }

    private void displayImage(String imagePath) {
        // 设置标签的图标为选择的图片
        ImageIcon imageIcon = new ImageIcon(imagePath);
        // 缩放图片以适应标签的大小
        Image image = imageIcon.getImage().getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(image));
    }

    private void chooseImage() {
        // 创建文件选择器
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File file) {
                // 只接受图片文件
                String filename = file.getName().toLowerCase();
                return file.isDirectory() || filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".png") || filename.endsWith(".gif");
            }

            @Override
            public String getDescription() {
                return "Image Files (*.jpg, *.jpeg, *.png, *.gif)";
            }
        });

        // 显示打开文件对话框
        int returnValue = fileChooser.showOpenDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            picture=new Picture(selectedFile.getAbsolutePath());
            displayImage(selectedFile.getAbsolutePath());
        }
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (selectionRect != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(new Color(0, 0, 255, 100)); // 设置选择框的颜色和透明度
            g2d.fill(selectionRect);
            g2d.setColor(Color.BLUE);
            g2d.draw(selectionRect);
        }
    }
    public static void main(String[] args) {
        // 确保在事件调度线程中创建和显示 GUI
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Gui().setVisible(true);
            }
        });
    }
}
