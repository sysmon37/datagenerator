package generator;

import java.util.Arrays;
import java.util.List;

class QuadraticEquationSolver
{
	public static List<Double> solve(double A, double B, double C)
	{
		double delta = Math.sqrt(B*B - 4*A*C);
		return  Arrays.asList(((-B - delta )/ (2 * A)), ((-B + delta )/ (2 * A)));
	} 
}