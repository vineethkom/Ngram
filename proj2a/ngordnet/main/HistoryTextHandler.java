package ngordnet.main;

import ngordnet.ngrams.NGramMap;
import ngordnet.hugbrowsermagic.NgordnetQuery;
import ngordnet.hugbrowsermagic.NgordnetQueryHandler;

import java.util.List;

public class HistoryTextHandler extends NgordnetQueryHandler {

    private NGramMap ngMap;

    public HistoryTextHandler(NGramMap map) {
        super();
        ngMap = map;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();
        String response = "";
        for (String word : words) {
            response += word + ": " + ngMap.weightHistory(word, startYear, endYear).toString() + "\n";
        }
        return response;
    }
}
