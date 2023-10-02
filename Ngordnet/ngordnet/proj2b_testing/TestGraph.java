package ngordnet.proj2b_testing;

import ngordnet.browser.NgordnetQuery;
import ngordnet.browser.NgordnetQueryHandler;
import ngordnet.ngrams.DFSGraph;
import ngordnet.main.HyponymsHandler;
import org.junit.Test;


import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static com.google.common.truth.Truth.assertThat;

public class TestGraph {
    static String wordFile = "./data/ngrams/top_14377_words.csv";
    static String countFile = "./data/ngrams/total_counts.csv";

    static String synsetFile = "./data/wordnet/synsets16.txt";
    static String hyponymFile = "./data/wordnet/hyponyms16.txt";


    @Test
    public void testHyponymsSimple(){

        NgordnetQueryHandler studentHandler= AutograderBuddy.getHyponymHandler(wordFile, countFile, synsetFile, hyponymFile);
        long start = System.currentTimeMillis();
        NgordnetQuery nq = new NgordnetQuery(List.of("change"), 1470, 2019, 0);

        String actual =studentHandler.handle(nq);
        String expected = "[alteration, change, demotion, increase, jump, leap, modification, saltation, transition, variation]";
        long end = System.currentTimeMillis();
        System.out.println(end-start);

        assertThat(actual).isEqualTo(expected);

    }

    @Test
    public void testHyponymsSimple3() {
        NgordnetQueryHandler studentHandler= AutograderBuddy.getHyponymHandler(wordFile, countFile, synsetFile, hyponymFile);
        NgordnetQuery nq = new NgordnetQuery(List.of("transition"),1470, 2019, 0);
        String actual = studentHandler.handle(nq);
        String expected = "[flashback, jump, leap, saltation, transition]";
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    public void testHyponymsSimple4() {
        NgordnetQueryHandler studentHandler= AutograderBuddy.getHyponymHandler(wordFile, countFile, synsetFile, hyponymFile);
        NgordnetQuery nq = new NgordnetQuery(List.of("action"), 1470, 2019, 0);
        String actual = studentHandler.handle(nq);
        String expected = "[action, change, demotion, variation]";
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    public void testHyponymsSimple5() {
        NgordnetQueryHandler studentHandler= AutograderBuddy.getHyponymHandler(wordFile, countFile, synsetFile, hyponymFile);
        NgordnetQuery nq = new NgordnetQuery(List.of("change","occurrence"), 0, 0, 0);
        String actual = studentHandler.handle(nq);
        String expected = "[alteration, change, increase, jump, leap, modification, saltation, transition]";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testHyponymsSimple6() {
        NgordnetQueryHandler studentHandler= AutograderBuddy.getHyponymHandler(wordFile, countFile, synsetFile, hyponymFile);

        NgordnetQuery nq = new NgordnetQuery(List.of("demotion", "variation"), 0, 0, 0);
        String actual = studentHandler.handle(nq);
        String expected = "[]";
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    public void testHyponymsFull1() {
        NgordnetQueryHandler studentHandler = AutograderBuddy.getHyponymHandler(
                wordFile, countFile, "./data/wordnet/synsets.txt", "./data/wordnet/hyponyms.txt");
        NgordnetQuery nq = new NgordnetQuery(List.of("video", "recording"), 0, 0, 0);
        String actual = studentHandler.handle(nq);
        String expected = "[video, video_recording, videocassette, videotape]";
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    public void testHyponymsEdge() {
        NgordnetQueryHandler studentHandler = AutograderBuddy.getHyponymHandler(
                wordFile, countFile, "./data/wordnet/synsets.txt", "./data/wordnet/hyponyms.txt");
        NgordnetQuery nq = new NgordnetQuery(List.of("cognition", "nebula"), 0, 0, 0);
        String actual = studentHandler.handle(nq);
        String expected = "[Orion, nebula]";
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    public void testHyponymsFull2() {
        NgordnetQueryHandler studentHandler = AutograderBuddy.getHyponymHandler(
                wordFile, countFile, "./data/wordnet/synsets.txt", "./data/wordnet/hyponyms.txt");
        NgordnetQuery nq = new NgordnetQuery(List.of("pastry", "tart"), 0, 0, 0);
        String actual = studentHandler.handle(nq);
        String expected = "[apple_tart, lobster_tart, quiche, quiche_Lorraine, tart, tartlet]";
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    public void testHyponymsK() {

        NgordnetQueryHandler studentHandler = AutograderBuddy.getHyponymHandler(
                wordFile, countFile, "./data/wordnet/synsets.txt", "./data/wordnet/hyponyms.txt");
        NgordnetQuery nq = new NgordnetQuery(List.of("food", "cake"), 1950, 1990, 5);
        String actual = studentHandler.handle(nq);
        String expected = "[cake, cookie, kiss, snap, wafer]";
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    public void testHyponymsKEdge1() {

        NgordnetQueryHandler studentHandler = AutograderBuddy.getHyponymHandler(
                wordFile, countFile, "./data/wordnet/synsets.txt", "./data/wordnet/hyponyms.txt");
        NgordnetQuery nq = new NgordnetQuery(List.of("genus"), 1470, 2019, 8);
        String actual = studentHandler.handle(nq);
        String expected = "[genus]";
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    public void testHyponymsKEdge2() {

        NgordnetQueryHandler studentHandler = AutograderBuddy.getHyponymHandler(
                wordFile, countFile, "./data/wordnet/synsets.txt", "./data/wordnet/hyponyms.txt");
        NgordnetQuery nq = new NgordnetQuery(List.of("conception", "resultant"), 1470, 2019, 3);
        String actual = studentHandler.handle(nq);
        String expected = "[resultant]";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testHyponymsKEdge3() {
        NgordnetQueryHandler studentHandler = AutograderBuddy.getHyponymHandler(
                wordFile, countFile, "./data/wordnet/synsets.txt", "./data/wordnet/hyponyms.txt");
        NgordnetQuery nq = new NgordnetQuery(List.of("instrument", "GA"), 1920, 1980, 7);
        String actual = studentHandler.handle(nq);
        String expected = "[]";
        assertThat(actual).isEqualTo(expected);
    }
}
