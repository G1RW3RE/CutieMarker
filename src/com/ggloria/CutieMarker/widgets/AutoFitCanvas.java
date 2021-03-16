package com.ggloria.CutieMarker.widgets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Optional;

/** 用于显示逐帧动画的面板 */
public abstract class AutoFitCanvas extends JPanel {

    protected Rectangle drawWindow;
    protected BufferedImage currentDisplayImage;

    public static final String DEFAULT_PROMPT = "双击导入图片";
    protected JLabel lblPrompt;

    /** 图片缩放比，实际显示 / 原始大小 */
    protected float zoomScale;

    public float getZoomScale() {
        return zoomScale;
    }

    public AutoFitCanvas() {
        this(DEFAULT_PROMPT);
    }

    public AutoFitCanvas(String prompt) {
        super();
        drawWindow = new Rectangle();
        lblPrompt = new JLabel(Optional.ofNullable(prompt).orElse(DEFAULT_PROMPT));
        lblPrompt.setHorizontalAlignment(SwingConstants.CENTER);
        setLayout(new BorderLayout());
        add(lblPrompt, BorderLayout.CENTER);
    }

    /** 初始化加载 */
    public void initLoad() {
        // empty function to override
    }

    protected void updateDrawWindow() {
        if(currentDisplayImage != null) {
            int imageWidth = currentDisplayImage.getWidth();
            int imageHeight = currentDisplayImage.getHeight();
            float ratio = imageWidth / (float) imageHeight;
            int canvasWidth = getWidth();
            int canvasHeight = getHeight();
            // "目"
            if(ratio > canvasWidth / (float) canvasHeight) {
                drawWindow.x = 0;
                drawWindow.width = canvasWidth;
                drawWindow.height = (int) (canvasWidth / ratio);
                drawWindow.y = (canvasHeight - drawWindow.height) / 2;
            } else {
                drawWindow.y = 0;
                drawWindow.width = (int) (canvasHeight * ratio);
                drawWindow.height = canvasHeight;
                drawWindow.x = (canvasWidth - drawWindow.width) / 2;
            }
            zoomScale = drawWindow.width / (float) imageWidth;
            _update_w = this.getWidth();
            _update_h = this.getHeight();
        }
    }

    int _update_w, _update_h;

    private boolean needUpdateWindow() {
        return _update_w != getWidth() || _update_h != getHeight();
    }

    /** 覆写绘制事件 */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if(needUpdateWindow()) {
            updateDrawWindow();
        }
        if(currentDisplayImage != null) {
            lblPrompt.setVisible(false);
            g.drawImage(currentDisplayImage, drawWindow.x, drawWindow.y, drawWindow.width, drawWindow.height, this);
        } else {
            lblPrompt.setVisible(true);
        }
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);
        if(lblPrompt != null) { lblPrompt.setFont(font); }
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }
}
