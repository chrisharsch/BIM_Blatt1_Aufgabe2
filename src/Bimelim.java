import net.gumbix.dynpro.DynProJava;
import net.gumbix.dynpro.Idx;
import net.gumbix.dynpro.PathEntry;
import scala.Function2;
import scala.Option;
import scala.Some;

import java.util.List;

/**
 * The Bimelim problem solved with dynamic programming.
 *
 * @author Markus Gumbel (m.gumbel@hs-mannheim.de)
 */
public class Bimelim extends DynProJava<Integer> {

    public static void main(String[] args) {
        String[] rowLabels = {"1", "3","5","6","9"};
        int[] werte = {1,3,5,6,9};
        int gesamtLaenge = 58;
        Bimelim dp = new Bimelim(rowLabels, werte, werte, gesamtLaenge);
        // The maximum is expected at the last item (n-1)
        // with no capacity left (0);
        List<PathEntry<Integer>> solutionJava = dp.solutionAsList(new Idx(dp.n() -1, 0));
        System.out.println("Optimal Decisions with length "+gesamtLaenge+":");
        for (int i = 0; i< rowLabels.length;i++){
            System.out.print(rowLabels[i]+" ");
        }
        System.out.print("\n");
        for (PathEntry<Integer> entry : solutionJava) {

            System.out.print(entry.decision() + " ");
        }
        System.out.println("\n");
        System.out.println(dp.mkMatrixString(dp.solution(new Idx(dp.n() - 1, 0))));


        String[] rowLabelsTwo = {"1", "3","9","15"};
        int[] werteTwo = {1,3,5,9,15};
        gesamtLaenge = 533;
        dp = new Bimelim(rowLabelsTwo, werteTwo, werteTwo, gesamtLaenge);
        // The maximum is expected at the last item (n-1)
        // with no capacity left (0);
        List<PathEntry<Integer>> solutionJavaTwo = dp.solutionAsList(new Idx(dp.n() -1, 0));
        System.out.println("Optimal Decisions with length "+gesamtLaenge+":");
        for (int i = 0; i< rowLabels.length;i++){
            System.out.print(rowLabels[i]+" ");
        }
        System.out.print("\n");
        for (PathEntry<Integer> entry : solutionJavaTwo) {
            System.out.print(entry.decision() + " ");
        }
        System.out.println("\n");
        System.out.println(dp.mkMatrixString(dp.solution(new Idx(dp.n() - 1, 0))));
    }

    private String[] items;
    private int[] weights;
    private int[] values;
    private int capacity;

    public Bimelim(String[] items, int[] weights, int[] values,
                     int capacity) {
        this.items = items;
        this.weights = weights;
        this.values = values;
        this.capacity = capacity;
        // Defines how values are formatted in the console output.
        // Formatter are: INT, ENGINEER, DECIMAL
        //this.formatter_$eq(this.INT());
    }

    @Override
    public int n() {
        return weights.length;
    }

    @Override
    public int m() {
        return capacity + 1;
    }

    @Override
    public double value(Idx idx, Integer d) {
        return d;
    }

    /**
     * If the remaining capacity (idx.j) plus the weight that could be taken
     * is less than the overall capacity we could take it. Thus,  { 0, 1 }.
     * If not, we can only skip it (={0}).
     */
    @Override
    public Integer[] decisions(Idx idx) {
        if (idx.i() != 0) {
            int z = (int) Math.floor((capacity - idx.j()) / weights[idx.i()])+1;
            Integer[] ret = new Integer[z];
            for (int k = 0; k < z; k++) {
                ret[k] = k;
            }
            return ret;
        } else {
            return new Integer[]{capacity - idx.j()};
        }
    }

    /**
     * The prev. state is the previous item (idx.i-1) and the prev. capacity.
     * The prev. capacity is the remaining capacity (idx.j) plus weight that was
     * taken (or plus 0 if it was skipped).
     */
    @Override
    public Idx[] prevStates(Idx idx, Integer d) {
        if (idx.i() > 0) {
            return new Idx[]{new Idx(idx.i() - 1, (weights[idx.i()] * d) + idx.j())};
        } else {
            return new Idx[]{};
        }
    }

    /**
     * Defines whether the minimum or maximum is calculated.
     *
     * @return
     */
    @Override
    public Function2 extremeFunction() {
        return this.MIN();
        // MAX();
    }

    /**
     * Provide row labels, i.e. each row gets a short description.
     *
     * @return Array of size n with the labels.
     */
    @Override
    public String[] rowLabels() {
        return items;
    }

    /**
     * Provide column labels, i.e. each columns gets a short description.
     * In this case, the column labels are the same as the column index.
     *
     * @return Array of size m with the labels.
     */
    @Override
    public Option<String[]> columnLabels() {
        String[] cArray = new String[capacity + 1];
        for (int i = 0; i <= capacity; i++) {
            cArray[i] = "" + i;
        }
        return new Some(cArray);
    }
}