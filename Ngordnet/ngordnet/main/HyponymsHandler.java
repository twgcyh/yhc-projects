package ngordnet.main;

import ngordnet.browser.NgordnetQuery;
import ngordnet.browser.NgordnetQueryHandler;
import ngordnet.ngrams.DFSGraph;
import ngordnet.ngrams.NGramMap;
import ngordnet.ngrams.TimeSeries;

import java.util.*;

public class HyponymsHandler extends NgordnetQueryHandler {

    DFSGraph targetG;
    NGramMap targetMap;

    public HyponymsHandler(DFSGraph g, NGramMap ngram) {
        targetG = g;
        targetMap = ngram;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> filStringTree = new ArrayList<>();
        for (String word : q.words()) {
            HashSet<Integer> matchInt = targetG.transformed(word);
            HashSet<Integer> result = new HashSet<>();
            targetG.clearTraversed();
            for (int i : matchInt) {
                for (int j : targetG.dfs(i)) {
                    result.add(j);
                }
            }
            List<String> filResultT = new ArrayList<>();
            HashSet<String> filResultTSet = new HashSet<>();
            for (int i : result) {
                for (String w : targetG.synMap.get(i)) {
                    if (!filResultTSet.contains(w)) {
                        filResultT.add(w);
                        filResultTSet.add(w);
                    }
                }
            }
            if (filStringTree.size() == 0) {
                filStringTree = filResultT;
            } else {
                filStringTree.retainAll(filResultT);
                if (filStringTree.size() == 0) {
                    break;
                }
            }
        }
        String response = "[";
        if (q.k() > 0) {
            TreeMap<Double, String> compaTree = new TreeMap<>(Collections.reverseOrder());
            TreeSet<String> finalTree = new TreeSet<>();
            for (String word : filStringTree) {
                TimeSeries countSeries = targetMap.countHistory(word, q.startYear(), q.endYear());
                double sumSingle = 0;
                for (Double i : countSeries.values()) {
                    sumSingle = sumSingle + i;
                }
                if (sumSingle > 0) {
                    compaTree.put(sumSingle, word);
                }
            }
            for (int i = 0; i < q.k(); i++) {
                if (i == compaTree.size()) {
                    break;
                }
                int count = 0;
                List<String> valueCopy = new ArrayList<>(compaTree.values());
                for (String tar : valueCopy) {
                    finalTree.add(tar);
                    count = count + 1;
                    if (count == q.k()) {
                        break;
                    }
                }
                for (String w : finalTree) {
                    response = response + w + "," + " ";
                }
                if (response.length() != 1) {
                    response = response.substring(0, response.length() - 2) + "]";
                } else {
                    response += "]";
                }
                return response;
            }
            return response + "]";
        }
        TreeSet<String> simpleT = new TreeSet<>();
        for (String e : filStringTree) {
            simpleT.add(e);
        }
        response = concate(simpleT);
        return response;
    }
    public String concate(TreeSet<String> responseSet) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (String w : responseSet) {
            sb.append(w).append(", ");
        }
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2); // remove the last ", "
        }
        sb.append("]");
        return sb.toString();
    }
}

