package ngordnet.ngrams;

import java.util.*;

public class DFSGraph extends NGram2{
    boolean marked[];
    int edgeTo[];
    TreeSet<Integer> traversed = new TreeSet<>();

    public DFSGraph(String synsetFilename, String hyponymFilename) {
        super(synsetFilename, hyponymFilename);
        if (synMap != null) {
            int V = synMap.size();
            marked = new boolean[V];
            for (int i = 0; i < marked.length; i++) {
                marked[i] = false;
            }
//            edgeTo = new int[V];
        }
    }

    public TreeSet<Integer> dfs(int s) {
        if(!marked[s]) {
            marked[s] = true;
            traversed.add(s);
            if(hypMap.get(s)!=null){
                HashSet<Integer> children = hypMap.get(s);
                for (int i:children) {
                    dfs(i);
                }
            }
        }
        return traversed;
    }
    public void clearTraversed() {
        traversed = new TreeSet<>();
        for (int i = 0; i < marked.length; i++) {
            marked[i] = false;
        }
    }
}
