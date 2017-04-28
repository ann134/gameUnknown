package ru.spbu.arts.game.unknown;

import java.awt.image.BufferedImage;
import java.util.List;

public class Animation {
    private List<BufferedImage> images;
    private Animation next;

    public Animation(List<BufferedImage> images) {
        this.images = images;
        next = this;
    }

    public Animation getNext() {
        return next;
    }

    public void setNext(Animation next) {
        this.next = next;
    }

    public List<BufferedImage> getImages() {
        return images;
    }
}
