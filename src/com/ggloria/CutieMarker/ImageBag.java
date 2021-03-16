package com.ggloria.CutieMarker;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.awt.Image;

public class ImageBag {

    /** 动画 */
    List<BufferedImage> animImages;

    /** cutie mark 贴图 */
    BufferedImage cmImage;

    public int getCmWidth() {
        return cmWidth;
    }

    public int getCmHeight() {
        return cmHeight;
    }

    /** 贴图宽高 */
    int cmWidth, cmHeight;

    public ImageBag() {
        animImages = new ArrayList<>();
    }

    /** 加载动画序列 */
    public void loadImageFiles(File[] files) {
        if (files == null || files.length == 0) {
            return;
        }
        for (File f : files) {
            if (f.isFile()) {
                try {
                    BufferedImage img = ImageIO.read(f);
                    if(img != null) animImages.add(img);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /** 加载 Cutie Mark 图像 */
    public void loadCutieMarkFile(File file) {
        if(file != null) {
            try {
                cmImage = ImageIO.read(file);
                if(cmImage != null) {
                    cmWidth = cmImage.getWidth();
                    cmHeight = cmImage.getHeight();
                    // 重置缩放图片的缓存
                    lastScale = 0f;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public int getSize() {
        return animImages.size();
    }

    /** 获取动画序列的第一张图 */
    public BufferedImage getFirstImage() {
        if(animImages.size() > 0) {
            return animImages.get(0);
        } else {
            return null;
        }
    }

    /** 获取 Cutie Mark 贴图 */
    public BufferedImage getCutieMarkImage() {
        return cmImage;
    }

    private Image scaledImageCache;
    private float lastScale;

    public Image getScaledCutieMarkImage(float scale) {
        if(scale == lastScale) {
            return scaledImageCache;
        } else {
            lastScale = scale;
            return scaledImageCache = cmImage.getScaledInstance(((int) (cmWidth * scale)), ((int) (cmHeight * scale)), Image.SCALE_SMOOTH);
        }
    }
}
