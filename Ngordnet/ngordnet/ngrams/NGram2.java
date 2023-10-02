package ngordnet.ngrams;

import edu.princeton.cs.algs4.In;

import java.util.*;

public class NGram2 {
    private static final int MIN_YEAR = 1400;
    private static final int MAX_YEAR = 2100;

    public HashMap<Integer, SynSets> synMap = new HashMap<>();
    public HashMap<Integer, HashSet> hypMap = new HashMap<>();
    public HashMap<String, TreeSet<Integer>> wordToIds = new HashMap<>();
//stop
    public class SynSets extends TreeSet<String> {
        public SynSets() {
            super();
        }
    }


    public NGram2(String synsetFilename, String hyponymFilename) {

        In inSyn = new In(synsetFilename);
        In inHyp = new In(hyponymFilename);
        int ids;
        String whole_Syns;
        String[] syns_array;
        String[] curr1;
        SynSets single_Syns;
        while (inSyn.hasNextLine()) {
            single_Syns = new SynSets();
            curr1 = inSyn.readLine().split(",");
            ids = Integer.parseInt(curr1[0]);
            whole_Syns = curr1[1];
            syns_array = whole_Syns.split(" ");
            for (int i = 0; i < syns_array.length; i++) {
                single_Syns.add(syns_array[i]);
            }

            synMap.put(ids, single_Syns);

        }
        String[] curr2;
        HashSet<Integer> single_Hypos;
        int hyp_Head;
        while (inHyp.hasNextLine()) {
            single_Hypos = new HashSet<>();
            curr2 = inHyp.readLine().split(",");
            hyp_Head = Integer.parseInt(curr2[0]);
            for (int i = 1; i < curr2.length; i++) {
                single_Hypos.add(Integer.parseInt(curr2[i]));
            }

//            for (String s : synMap.get(hyp_Head)) {
//                for (int i : transformed(s)) {
//                    if (hypMap.containsKey(i)) {
//                        TreeSet<Integer> extra = hypMap.get(i);
//                        for (Integer n : extra) {
//                            single_Hypos.add(n);
//                        }
//                    }
//                }
//            }
            if(hypMap.containsKey(hyp_Head)) {
                HashSet<Integer> child = hypMap.get(hyp_Head);
                for (Integer i:child)
                single_Hypos.add(i);
            }
            hypMap.put(hyp_Head, single_Hypos);
        }
    }
    public HashSet<Integer> transformed(String word) {
        HashSet<Integer> res = new HashSet<>();
        for (int i:synMap.keySet()) {
            if(synMap.get(i).contains(word)){
                res.add(i);
            }
        }
       return res;


    }
    }


