package ngordnet.main;

import edu.princeton.cs.algs4.In;
import ngordnet.ngrams.NGramMap;
import ngordnet.ngrams.TimeSeries;

import java.util.*;

public class WordNet {

    private Graph gMap;
    private NGramMap nMap;
    //private Graph hypMap;


    public WordNet(String synsetFile, String HyponymFile, NGramMap nGramMap){
        gMap = new Graph();
        nMap = nGramMap;
        In in = new In(synsetFile);
        String currLine = in.readLine();
        while (currLine != null) {
            String[] items = currLine.split(",");
            int key = Integer.parseInt(items[0]);
            String[] words = items[1].split(" ");
            for (int i = 0; i < words.length; i++) {
                gMap.createNode(key, words[i]);
            }
            currLine = in.readLine();
        }

        In in2 = new In(HyponymFile);
        String currLine2 = in2.readLine();
        while(currLine2 != null) {
            String[] items = currLine2.split(",");
            int wordID = Integer.parseInt(items[0]);
            ArrayList<Graph.Node> nodes = gMap.getByID(wordID);
            ArrayList<Graph.Node> nodeChildren = new ArrayList<>();

            for(int i = 1; i < items.length; i += 1){
                int childID = Integer.parseInt(items[i]);
                nodeChildren.addAll(gMap.getByID(childID));
            }
            for(Graph.Node node : nodes){
                node.hyponyms.addAll(nodeChildren);
            }
            currLine2 = in2.readLine();
        }



    }

    public String allHyponyms(List<String> words, int k, int start, int end){
        HashSet<String> allWords = new HashSet<>();
        if (words.isEmpty()) {
            return "[]";
        }
        //System.out.println(gMap.getByWord(words.get(0)));
        allWords.addAll(gMap.childWords(words.get(0)));

        for (int i = 1; i < words.size(); i++) {
            allWords.retainAll(gMap.childWords(words.get(i)));
        }
        if (k == 0){
            return printList(sortSet(allWords));
        }


        HashMap<Double, ArrayList<String>> map = buildMap(new ArrayList<String> (allWords), start, end);

        List<Double> popList = new ArrayList<>(map.keySet());
        Collections.sort(popList);
        ArrayList<String> sortedWords = new ArrayList<>();

        for (int i = popList.size() - 1; i >= 0; i--) {
            sortedWords.addAll(map.get(popList.get(i)));
//            System.out.println(popList.get(i) + map.get(popList.get(i)).get(0));
            if (sortedWords.size() >= k ) {
                break;
            }
        }

        sortedWords.sort(String :: compareTo);

        if (k >= sortedWords.size()){
            return printList(sortedWords);
        }
//       return printList(sortedWords.subList(sortedWords.size() - k, sortedWords.size()));
       return printList(sortedWords.subList(0, k));
    }

    public HashMap<Double, ArrayList<String>> buildMap(ArrayList<String> words, int start, int end){
        HashMap<Double, ArrayList<String>> map = new HashMap<>();
        for (String word: words) {
            Double popularity = wordPopularity(word, start, end);
            if (popularity != 0) {
                if (map.containsKey(popularity)) {
                    map.get(popularity).add(word);
                } else {
                    ArrayList<String> lst = new ArrayList<>();
                    lst.add(word);
                    map.put(popularity, lst);
                }
            }
        }

        return map;
    }

    public Double wordPopularity(String word, int start, int end) {
        TimeSeries timeSeries = this.nMap.countHistory(word, start, end);
        Double sum = 0.0;
        for (Integer year: timeSeries.keySet()) {
            sum += timeSeries.get(year);
        }
        return sum;
    }





    public List<String> sortSet(HashSet<String> words){
        ArrayList<String> list = new ArrayList<>(words);
        list.sort(String :: compareTo);
        return list;
    }

    public List<String> sortSet(ArrayList<String> words){
        ArrayList<String> list = new ArrayList<>(words);
        list.sort(String :: compareTo);
        return list;
    }

    public String printList(List<String> words) {
        String str = "";
        if (words.isEmpty()){
            return "[]";
        }
        for (int i = 0; i < words.size() - 1; i ++) {
            str += words.get(i) + ", ";
        }
        str += words.get(words.size() - 1);
        return "[" + str + "]";
    }

}
