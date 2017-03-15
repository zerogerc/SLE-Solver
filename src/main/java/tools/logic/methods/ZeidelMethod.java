package tools.logic.methods;

import tools.logic.Equation;
import tools.logic.Result;

import java.util.Arrays;

public class ZeidelMethod implements IMethod {
    @Override
    public Result solve(Equation equation, double eps) {
        int n = equation.getMatrix().length;
        double[][] matrix = equation.getMatrix();
        double[] temp = new double[n];
        double[] x = new double[n];
//        for (int i = 0; i < n; i++) {
//            x[i] = equation.getVector()[i] / matrix[i][i];
//        }
        Arrays.fill(x, 0);

        long iter = 0;
        long startTime = System.currentTimeMillis();
        double norm = 0;

        do {
            norm = 0;
            System.arraycopy(x, 0, temp, 0, n);

            for (int i = 0; i < n; i++) {
                double var = 0;
                for (int j = 0; j < i; j++) {
                    var += (matrix[i][j] * x[j]);
                }
                for (int j = i + 1; j < n; j++) {
                    var += (matrix[i][j] * temp[j]);
                }
                x[i] = (equation.getVector()[i] - var) / matrix[i][i];
            }

            for (int i = 0; i < n; i++) {
                norm += (x[i] - temp[i]) * (x[i] - temp[i]);
            }
            if (Math.sqrt(norm) >= eps)
                iter++;
            if (iter > 1_000_000) {
                return new Result(false, x, iter, System.currentTimeMillis() - startTime);
            }
        } while (norm > eps);

        return new Result(true, x, iter, System.currentTimeMillis() - startTime);
    }
}