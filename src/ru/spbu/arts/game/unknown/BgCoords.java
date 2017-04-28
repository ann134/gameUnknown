package ru.spbu.arts.game.unknown;

import java.util.Objects;

public class BgCoords {

    public int x;
    public int y;

    public BgCoords(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BgCoords coords = (BgCoords) o;
        return x == coords.x && y == coords.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
