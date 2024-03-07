package ngordnet.main;

import ngordnet.hugbrowsermagic.HugNgordnetServer;
import ngordnet.ngrams.NGramMap;

public class Main {
    public static void main(String[] args) {
        HugNgordnetServer hns = new HugNgordnetServer();
        String wordFile = "./data 2/ngrams/top_49887_words.csv";
        String countFile = "./data 2/ngrams/total_counts.csv";

        String synsetFile = "./data 2/wordnet/synsets.txt";
        String hyponymFile = "./data 2/wordnet/hyponyms.txt";

       NGramMap ngm = new NGramMap(wordFile, countFile);
       WordNet wm = new WordNet(synsetFile, hyponymFile, ngm);

        hns.startUp();
        hns.register("history", new HistoryHandler(ngm));
        hns.register("historytext", new HistoryTextHandler(ngm));
        hns.register("hyponyms", new HyponymsHandler(wm));

    }
}
