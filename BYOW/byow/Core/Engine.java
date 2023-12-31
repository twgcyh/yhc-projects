package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.algs4.StdDraw;


public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;


    public Engine() {
        ter.initialize(WIDTH, HEIGHT + 3);
    }

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        MapWorld m2 = new MapWorld(this, WIDTH, HEIGHT);

        m2.drawInterface();
        while (1 == 1) {
            if (m2.isPlaying) {
                m2.drawMouse();
            }
            if (StdDraw.hasNextKeyTyped()) {
                char typed = StdDraw.nextKeyTyped();
                m2.keyExecute(typed);
                if (m2.isPlaying) {
                    m2.showMap();
                }
            }
        }
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, running both of these:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        String s = "";
        int breakpoint = 0;
        MapWorld m = new MapWorld(this, WIDTH, HEIGHT);
        if (input.charAt(0) == 'l' || input.charAt(0) == 'L') {
            String pre = m.loadGame();
            if (pre.length() > 0) {
                m = this.helper(pre);

            }
            if (input.length() > 1) {
                m.isPlaying = true;
            }
            for (int i = 1; i < input.length(); i++) {
                char a = input.charAt(i);
                m.keyExecute(a);
            }
            return m.world;
        }
        if (input.charAt(0) == 'n' || input.charAt(0) == 'N') {
            for (int i = 1; i < input.length(); i++) {
                if (input.charAt(i) == 's' || input.charAt(i) == 'S') {
                    breakpoint = i + 1;
                    break;
                }
                s = s + input.charAt(i);
            }
        }
        long seed = Long.parseLong(s);
        m.exeC.append('n');
        m.exeC.append(seed);
        m.exeC.append('s');
        m.generateMap(seed);
        if (breakpoint < input.length()) {
            m.isPlaying = true;
            for (int i = breakpoint; i < input.length(); i++) {
                char action = input.charAt(i);
                m.keyExecute(action);
            }
        }
        return m.world;
    }


    public MapWorld helper(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        String s = "";
        int breakpoint = 0;
        MapWorld m = new MapWorld(this, WIDTH, HEIGHT);
        if (input.charAt(0) == 'n' || input.charAt(0) == 'N') {
            for (int i = 1; i < input.length(); i++) {
                if (input.charAt(i) == 's') {
                    breakpoint = i + 1;
                    break;
                }
                s = s + input.charAt(i);
            }
        }
        long seed = Long.parseLong(s);
        m.generateMap(seed);
        if (breakpoint < input.length()) {
            m.isPlaying = true;
            for (int i = breakpoint; i < input.length(); i++) {
                char action = input.charAt(i);
                m.keyExecute(action);
            }
        }
        return m;
    }

}
