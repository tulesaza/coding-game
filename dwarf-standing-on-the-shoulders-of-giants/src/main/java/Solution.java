import java.util.*;


import java.io.*;
import java.math.*;


class Solution {

    public static void main(String args[]) {

        Graph g = new Graph();
        try (Scanner in = new Scanner(System.in)) {
            int n = in.nextInt(); // the number of relationships of influence
            for (int i = 0; i < n; i++) {
                int x = in.nextInt(); // a relationship of influence between two people (x influences y)
                int y = in.nextInt();
                g.readEdge(x, y);
            }
        }

        System.err.println(g.graph);

        System.out.println(g.findLongestPath());
    }

    static class Graph {
        final Map<Integer, Set<Integer>> graph = new HashMap<>();
        final HashMap<Integer,Integer> longesPathFrom = new HashMap<>();


        public void readEdge(int x, int y) {
            if (graph.containsKey(x)) {
                graph.get(x).add(y);
            } else {
                Set<Integer> a = new HashSet<>();
                a.add(y);
                graph.put(x, a);
            }
        }

        public int findLongestPath() {

            int max = 0;
            for (int vertex: graph.keySet()) {
                max = Math.max(max, findLongestPathForCurrent(vertex));
            }

            return max;
        }


        private int findLongestPathForCurrent(int vertex) {

            //already cached
            if(longesPathFrom.containsKey(vertex)){
                return longesPathFrom.get(vertex);
            }

            LinkedList<LinkedList<Integer>> ancestorPaths = new LinkedList<>();
            LinkedList<Integer> currentAncestorPath;
            // add root
            ancestorPaths.push(new LinkedList<>(Collections.singleton(vertex)));
            // paths from root exist
            while (!ancestorPaths.isEmpty()){
                // get last path
                currentAncestorPath = ancestorPaths.pop();
                // get last parent
                int last = currentAncestorPath.getFirst();

                // put to cache
                if (longesPathFrom.containsKey(last)) {
                    int path = longesPathFrom.get(last);
                    for (int i = 1; i < currentAncestorPath.size(); i++) {
                        int maxPathFor = Math.max(longesPathFrom.getOrDefault(currentAncestorPath.get(i),0),path+i);
                        longesPathFrom.put(currentAncestorPath.get(i),maxPathFor);
                    }
                    continue;
                }

                //has child, add to all possible paths
                if(graph.containsKey(last)){
                    for (int child: graph.get(last)) {
                        LinkedList<Integer> tmp = new LinkedList<>(currentAncestorPath);
                        tmp.push(child);
                        ancestorPaths.push(tmp);
                    }
                }
                // put to cache
                else {
                    int path = 1;
                    for (int i = 0; i < currentAncestorPath.size(); i++) {
                        int maxPathFor = Math.max(longesPathFrom.getOrDefault(currentAncestorPath.get(i),0), path+i);
                        longesPathFrom.put(currentAncestorPath.get(i),maxPathFor);
                    }
                }
            }

            return longesPathFrom.get(vertex);
        }


    }


}