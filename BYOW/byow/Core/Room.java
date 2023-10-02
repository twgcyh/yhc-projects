package byow.Core;

import java.awt.*;
import java.util.HashSet;
import java.util.Map;

import byow.TileEngine.TETile;
import byow.Core.MapWorld;

public class Room {
    private int width;
    private int height;
    private Position left_bottom;

    public Room(int w, int h, Position p) {
        width = w;
        height = h;
        left_bottom = p;
    }

    public Position MidPosition() {
        int mid_Px = left_bottom.xp + width / 2;
        int mid_Py = left_bottom.yp + height / 2;
        return new Position(mid_Px, mid_Py);
    }


}



