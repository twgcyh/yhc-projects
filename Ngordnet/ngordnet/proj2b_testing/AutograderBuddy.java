package ngordnet.proj2b_testing;

import ngordnet.browser.NgordnetQueryHandler;
import ngordnet.main.HyponymsHandler;
import ngordnet.ngrams.DFSGraph;
import ngordnet.ngrams.NGramMap;


public class AutograderBuddy {
    /** Returns a HyponymHandler */
    public static NgordnetQueryHandler getHyponymHandler(
            String wordFile, String countFile,
            String synsetFile, String hyponymFile) {
        DFSGraph gra = new DFSGraph(synsetFile,hyponymFile);
        NGramMap ngm = new NGramMap(wordFile, countFile);
        return new HyponymsHandler(gra,ngm);
    }
}
