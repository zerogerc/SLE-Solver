package tools.ui;

import com.sun.istack.internal.NotNull;
import tools.logic.Equation;
import tools.logic.MatrixGenerator;
import tools.logic.Result;
import tools.logic.methods.GaussMethod;
import tools.logic.methods.DescentMethod;
import tools.logic.methods.YakobiMethod;
import tools.logic.methods.ZeidelMethod;

public class MainScreenDelegate {

    private static final double DEFAULT_EPS = 1e-9;

    @NotNull
    private final MatrixGenerator matrixGenerator = new MatrixGenerator();

    @NotNull
    public double[][] generateDiagonalPrevailingMatrix() {
        return matrixGenerator.generateDiagonalPrevailingMatrix();
    }

    @NotNull
    public double[][] generateRandomMatrix() {
        return matrixGenerator.generateRandomMatrix();
    }

    @NotNull
    public double[][] generateRandomSymMatrix() {
        return matrixGenerator.generateRandomSymMatrix();
    }

    @NotNull
    public double[][] generateHilbertMatrix() {
        return matrixGenerator.generateHilbertMatrix();
    }

    @NotNull
    public Result solveWithGauss(@NotNull Equation equation) {
        GaussMethod method = new GaussMethod();
        return method.solve(equation, DEFAULT_EPS);
    }

    @NotNull
    public Result solveWithYakobi(@NotNull Equation equation) {
        YakobiMethod method = new YakobiMethod();
        return method.solve(equation, DEFAULT_EPS);
    }

    @NotNull
    public Result solveWithZeidel(@NotNull Equation equation) {
        ZeidelMethod method = new ZeidelMethod();
        return method.solve(equation, DEFAULT_EPS);
    }

    @NotNull
    public Result solveWithDescent(@NotNull Equation equation) {
        DescentMethod method = new DescentMethod();
        return method.solve(equation, DEFAULT_EPS);
    }


}
