import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import back_end.Stablize_SC;

import edu.princeton.cs.algs4.Picture;

public class Gui extends JFrame {
    private JLabel imageLabel;
    private JButton buttonChooseImage;
    private JButton buttonResizeImage;

    private JButton buttonApply;
    private JButton buttonDraw1;
    private JButton buttonDraw0;
    private JButton buttonDraw2;
    private Rectangle selectionRect;
    private Point startPoint;
    private Point endPoint;
    private Picture picture;
    private JPanel panel;
    private int new_width;
    private int new_height;
    private int model=0;

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
        buttonChooseImage = new JButton("Choose Image");
        buttonChooseImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseImage();
            }
        });

        // 创建按钮用于调整图像尺寸
        buttonResizeImage = new JButton("Resize Image");
        buttonResizeImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resizeImage();
            }
        });

        // 创建按钮用于应用调整后的图像尺寸
        buttonApply = new JButton("Apply");
        buttonApply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyResize();
            }
        });
        
        


        // 创建一个子面板用于包含按钮
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(buttonChooseImage);
        buttonPanel.add(buttonResizeImage);
        buttonPanel.add(buttonApply);

        // 将按钮面板添加到主面板的南部
        panel.add(buttonPanel, BorderLayout.SOUTH);

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

            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println(e.getPoint());
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

    private void applyResize() {
        picture=Stablize_SC.bs(picture,new_width,new_height);
        picture.show();
    }

    private void displayImage(String imagePath) {
        // 设置标签的图标为选择的图片
        ImageIcon imageIcon = new ImageIcon(imagePath);
        // 缩放图片以适应标签的大小
        int imageWidth = imageIcon.getIconWidth();
        int imageHeight = imageIcon.getIconHeight();
        pack();
        setSize(imageWidth, imageHeight + buttonChooseImage.getHeight() + buttonResizeImage.getHeight());
        setLocationRelativeTo(null);
        imageLabel.setIcon(imageIcon);
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

            picture = new Picture(selectedFile.getAbsolutePath());
            displayImage(selectedFile.getAbsolutePath());
            JLabel p = picture.getJLabel();

            p.addMouseListener(new MouseAdapter() {
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

                @Override
                public void mouseClicked(MouseEvent e) {
                    System.out.println(e.getPoint());
                }

            });
        }
    }

    private void resizeImage() {
        // 创建一个对话框来输入新的宽度和高度
        JTextField widthField = new JTextField(5);
        JTextField heightField = new JTextField(5);

        JPanel dialogPanel = new JPanel();
        dialogPanel.add(new JLabel("New Width:"));
        dialogPanel.add(widthField);
        dialogPanel.add(Box.createHorizontalStrut(15)); // 一个间隔
        dialogPanel.add(new JLabel("New Height:"));
        dialogPanel.add(heightField);

        int result = JOptionPane.showConfirmDialog(null, dialogPanel,
                "Enter new dimensions", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                // 解析用户输入的新宽度和高度并存储在new_width和new_height变量中
                new_width = Integer.parseInt(widthField.getText());
                new_height = Integer.parseInt(heightField.getText());
                System.out.println("New Width: " + new_width);
                System.out.println("New Height: " + new_height);

                // 提示用户新尺寸已被存储
                JOptionPane.showMessageDialog(null, "New dimensions have been saved.", "Info", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter valid integers for dimensions.", "Error", JOptionPane.ERROR_MESSAGE);
            }
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
