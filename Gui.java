import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.File;
import back_end.Stablize_SC;
import edu.princeton.cs.algs4.Picture;

public class Gui extends JFrame {
    private JLabel imageLabel;
    private JButton buttonChooseImage;
    private JButton buttonResizeImage;
    private JButton buttonApply;
    private JButton buttonDraw1;
    private JButton buttonDraw2;
    private Rectangle selectionRect;
    private Point startPoint;
    private Point endPoint;
    private Picture picture;
    private JPanel panel;
    private int new_width;
    private int new_height;
    private int[][] pixelStatus;
    private int model = 0;

    Image image;

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

        buttonDraw1 = new JButton("Protect");
        buttonDraw1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model = 1;
            }
        });


        buttonDraw2 = new JButton("Erase");
        buttonDraw2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model = -1;
            }
        });

        // 创建一个子面板用于包含按钮
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(buttonChooseImage);
        buttonPanel.add(buttonResizeImage);
        buttonPanel.add(buttonApply);
        buttonPanel.add(buttonDraw1);
        buttonPanel.add(buttonDraw2);

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
                if (startPoint != null && endPoint != null) {
                    //drawOnImage(startPoint, endPoint);
                }
            }
        });

        imageLabel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                endPoint = e.getPoint();
                Point convertedPoint = convertPoint(endPoint, imageLabel);
                if (startPoint != null) {
                    if (convertedPoint.x<picture.width()&&convertedPoint.y<picture.height()){
                        Graphics g = image.getGraphics();

                        if (model==1){
                            g.setColor(new Color(0, 0, 255, 200));
                            g.fillOval(convertedPoint.x,convertedPoint.y,5,5);
                            for (int i = 0; i < 8; i++) {
                                for (int j = 0; j < 8; j++) {
                                    if (convertedPoint.x-4+i>0&&convertedPoint.x-4+i<picture.width()){
                                        if (convertedPoint.y-4+j>0&&convertedPoint.y-4+j<picture.height()){
                                            pixelStatus[convertedPoint.x-4+i][convertedPoint.y-4+j]=model;
                                        }
                                    }
                                }
                            }
                        }
                        if (model==-1){
                            g.setColor(new Color(255, 0, 98, 200));
                            g.fillOval(convertedPoint.x,convertedPoint.y,5,5);
                            for (int i = 0; i < 8; i++) {
                                for (int j = 0; j < 8; j++) {
                                    if (convertedPoint.x-4+i>0&&convertedPoint.x-4+i<picture.width()){
                                        if (convertedPoint.y-4+j>0&&convertedPoint.y-4+j<picture.height()){
                                            pixelStatus[convertedPoint.x-4+i][convertedPoint.y-4+j]=model;
                                        }
                                    }
                                }
                            }

                        }
                    }
                    repaint();
                }
            }
        });
    }

    private Point convertPoint(Point point, Component b) {
        Point convertedPoint = SwingUtilities.convertPoint(b, point, this);
        return convertedPoint;
    }

    private void applyResize() {
        picture = Stablize_SC.bs(picture, new_width, new_height);
        picture.show();
    }


    ImageIcon imageIcon;
    private void displayImage(String imagePath) {
        imageIcon = new ImageIcon(imagePath);
        int imageWidth = imageIcon.getIconWidth();
        int imageHeight = imageIcon.getIconHeight();
        imageLabel.setIcon(imageIcon);
        setSize(imageWidth, imageHeight + buttonChooseImage.getHeight() + buttonResizeImage.getHeight());
        setLocationRelativeTo(null);
    }

    private void chooseImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File file) {
                String filename = file.getName().toLowerCase();
                return file.isDirectory() || filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".png") || filename.endsWith(".gif");
            }

            @Override
            public String getDescription() {
                return "Image Files (*.jpg, *.jpeg, *.png, *.gif)";
            }
        });

        int returnValue = fileChooser.showOpenDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            picture = new Picture(selectedFile.getAbsolutePath());
            pixelStatus = new int[picture.width()][picture.height()]; // 初始化二维数组
            displayImage(selectedFile.getAbsolutePath());
            image=new  BufferedImage(picture.width(),picture.height(),BufferedImage.TYPE_INT_ARGB);
        }

    }

    private void resizeImage() {
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
                new_width = Integer.parseInt(widthField.getText());
                new_height = Integer.parseInt(heightField.getText());
                System.out.println("New Width: " + new_width);
                System.out.println("New Height: " + new_height);

                JOptionPane.showMessageDialog(null, "New dimensions have been saved.", "Info", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter valid integers for dimensions.", "Error", JOptionPane.ERROR_MESSAGE);
            }
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
            g2d.drawImage(image,0,0,this);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Gui().setVisible(true);
            }
        });
    }
}