package ngordnet.main;

import java.util.*;

public class Graph {

    private Map<Node, List<Node>> map = new HashMap<>();

    private HashMap<String, ArrayList<Graph.Node>> stringTree = new HashMap<>();
    private HashMap<Integer, ArrayList<Graph.Node>> idTree = new HashMap<>();


    public class Node {

        public ArrayList<Node> hyponyms;
        public int key;
        public String val;

        private Node(int key, String val) {
            this.key = key;
            this.val = val;
            this.hyponyms = new ArrayList<>();
        }
    }

    public void createNode(int key, String name) {
        Node node = new Node(key, name);
        addNode(node);
    }

    public void addNode(Node n) {
        if (stringTree.containsKey(n.val)) {
            ArrayList<Node> nodes = stringTree.get(n.val);
            nodes.add(n);
        } else {
            ArrayList<Node> nodes2 = new ArrayList<>();
            nodes2.add(n);
            stringTree.put(n.val, nodes2);
            //System.out.println(n.val);
        }

        if (idTree.containsKey(n.key)) {
            ArrayList<Node> nodes = idTree.get(n.key);
            nodes.add(n);
        } else {
            ArrayList<Node> nodes2 = new ArrayList<>();
            nodes2.add(n);
            idTree.put(n.key, nodes2);
        }

    }

    public void addChild(Node parent, Node child) {
        parent.hyponyms.add(child);
    }

    public void addChildren(Node parent, ArrayList<Node> children) {
        parent.hyponyms.addAll(children);
    }

    public ArrayList<Node> getByID(int key) {
        return idTree.get(key);
    }

    public ArrayList<Node> getByWord(String word) {
        return stringTree.get(word);
    }

    public void DepthFirstSearch(HashSet<String> lst, Node n) {
        ArrayList<Node> syns = getByID(n.key);
        //System.out.println(n.val);
        for (int i = 0; i < syns.size(); i++) {
            Node node = syns.get(i);
            lst.add(node.val);
        }
        for (int i = 0; i < n.hyponyms.size(); i++) {
            Node child = n.hyponyms.get(i);
            DepthFirstSearch(lst, child);
        }
    }

    public HashSet<String> childWords(String word) {
        ArrayList<Node> words = this.stringTree.get(word);
        HashSet<String> children = new HashSet<>();
        if (words != null) {
            for (int i = 0; i < words.size(); i++) {
                Node n = words.get(i);
                //System.out.println(n.val);
                DepthFirstSearch(children, n);
            }
        }
        return children;
    }


}
