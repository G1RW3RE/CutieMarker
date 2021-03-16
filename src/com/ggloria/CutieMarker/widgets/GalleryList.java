package com.ggloria.CutieMarker.widgets;

import com.ggloria.CutieMarker.ImageBag;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import java.awt.Component;
import java.awt.Graphics;

public class GalleryList<E> extends JList<E> {

    public GalleryList() {
        super();
    }

    private ImageBag imageBag;

    public void setImageBag(ImageBag imageBag) {
        this.imageBag = imageBag;
    }

    public class GalleryCellRenderer<T> extends JPanel implements ListCellRenderer<T> {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            return this;
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
        }
    }
}
