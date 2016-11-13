import net.gumbix.dynpro.DynProJava;
import net.gumbix.dynpro.Idx;
import net.gumbix.dynpro.PathEntry;
import scala.Function2;
import scala.Option;
import scala.Some;

import java.util.List;

/**
 * The Knapsack problem solved with dynamic programming.
 *
 * @author Markus Gumbel (m.gumbel@hs-mannheim.de)
 */
public class Knapsack extends DynProJava<Integer> {

  public static void main(String[] args) {
    String[] rowLabels = {"A", "B", "C", "D"};
    int[] weights = {2, 2, 6, 5};
    int[] values = {6, 3, 5, 4};
    Knapsack dp = new Knapsack(rowLabels, weights, values, 10);
    // The maximum is expected at the last item (n-1)
    // with no capacity left (0);
    List<PathEntry<Integer>> solutionJava =
            dp.solutionAsList(new Idx(dp.n() - 1, 0));
    System.out.println("Optimal Decisions:");
    for (PathEntry<Integer> entry : solutionJava) {
      System.out.print(entry.decision() + " ");
    }
    System.out.println("\n");
    System.out.println(dp.mkMatrixString(dp.solution(new Idx(dp.n() - 1, 0))));
  }

  private String[] items;
  private int[] weights;
  private int[] values;
  private int capacity;

  public Knapsack(String[] items, int[] weights, int[] values,
                  int capacity) {
    this.items = items;
    this.weights = weights;
    this.values = values;
    this.capacity = capacity;
    // Defines how values are formatted in the console output.
    // Formatter are: INT, ENGINEER, DECIMAL
    this.formatter_$eq(this.INT());
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
    return d * values[idx.i()];
  }

  /**
   * If the remaining capacity (idx.j) plus the weight that could be taken
   * is less than the overall capacity we could take it. Thus,  { 0, 1 }.
   * If not, we can only skip it (={0}).
   */
  @Override
  public Integer[] decisions(Idx idx) {
    if (idx.j() + weights[idx.i()] <= capacity) {
      return new Integer[]{0, 1};
    } else {
      return new Integer[]{0};
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
      Idx pidx = new Idx(idx.i() - 1, idx.j() + d * weights[idx.i()]);
      return new Idx[]{pidx};
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
    return this.MAX(); // oder MIN()
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