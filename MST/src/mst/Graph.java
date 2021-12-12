/*
 * Copyright (C) 2019 brahim
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package mst;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import javafx.collections.FXCollections;

/**
 *
 * @author brahim remmouche
 */
public class Graph {
    
    private String name, creator;
    private int [] edges[], terminals, coordinates[], graphCoordinate[];
    private int NODES_NUMBER, EDGES_NUMBER, TERMINAlS_NUMBER;
    private int MAX_COORDINATE_WIDTH = 0, MAX_COORDINATE_HEIGHT = 0;
    private HashSet<Integer> steinerNodes = new HashSet<>();

    public Graph() {
    }
    
    public Graph(File file) throws Exception {
        scanFile(file);
        if ( edges == null || coordinates == null ) {
            throw new Exception();
        }
    }
    
    private void scanFile(File file) throws Exception {
        Scanner fileIn = new Scanner(file);
        while ( fileIn.hasNext() ) {
            String nextLine = fileIn.nextLine();
            if ( nextLine.split(" ")[0].equals("SECTION") ) {
                String next;
                int i;
                switch (nextLine.split(" ")[1]) {
                    case "Comment":
                        next = fileIn.nextLine();
                        while ( ! next.equals("END") ) {                            
                            if ( next.split(" ")[0].equals("Name") ) {
                                this.name = next.split("\"")[1];
                            } else if ( next.split(" ")[0].equals("Creator") ) {
                                this.creator = next.split("\"")[1];
                            }
                            next = fileIn.nextLine();
                        }
                        break;
                    case "Graph":
                        next = fileIn.nextLine();
                        i = 0;
                        while ( ! next.equals("END") ) {                            
                            switch (next.split(" ")[0]) {
                                case "Nodes":
                                    this.NODES_NUMBER = Integer.parseInt(next.split(" ")[1]);
                                    break;
                                case "Edges":
                                    this.EDGES_NUMBER = Integer.parseInt(next.split(" ")[1]);
                                    this.edges = new int[this.EDGES_NUMBER][3];
                                    break;
                                case "E":
                                    this.edges[i][0] = Integer.parseInt(next.split(" ")[1]);
                                    this.edges[i][1] = Integer.parseInt(next.split(" ")[2]);
                                    this.edges[i][2] = Integer.parseInt(next.split(" ")[3]);
                                    i++;
                                    break;
                            }
                            next = fileIn.nextLine();
                        }
                        break;
                    case "Terminals":
                        next = fileIn.nextLine();
                        i = 0;
                        while ( ! next.equals("END") ) {                            
                            if ( next.split(" ")[0].equals("Terminals") ) {
                                this.TERMINAlS_NUMBER = Integer.parseInt(next.split(" ")[1]);
                                this.terminals = new int[this.TERMINAlS_NUMBER];
                            } else if ( next.split(" ")[0].equals("T") ) {
                                this.terminals[i] = Integer.parseInt(next.split(" ")[1]);
                                i++;
                            }
                            next = fileIn.nextLine();
                        }
                        break;
                    case "Coordinates":
                        this.coordinates = new int[this.NODES_NUMBER][3];
                        this.graphCoordinate = new int[this.NODES_NUMBER][3];
                        next = fileIn.nextLine();
                        while ( ! next.equals("END") ) {                            
                            if ( next.split(" ")[0].equals("DD") ) {
                                i = Integer.parseInt(next.split(" ")[1])-1;
                                this.coordinates[i][0] = Integer.parseInt(next.split(" ")[1]);
                                this.graphCoordinate[i][0] = Integer.parseInt(next.split(" ")[1]);
                                this.steinerNodes.add(this.coordinates[i][0]);
                                this.coordinates[i][1] = Integer.parseInt(next.split(" ")[2]);
                                this.coordinates[i][2] = Integer.parseInt(next.split(" ")[3]);
                                if ( this.coordinates[i][1] > this.MAX_COORDINATE_WIDTH ) {
                                    this.MAX_COORDINATE_WIDTH = this.coordinates[i][1];
                                }
                                if ( this.coordinates[i][2] > this.MAX_COORDINATE_HEIGHT ) {
                                    this.MAX_COORDINATE_HEIGHT = this.coordinates[i][2];
                                }
                            }
                            next = fileIn.nextLine();
                        }
                        break;
                }
            }
        }
    }

    public String getName() {
        return name;
    }

    public String getCreator() {
        return creator;
    }

    public int[][] getGraph() {
        return edges;
    }

    public int[] getTerminals() {
        return terminals;
    }

    public int[][] getCoordinates() {
        return coordinates;
    }

    public int getNodes_number() {
        return NODES_NUMBER;
    }

    public int getEdges_number() {
        return EDGES_NUMBER;
    }

    public int getTerminals_number() {
        return TERMINAlS_NUMBER;
    }

    public int getMaxCoordinateWidth() {
        return MAX_COORDINATE_WIDTH;
    }

    public int getMaxCoordinateHeight() {
        return MAX_COORDINATE_HEIGHT;
    }
    
    private boolean isTerminal(int node) {
        for (int n : this.terminals) {
            if ( node == n ) {
                return true;
            }
        }
        return false;
    }
    
    private int cost(int n1, int n2) {
        for (int i = 0; i < this.EDGES_NUMBER; i++) {
            if ( (this.edges[i][0] == n1 && this.edges[i][1] == n2) || (this.edges[i][0] == n2 && this.edges[i][1] == n1) ) {
                return this.edges[i][2];
            }
        }
        return 0;
    }
    
    public int[][] getGraphCoordinate(int width, int height, int margin) {
        for (int i = 0; i < this.NODES_NUMBER; i++) {
            graphCoordinate[i][1] = (int)(coordinates[i][1]*(width-2*margin)/MAX_COORDINATE_WIDTH) + margin;
            graphCoordinate[i][2] = (int)(coordinates[i][2]*(height-2*margin)/MAX_COORDINATE_HEIGHT) + margin;
        }
        return graphCoordinate;
    }
    
    public int [] getNodeCoordinate(int node) {
        int [] coordinate = new int[2];
        try {
            if ( this.graphCoordinate[node-1][0] == node ) {
                coordinate[0] = this.graphCoordinate[node-1][1];
                coordinate[1] = this.graphCoordinate[node-1][2];
                return coordinate;
            }
        } catch (Exception e) {
        }
        for (int[] elem : this.graphCoordinate) {
            if ( elem[0] == node ) {
                coordinate[0] = elem[1];
                coordinate[1] = elem[2];
                return coordinate;
            }
        }
        return coordinate;
    }
    
    public Graph kmb() {
        Graph graph = completeGraph(this);
        //graph.edges = Graph.kruskal(graph);
        //graph.edges = Graph.prime(graph);
        //graph.EDGES_NUMBER = graph.edges.length;
        //System.out.println("Delete the extra nodes");
        //graph = completeGraph(graph);
        return graph;
    }
    
    
    /* Source : https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
        1  function Dijkstra(Graph, source):
        2
        3      create vertex set Q
        4
        5      for each vertex v in Graph:             
        6          dist[v] ← INFINITY                  
        7          prev[v] ← UNDEFINED                 
        8          add v to Q                      
       10      dist[source] ← 0                        
       11      
       12      while Q is not empty:
       13          u ← vertex in Q with min dist[u]    
       14                                              
       15          remove u from Q 
       16          
       17          for each neighbor v of u:           // only v that are still in Q
       18              alt ← dist[u] + length(u, v)
       19              if alt < dist[v]:               
       20                  dist[v] ← alt 
       21                  prev[v] ← u 
       22
       23      return dist[], prev[]
    */
    
    /**
     * Dijkstra method return a short path between the node and the other nodes.
     * @param graph
     * @param source
     * @param otherNode
     * @return  {@code HashMap<Integer, Integer[]>} that contains node and source and cost: 
     *          {@code HashMap<node, [source, cost]>} .
     */
    public static HashMap<Integer, Integer[]> dijkstra(Graph graph, int source, HashSet<Integer> otherNode) {
        //long start = System.currentTimeMillis();
        HashMap<Integer, HashSet<Integer[]>> neighbor = new HashMap<>();
        HashMap<Integer, Integer[]> dijkstra = new HashMap<>();
        //HashSet<Integer> nodeCalculated = new HashSet<>();
        HashSet<Integer> nodeModified = new HashSet<>();
        HashSet<Integer> Q = new HashSet<>(graph.steinerNodes);
        for (int i = 0; i < graph.NODES_NUMBER; i++) {
            neighbor.put(graph.coordinates[i][0], new HashSet<>());
            dijkstra.put(graph.coordinates[i][0], new Integer[]{-1, Integer.MAX_VALUE});
        }
        for (int[] edge : graph.edges) {
            neighbor.get(edge[0]).add(new Integer[]{edge[1], edge[2]});
            neighbor.get(edge[1]).add(new Integer[]{edge[0], edge[2]});
        }
        Q.add(source);
        dijkstra.get(source)[1] = 0;
        nodeModified.add(source);
        int u;
        while ( ! Q.isEmpty() /*&& ! nodeCalculated.containsAll(otherNode)*/ ) {   
            u = Q.iterator().next();
            for (Integer q : nodeModified) {
                if ( dijkstra.get(u)[1] > dijkstra.get(q)[1] ) {
                    u = q;
                }
            }
            Q.remove(u);
            nodeModified.remove(u);
            //nodeCalculated.add(u);
            for (Integer[] v : neighbor.get(u)) {
                int alt = dijkstra.get(u)[1] + v[1];
                if ( alt < dijkstra.get(v[0])[1] ) {
                    dijkstra.get(v[0])[1] = alt;
                    dijkstra.get(v[0])[0] = u;
                    nodeModified.add(v[0]);
                }
            }
        }
        //long end = System.currentTimeMillis();
        //System.out.println("Dijkstra Terminal : "+source+" -> "+(end - start) / 1000F + " seconds");
        return dijkstra;
    }
    
    public static Graph completeGraph(Graph graph) {
        HashMap<String, ArrayList<Integer[]>> pathsTerminals = new HashMap<>();
        ArrayList<Object[]> costs = new ArrayList<>();
        int k = 0;
        for (int i = 0; i < graph.TERMINAlS_NUMBER-1; i++) {
            HashSet<Integer> otherNode = new HashSet<>();
            //for (int j = i+1; j < graph.TERMINAlS_NUMBER; j++) {otherNode.add(graph.terminals[j]);}
            HashMap<Integer, Integer[]> dijkstra = Graph.dijkstra(graph, graph.terminals[i], otherNode);
            for (int j = i+1; j < graph.TERMINAlS_NUMBER; j++) {
                String key = String.valueOf(graph.terminals[i])+","+String.valueOf(graph.terminals[j]);
                int node = graph.terminals[j];
                Integer[] d = dijkstra.get(node);
                int cost = 0;
                ArrayList<Integer[]> shortPathEdges = new ArrayList<>();
                while ( d[0] != -1 ) {                    
                    Integer[] edg;
                    if (d[0] < node) {
                        edg = new Integer[]{d[0], node, d[1]-dijkstra.get(d[0])[1]};
                    } else {
                        edg = new Integer[]{node, d[0], d[1]-dijkstra.get(d[0])[1]};
                    }
                    cost += edg[2];
                    shortPathEdges.add(edg);
                    node = d[0];
                    d = dijkstra.get(node);
                }
                costs.add(new Object[]{cost ,key});
                pathsTerminals.put(key, shortPathEdges);
            }
        }
        ArrayList<Integer[]> completeGraphEdges = Graph.makeTree(graph, pathsTerminals, costs);
        HashSet<Integer> completeGraphNodes = new HashSet<>();
        int [][] edges = new int[completeGraphEdges.size()][3];
        int i = 0;
        for (Integer[] elem : completeGraphEdges) {
            edges[i][0] = elem[0];
            edges[i][1] = elem[1];
            edges[i++][2] = elem[2];
            completeGraphNodes.add(elem[0]);
            completeGraphNodes.add(elem[1]);
        }
        
        return createNewGraph(graph, edges, completeGraphNodes);
    }
    
    private static ArrayList<Integer[]> makeTree(Graph graph, HashMap<String, ArrayList<Integer[]>> pathsTerminals, ArrayList<Object[]> costs) {
        costs.sort((Object[] o1, Object[] o2) -> (Integer)o1[0]-(Integer)o2[0]);
        int LinkedTerminal = 0;
        ArrayList<Integer[]> completeGraphEdges = new ArrayList<>();
        HashMap<Integer, HashSet<Integer>> P = new HashMap<>();
        for (int i = 0; i < graph.TERMINAlS_NUMBER; i++) P.put(graph.terminals[i], new HashSet<>(FXCollections.observableArrayList(graph.terminals[i])));
        for (Object[] cost : costs) {
            int n1 = Integer.parseInt(String.valueOf(cost[1]).split(",")[0]);
            int n2 = Integer.parseInt(String.valueOf(cost[1]).split(",")[1]);
            if ( ! hashSetContains(P.get(n1), P.get(n2)) ) {
                P.get(n1).addAll(P.get(n2));
                HashSet<Integer> set = P.get(n1);
                set.forEach((elem) -> {P.put(elem, set);});
                for (Integer[] elem : pathsTerminals.get(String.valueOf(cost[1]))) {
                    boolean b = false;
                    for (Integer[] e : completeGraphEdges) {
                        if ( Arrays.equals(elem, e) ) {
                            b = true;
                            break;
                        }
                    }
                    if (!b) {
                        completeGraphEdges.add(elem);
                    }
                }
                LinkedTerminal++;
            }
            if (LinkedTerminal+1 == graph.TERMINAlS_NUMBER) break;
        }
        return completeGraphEdges;
    }
    
    private static boolean hashSetContains(HashSet<Integer> set1, HashSet<Integer> set2) {
        if ( set1.size() > set2.size() ) {
            HashSet<Integer> set3 = set1;
            set1 = set2;
            set2 = set3;
        }
        for (Integer elem : set1) {
            if ( set2.contains(elem) ) {
                return true;
            }
        }
        return false;
    }
    
    public static Graph createNewGraph(Graph graph, int [][] edges, HashSet<Integer> steinerNodes) {
        Graph g = new Graph();
        g.edges = edges;
        g.EDGES_NUMBER = edges.length;
        g.NODES_NUMBER = graph.NODES_NUMBER;
        g.TERMINAlS_NUMBER = graph.TERMINAlS_NUMBER;
        g.coordinates = new int[graph.NODES_NUMBER][3];
        g.terminals = new int[graph.TERMINAlS_NUMBER];
        g.graphCoordinate = new int[graph.NODES_NUMBER][3];
        g.MAX_COORDINATE_WIDTH = graph.MAX_COORDINATE_WIDTH;
        g.MAX_COORDINATE_HEIGHT = graph.MAX_COORDINATE_HEIGHT;
        g.coordinates = Arrays.copyOf(graph.coordinates, graph.coordinates.length);
        g.graphCoordinate = Arrays.copyOf(graph.graphCoordinate, graph.graphCoordinate.length);
        g.terminals = Arrays.copyOf(graph.terminals, graph.terminals.length);
        g.steinerNodes = steinerNodes;
        return g;
    }
    
    public static int [][] kruskal(Graph graph) {
        long start = System.currentTimeMillis();
        int [][] newEdges = new int[graph.NODES_NUMBER-1][3];
        HashMap<Integer, HashSet<Integer>> P = new HashMap<>();
        for (int i = 0; i < graph.NODES_NUMBER; i++) {
            P.put(graph.coordinates[i][0], new HashSet<>(FXCollections.observableArrayList(graph.coordinates[i][0])));
        }
        int k = 0;
        for (int[] edge : graph.edges) {
            if ( ! hashSetContains(P.get(edge[0]), P.get(edge[1])) ) {
                P.get(edge[0]).addAll(P.get(edge[1]));
                HashSet<Integer> set = P.get(edge[0]);
                set.forEach((elem) -> {P.put(elem, set);});
                newEdges[k][0] = edge[0];
                newEdges[k][1] = edge[1];
                newEdges[k][2] = edge[2];
                k++;
                if ( k == graph.NODES_NUMBER-1 ) {
                    break;
                }
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("Kruscal time -> "+(end - start) / 1000F + " seconds");
        int [][] B = Arrays.copyOf(newEdges, k);
        return B;
    }
    
    public static int [][] kruskal2(Graph graph) {
        long start = System.currentTimeMillis();
        int [][] A = new int[graph.NODES_NUMBER-1][3];
        int [][] E = Arrays.copyOf(graph.edges, graph.edges.length);
        //Graph.sort(E, 2);
        long end1 = System.currentTimeMillis();
        System.out.println("Sort time -> "+(end1 - start) / 1000F + " seconds");
        int k = 0;
        for (int i = 0; i < E.length; i++) {
            if ( ! Graph.cycle(E[i][0], E[i][1], -1, A, k) ) {
                A[k][0] = E[i][0];
                A[k][1] = E[i][1];
                A[k][2] = E[i][2];
                k++;
                if ( k == graph.NODES_NUMBER-1 ) {
                    break;
                }
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("Kruscal time -> "+(end - start) / 1000F + " seconds");
        int [][] B = Arrays.copyOf(A, k);
        return B;
    }
    
    private static boolean cycle(int nodeSource, int nodeDestination, int root, int [][] E, int k) {
        for (int i = 0; i < k; i++) {
            if (( E[i][1] == nodeSource && E[i][0] == nodeDestination) || (E[i][0] == nodeSource && E[i][1] == nodeDestination )) {
                return true;
            } else {
                if ( E[i][0] == nodeSource && E[i][1] != root && cycle(E[i][1], nodeDestination, nodeSource, E, k) ) {
                    return true;
                }
                if ( E[i][1] == nodeSource && E[i][0] != root && cycle(E[i][0], nodeDestination, nodeSource, E, k) ) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private static void sort(int [][] A, int col) {
        for (int i = 0; i < A.length-1; i++) {
            int min = i;
            for (int j = i+1; j < A.length; j++) {
                if ( A[j][col] < A[min][col] ) {
                    min = j;
                }
            }
            if ( min != i ) {
                int [] c = A[i];
                A[i] = A[min];
                A[min] = c;
            }
        }
    }
    
    private static Set<Integer> findSet(int node, int root, int [][] E, int k) {
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < k; i++) {
            if ( E[i][0] == node && E[i][1] != root ) {
                set.add(E[i][1]);
                set.addAll(findSet(E[i][1], node, E, k));
            }
            if ( E[i][1] == node && E[i][0] != root ) {
                set.add(E[i][0]);
                set.addAll(findSet(E[i][0], node, E, k));
            }
        }
        return set;
    }
    
    /**
     * Prime method return a minimal covering tree.
     * @param graph an {@code Graph}.
     * @return {@code int[][]} that contains the edges
     * of the minimal coverting tree from the graph.
     */
    public static int [][] prime(Graph graph) {
        long start = System.currentTimeMillis();
        Integer [] nodes = graph.steinerNodes.toArray(new Integer[0]);
        int [] roots, costs;
        boolean [] isAdd;
        roots = new int[nodes.length];
        costs = new int[nodes.length];
        isAdd = new boolean[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            costs[i] = Integer.MAX_VALUE;
            isAdd[i] = false;
        }
        roots[0] = -1;
        costs[0] = 0;
        for (int i = 0; i < nodes.length-1; i++) {
            int min = Integer.MAX_VALUE;
            int index = -1;
            for (int j = 0; j < nodes.length; j++) {
                if ( isAdd[j] == false && costs[j] < min ) {
                    min = costs[j];
                    index = j;
                }
            }
            isAdd[index] = true;
            for (int j = 0; j < nodes.length; j++) {
                int cost = graph.cost(nodes[index], nodes[j]);
                if ( isAdd[j] == false && cost != 0 && cost < costs[j] ) {
                    roots[j] = nodes[index];
                    costs[j] = cost;
                }
            }
        }
        int [][] edgesCoveringTree = new int[nodes.length-1][3];
        for (int i = 0; i < nodes.length-1; i++) {
            edgesCoveringTree[i][0] = nodes[i+1];
            edgesCoveringTree[i][1] = roots[i+1];
            edgesCoveringTree[i][2] = costs[i+1];
        }
        long end = System.currentTimeMillis();
        System.out.println("Prim time -> "+(end - start) / 1000F + " seconds");
        return edgesCoveringTree;
    }
    
}