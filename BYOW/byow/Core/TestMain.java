package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;

import java.util.Random;

public class TestMain {
    public static void main(String[] args) {
        // Change these parameters as necessary
        Engine engine = new Engine();
//        TETile[][] tiles = engine.interactWithInputString(args[0]);
//        engine.ter.renderFrame(tiles);
        engine.interactWithKeyboard();


    }
    public static void main1(String[] args) {
        // Change these parameters as necessary
        Engine engine = new Engine();
        TETile[][] tiles = engine.interactWithInputString(args[0]);
        engine.ter.renderFrame(tiles);
    }
}
