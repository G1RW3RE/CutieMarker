package com.ggloria.CutieMarker.widgets;

import com.ggloria.CutieMarker.ImageBag;
import javafx.scene.image.Image;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.swing.border.BevelBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class MainFrame extends JFrame implements ActionListener, MouseListener {

    private static final String MENU_TEXT_OPEN_ARR = "导入图像序列...";
    private static final String MENU_TEXT_OPEN_CM = "导入CM图像...";

    /* 窗体分辨率 */
    public static final int RESOLUTION_W = 1080, RESOLUTION_H = 720;
    public static final int RESOLUTION_W_MIN = 1080, RESOLUTION_H_MIN = 720;
    /* listview 高度 */
    public static final int LV_HEIGHT = 160;

    private static final Dimension MENU_DIMENSION = new Dimension(80, 25);
    private static final Dimension MENU_ITEM_DIMENSION = new Dimension(160, 25);
    private static final Font MENU_ITEM_FONT = new Font("等线", Font.PLAIN, 16);
    private static final Font MENU_FONT = new Font("等线", Font.BOLD, 16);


    /* 菜单 */
    private JMenuBar jMenuBar;
    private JMenu mFile;
    private JMenuItem mFileOpen;
    private JMenuItem mFileOpenCM;

    /* 工具面板 */
    private JPanel toolPanel;
    /* 图像面板 */
    private JPanel imagePanel;

    /* 画布 */
    private AnimationCanvas animFrameCanvas;

    /* 显示贴图 */
    private CutieMarkCanvas cutieMarkCanvas;

    /* listview, 画廊式图片帧序列显示 */
    private GalleryList<Image> lvAnimFrameImage;

    /* 显示贴图尺寸 */
    private final JLabel lblCMImageWidth = new JLabel("宽：");
    private final JLabel lblCMImageHeight = new JLabel("高：");
    private JTextField txtCMImageWidth;
    private JTextField txtCMImageHeight;

    /* 保存图片数据 */
    ImageBag imageBag;

    public MainFrame() {
        super("Gloria's Cutie Marker");
        this.setSize(RESOLUTION_W, RESOLUTION_H);
        this.setMinimumSize(new Dimension(RESOLUTION_W_MIN, RESOLUTION_H_MIN));
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initMenu();
        initComponents();
        initLayouts();
        activateListeners();
        loadData();

        this.setVisible(true);
    }

    /** 初始化控件 */
    private void initComponents() {
        /* 最外层面版，分为左右两部分 */
        toolPanel = new JPanel();
        toolPanel.setBackground(Color.ORANGE);
        imagePanel = new JPanel();
        add(toolPanel);
        add(imagePanel);

        /* CM 显示面板 */
        cutieMarkCanvas = new CutieMarkCanvas();
        /* CM 宽高*/
        txtCMImageWidth = new JTextField();
        txtCMImageWidth.setEditable(false);
        txtCMImageHeight = new JTextField();
        txtCMImageHeight.setEditable(false);

        /* 绘制面板 */
        animFrameCanvas = new AnimationCanvas();

        /* 帧序列展示 */
        lvAnimFrameImage = new GalleryList<>();
        lvAnimFrameImage.setDragEnabled(true);
        lvAnimFrameImage.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        lvAnimFrameImage.setVisibleRowCount(1);
    }

    private void initMenu() {
        /* Menu - 菜单项 */
        jMenuBar = new JMenuBar();

        mFile = new JMenu("文件(F)");
        mFile.setMnemonic('F');
        jMenuBar.add(mFile);

        mFileOpen = new JMenuItem(MENU_TEXT_OPEN_ARR);
        mFileOpenCM = new JMenuItem(MENU_TEXT_OPEN_CM);
        mFile.add(mFileOpen);
        mFile.add(mFileOpenCM);

        this.setJMenuBar(jMenuBar);

        /* 添加菜单项后在这里进行初始化 */
        JMenu[] topMenus = new JMenu[] {mFile};
        JMenuItem[] menuItems = new JMenuItem[] {mFileOpen, mFileOpenCM};
        for (JMenu topMenu : topMenus) {
            topMenu.setFont(MENU_FONT);
            topMenu.setPreferredSize(MENU_DIMENSION);
        }
        for (JMenuItem menuItem : menuItems) {
            menuItem.setFont(MENU_ITEM_FONT);
            menuItem.setPreferredSize(MENU_ITEM_DIMENSION);
        }
    }

    private void initLayouts() {
        SpringLayout frameLayout = new SpringLayout();
        this.setLayout(frameLayout);

        /* 两大面板定位 */
        frameLayout.putConstraint(SpringLayout.NORTH, toolPanel, 0, SpringLayout.NORTH, getContentPane());
        frameLayout.putConstraint(SpringLayout.WEST, toolPanel, 0, SpringLayout.WEST, getContentPane());
        frameLayout.putConstraint(SpringLayout.SOUTH, toolPanel, 0, SpringLayout.SOUTH, getContentPane());
        frameLayout.getConstraints(toolPanel).setWidth(Spring.constant(0, 240, 500));
        frameLayout.putConstraint(SpringLayout.NORTH, imagePanel, 0, SpringLayout.NORTH, getContentPane());
        frameLayout.putConstraint(SpringLayout.EAST, imagePanel, 0, SpringLayout.EAST, getContentPane());
        frameLayout.putConstraint(SpringLayout.WEST, imagePanel, 0, SpringLayout.EAST, toolPanel);
        frameLayout.putConstraint(SpringLayout.SOUTH, imagePanel, 0, SpringLayout.SOUTH, getContentPane());

        /* 帧画布 */
        JPanel animFrameCanvasWrapper = new JPanel();
        animFrameCanvasWrapper.setBorder(
                BorderFactory.createTitledBorder(
//                        BorderFactory.createMatteBorder(5, 5, 5, 5, Color.LIGHT_GRAY),
                        BorderFactory.createBevelBorder(BevelBorder.LOWERED),
                        "绘制区域"
                )
        );
        animFrameCanvasWrapper.setLayout(new BorderLayout());
        animFrameCanvasWrapper.add(animFrameCanvas, BorderLayout.CENTER);
        imagePanel.setLayout(new BorderLayout());
        imagePanel.add(animFrameCanvasWrapper, BorderLayout.CENTER);
        /* 帧序列显示 */
        JPanel lvAnimFrameImageWrapper = new JPanel();
        lvAnimFrameImageWrapper.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        lvAnimFrameImageWrapper.setPreferredSize(new Dimension(0, LV_HEIGHT));
        lvAnimFrameImageWrapper.add(lvAnimFrameImage);
        lvAnimFrameImage.setBackground(this.getBackground());
        imagePanel.add(lvAnimFrameImageWrapper, BorderLayout.SOUTH);

        /* 工具面板 */
        SpringLayout toolPanelLayout = new SpringLayout();
        toolPanel.setLayout(toolPanelLayout);
        /* CM 图片显示 */
        JPanel cutieMarkCanvasWrapper = new JPanel();
        cutieMarkCanvasWrapper.setLayout(new BorderLayout());
        cutieMarkCanvasWrapper.add(cutieMarkCanvas, BorderLayout.CENTER);
        cutieMarkCanvasWrapper.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED), "Cutie Mark"));
        toolPanel.add(cutieMarkCanvasWrapper);
        toolPanelLayout.putConstraint(SpringLayout.NORTH, cutieMarkCanvasWrapper, 0, SpringLayout.NORTH, toolPanel);
        toolPanelLayout.putConstraint(SpringLayout.EAST, cutieMarkCanvasWrapper, 0, SpringLayout.EAST, toolPanel);
        toolPanelLayout.putConstraint(SpringLayout.WEST, cutieMarkCanvasWrapper, 0, SpringLayout.WEST, toolPanel);
        toolPanelLayout.getConstraints(cutieMarkCanvasWrapper).setHeight(Spring.constant(240));
        /* CM 图片宽高显示 */
        JPanel cutieMarkDimensionWrapper = new JPanel();
        cutieMarkDimensionWrapper.setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        gridBagConstraints.weightx = 0;
        gridBagConstraints.weighty = 1;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.VERTICAL;
        cutieMarkDimensionWrapper.add(lblCMImageWidth, gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        cutieMarkDimensionWrapper.add(txtCMImageWidth, gridBagConstraints);
        gridBagConstraints.gridx = 2;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.fill = GridBagConstraints.VERTICAL;
        cutieMarkDimensionWrapper.add(lblCMImageHeight, gridBagConstraints);
        gridBagConstraints.gridx = 3;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        cutieMarkDimensionWrapper.add(txtCMImageHeight, gridBagConstraints);
        toolPanel.add(cutieMarkDimensionWrapper);
        toolPanelLayout.putConstraint(SpringLayout.NORTH, cutieMarkDimensionWrapper, 0, SpringLayout.SOUTH, cutieMarkCanvasWrapper);
        toolPanelLayout.putConstraint(SpringLayout.EAST, cutieMarkDimensionWrapper, 0, SpringLayout.EAST, toolPanel);
        toolPanelLayout.putConstraint(SpringLayout.WEST, cutieMarkDimensionWrapper, 0, SpringLayout.WEST, toolPanel);
        toolPanelLayout.getConstraints(cutieMarkDimensionWrapper).setHeight(Spring.constant(40));

    }

    /** 添加控件监听器 */
    private void activateListeners() {
        mFileOpen.addActionListener(this);
        mFileOpenCM.addActionListener(this);
        animFrameCanvas.addMouseListener(this);
        cutieMarkCanvas.addMouseListener(this);
        cutieMarkCanvas.addMouseMotionListener(animFrameCanvas);
    }

    /** 初始化各个数据成员 */
    private void loadData() {
        imageBag = new ImageBag();
        animFrameCanvas.setImageBag(imageBag);
        cutieMarkCanvas.setImageBag(imageBag);
        lvAnimFrameImage.setImageBag(imageBag);
    }

    /** 对话框打开 CM 文件并更新界面 */
    private void showOpenCutieMarkDialog() {
        FileDialog fileDialog = new FileDialog(this, "选择CM图片", FileDialog.LOAD);
        fileDialog.setModal(true);
        fileDialog.setVisible(true);
        if(fileDialog.getFiles().length == 0) return;
        imageBag.loadCutieMarkFile(fileDialog.getFiles()[0]);
        cutieMarkCanvas.initLoad();
        // updateUI
        cutieMarkCanvas.repaint();
        repaintCutieMarkDimensionText();
    }

    /** 对话框打开动画序列文件并更新界面 */
    private void showOpenAnimFrameDialog() {
        FileDialog fileDialog = new FileDialog(this, "选择图片序列", FileDialog.LOAD);
        fileDialog.setModal(true);
        fileDialog.setMultipleMode(true);
        fileDialog.setVisible(true);
        imageBag.loadImageFiles(fileDialog.getFiles());
        animFrameCanvas.initLoad();
        // updateUI
        animFrameCanvas.repaint();
        repaintCutieMarkDimensionText();
    }

    /** update UI */
    private void repaintCutieMarkDimensionText() {
        txtCMImageWidth.setText("" + imageBag.getCmWidth());
        txtCMImageHeight.setText("" + imageBag.getCmHeight());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() instanceof JMenuItem) {
            if(e.getSource() == mFileOpen) {
                showOpenAnimFrameDialog();
            } else if(e.getSource() == mFileOpenCM) {
                showOpenCutieMarkDialog();
            }
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == animFrameCanvas) {
            if(e.getClickCount() >= 2) {
                showOpenAnimFrameDialog();
            }
        } else if(e.getSource() == cutieMarkCanvas) {
            if(e.getClickCount() >= 2) {
                showOpenCutieMarkDialog();
            }
        }
    }

    private boolean cmImageDragging = false;

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getSource() == cutieMarkCanvas) {
           if(imageBag.getCutieMarkImage() != null) {
                cmImageDragging = true;
                this.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                animFrameCanvas.mouseXOff = animFrameCanvas.getLocationOnScreen().x - cutieMarkCanvas.getLocationOnScreen().x + (int) (imageBag.getCmWidth() * animFrameCanvas.getZoomScale() / 2);
                animFrameCanvas.mouseYOff = animFrameCanvas.getLocationOnScreen().y - cutieMarkCanvas.getLocationOnScreen().y + (int) (imageBag.getCmHeight() * animFrameCanvas.getZoomScale() / 2);
                animFrameCanvas.setCmVisible(true);
           }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getSource() == cutieMarkCanvas) {
            if(imageBag.getCutieMarkImage() != null) {
                cmImageDragging = false;
                this.setCursor(Cursor.getDefaultCursor());
                animFrameCanvas.setCmVisible(false);
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
