package com.ggloria.CutieMarker.widgets;

import com.ggloria.CutieMarker.ImageBag;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/** 用于显示逐帧动画的面板 */
public class AnimationCanvas extends AutoFitCanvas implements MouseMotionListener {

    ImageBag imageBag;

    public AnimationCanvas() {
        super("双击导入动画序列");
        drawWindow = new Rectangle();
    }

    public void setImageBag(ImageBag imageBag) {
        this.imageBag = imageBag;
    }

    /** 初始加载第一张图片 */
    public void initLoad() {
        currentDisplayImage = imageBag.getFirstImage();
        updateDrawWindow();
    }

    /** 覆写绘制事件 */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if(cmVisible) {
            // 对图像进行裁剪，限制在可视区域内
            Rectangle intersect = cmRect.intersection(drawWindow);
            g.drawImage(imageBag.getScaledCutieMarkImage(zoomScale),
                    intersect.x, intersect.y, intersect.x + intersect.width, intersect.y + intersect.height,
                    intersect.x - cmRect.x, intersect.y - cmRect.y, intersect.x - cmRect.x + intersect.width, intersect.y - cmRect.y + intersect.height, this);
        }

    }

    private boolean cmVisible = false;

    /* 放置的区域 */
    private Rectangle cmRect;

    /** 由于是CMCanvas的事件，因此需要减去两者之间的偏移量 */
    public int mouseXOff = 0, mouseYOff = 0;

    public void setCmVisible(boolean cmVisible) {
        this.cmVisible = cmVisible;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(cmVisible) {
            if(imageBag.getCutieMarkImage() != null) {
                cmRect = new Rectangle(
                        e.getX() - mouseXOff,
                        e.getY() - mouseYOff,
                        (int) (imageBag.getCmWidth() * zoomScale),
                        (int) (imageBag.getCmHeight() * zoomScale));
                repaint();
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
