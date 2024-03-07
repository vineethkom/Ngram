package ngordnet.ngrams;

import java.util.*;

/**
 * An object for mapping a year number (e.g. 1996) to numerical data. Provides
 * utility methods useful for data analysis.
 *
 * @author Josh Hug
 */
public class TimeSeries extends TreeMap<Integer, Double> {
    /**
     * Constructs a new empty TimeSeries.
     */
    public TimeSeries() {
        super();
    }

    /**
     * Creates a copy of TS, but only between STARTYEAR and ENDYEAR,
     * inclusive of both end points.
     */
    public TimeSeries(TimeSeries ts, int startYear, int endYear) {
        super();
        for (int k = startYear; k <= endYear; k++) {
            if (ts.containsKey(k)) {
                this.put(k, ts.get(k));
            }
        }
    }


    /**
     * Returns all years for this TimeSeries (in any order).
     */
    public List<Integer> years() {
        //return (List<Integer>) this.keySet();
        List<Integer> ls = new ArrayList<>();
        for (int k : this.keySet()) {
            ls.add(k);
        }
        return ls;
    }

    /**
     * Returns all data for this TimeSeries (in any order).
     * Must be in the same order as years().
     */
    public List<Double> data() {
        //return (List<Double>) this.values();
        List<Double> ls = new ArrayList<>();
        for (double v : this.values()) {
            ls.add(v);
        }
        return ls;
    }

    /**
     * Returns the yearwise sum of this TimeSeries with the given TS. In other words, for
     * each year, sum the data from this TimeSeries with the data from TS. Should return a
     * new TimeSeries (does not modify this TimeSeries).
     */
    public TimeSeries plus(TimeSeries ts) {
        TimeSeries plusTs = new TimeSeries();
        int first = 0;
        int last = 0;
        if (this.firstKey() > ts.firstKey()) {
            first = ts.firstKey();
        } else {
            first = this.firstKey();
        }
        if (this.lastKey() > ts.lastKey()) {
            last = this.lastKey();
        } else {
            last = ts.lastKey();
        }
        for (int k = first; k <= last; k++) {
            if (this.containsKey(k) && ts.containsKey(k)) {
                double added = this.get(k) + ts.get(k);
                plusTs.put(k, added);
            }
            if (this.containsKey(k) && !ts.containsKey(k)) {
                plusTs.put(k, this.get(k));
            }
            if (ts.containsKey(k) && !this.containsKey(k)) {
                plusTs.put(k, ts.get(k));
            }
        }
        return plusTs;
    }

    /**
     * Returns the quotient of the value for each year this TimeSeries divided by the
     * value for the same year in TS. If TS is missing a year that exists in this TimeSeries,
     * throw an IllegalArgumentException. If TS has a year that is not in this TimeSeries, ignore it.
     * Should return a new TimeSeries (does not modify this TimeSeries).
     */
    public TimeSeries dividedBy(TimeSeries ts) {
        TimeSeries divideTs = new TimeSeries();
        for (int k : this.keySet()) {
            if (!ts.containsKey(k)) {
                throw new IllegalArgumentException();
            }
            double dval = this.get(k) / ts.get(k);
            divideTs.put(k, dval);
        }
        return divideTs;
    }
}

