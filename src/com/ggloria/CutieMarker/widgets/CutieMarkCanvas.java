package com.ggloria.CutieMarker.widgets;

import com.ggloria.CutieMarker.ImageBag;

import java.awt.Graphics;

public class CutieMarkCanvas extends AutoFitCanvas {

    ImageBag imageBag;

    public CutieMarkCanvas() {
        super("双击导入图像");
    }

    public void setImageBag(ImageBag imageBag) {
        this.imageBag = imageBag;
    }

    @Override
    public void initLoad() {
        currentDisplayImage = imageBag.getCutieMarkImage();
        updateDrawWindow();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }
}
