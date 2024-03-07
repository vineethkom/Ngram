package ngordnet.ngrams;

import edu.princeton.cs.algs4.In;

import java.util.Collection;
import java.util.HashMap;


/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 * <p>
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {

    private HashMap<String, TimeSeries> ngMap;
    private TimeSeries totalData;

    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     */
    public NGramMap(String wordsFilename, String countsFilename) {
        ngMap = new HashMap<>();
        In in = new In(wordsFilename);
        String currLine = in.readLine();
        while (currLine != null) {
            String[] items = currLine.split("\\t");
            String word = items[0];
            int year = Integer.parseInt(items[1]);
            double data = Double.parseDouble(items[2]);
            TimeSeries ts = new TimeSeries();
            if (ngMap.get(word) == null) {
                ngMap.put(word, new TimeSeries());
            }
            ngMap.get(word).put(year, data);
            currLine = in.readLine();
        }
        totalData = new TimeSeries();
        In inTotal = new In(countsFilename);
        String currLine2 = inTotal.readLine();
        while (currLine2 != null) {
            String[] items2 = currLine2.split(",");
            int year = Integer.parseInt(items2[0]);
            double data = Double.parseDouble(items2[1]);
            totalData.put(year, data);
            currLine2 = inTotal.readLine();
        }

    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy,
     * not a link to this NGramMap's TimeSeries. In other words, changes made
     * to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy".
     */
    public TimeSeries countHistory(String word) {
        TimeSeries curr = ngMap.get(word);
        return new TimeSeries(curr, curr.firstKey(), curr.lastKey());
    }

    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other words,
     * changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy".
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        TimeSeries curr = ngMap.get(word);
        return new TimeSeries(curr, startYear, endYear);
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        return new TimeSeries(totalData, totalData.firstKey(), totalData.lastKey());
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to
     * all words recorded in that year.
     */
    public TimeSeries weightHistory(String word) {
        TimeSeries ts = new TimeSeries();
        TimeSeries curr = ngMap.get(word);
        for (int i = curr.firstKey(); i <= curr.lastKey(); i++) {
            if (curr.containsKey(i)) {
                ts.put(i, (curr.get(i) / totalData.get(i)));
            }
        }
        return ts;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        TimeSeries ts = new TimeSeries();
        TimeSeries curr = ngMap.get(word);
        for (int i = startYear; i <= endYear; i++) {
            if (curr.containsKey(i)) {
                ts.put(i, (curr.get(i) / totalData.get(i)));
            }
        }
        return ts;
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        TimeSeries ts = new TimeSeries();
        for (String s : words) {
            TimeSeries weightedSeries = weightHistory(s);
            if (ts.isEmpty()) {
                ts = weightedSeries;
            } else {
                ts = ts.plus(weightedSeries);
            }
        }
        return ts;
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS
     * between STARTYEAR and ENDYEAR, inclusive of both ends. If a word does not exist in
     * this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words, int startYear, int endYear) {
        TimeSeries ts = new TimeSeries();
        for (String s : words) {
            //TimeSeries wordSeries = NgMap.get(s);
            TimeSeries weightedSeries = weightHistory(s, startYear, endYear);
            if (ts.isEmpty()) {
                ts = weightedSeries;
            } else {
                ts = ts.plus(weightedSeries);
            }
        }
        return ts;
    }


}

