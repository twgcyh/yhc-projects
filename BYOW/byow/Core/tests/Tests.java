package byow.Core.tests;

import byow.Core.Engine;
import byow.TileEngine.TETile;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class Tests {
    @Test
    public void test1(){
        Engine engine1 = new Engine();
        TETile[][] world1 = engine1.interactWithInputString("n5197880843569031643sasdasd");

        TETile[][] world2 = engine1.interactWithInputString("n5197880843569031643sasdasd");

        assertThat(world1).isEqualTo(world2);
    }
    @Test
    public void test2(){
        Engine engine1 = new Engine();
        TETile[][] world1 = engine1.interactWithInputString("n519788083569031643sasasd");

        TETile[][] world2 = engine1.interactWithInputString("n519780843569031643sasdas");

        assertThat(world1).isNotEqualTo(world2);
    }

    @Test
    public void test3(){
        Engine engine1 = new Engine();
        TETile[][] world1 = engine1.interactWithInputString("n515sasdasd:q");

        TETile[][] world2 = engine1.interactWithInputString("lasdasd");

        assertThat(world1).isEqualTo(world2);
    }


}
