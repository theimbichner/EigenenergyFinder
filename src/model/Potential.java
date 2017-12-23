/*
 * model/Potential.java
 *
 * Author: Taylor Heimbichner
 *
 * Defines a functional interface to represent potentials, as well as a few
 * sample Potential objects. All potentials should be half-potentials. I.e, they
 * should equal POSITIVE_INFINITY for x < 0.
 */

package model;

public interface Potential
{
	public static Potential linearHalfPotential = halfPowerPotential(1d, 1d);
	public static Potential halfHarmonic = halfPowerPotential(3d, 2d);
	public static Potential cubicHalfPotential = halfPowerPotential(5d, 3d);
	public static Potential quarticHalfPotential = halfPowerPotential(5d, 4d);

	public static Potential halfPowerPotential(double alpha, double power)
	{
		return x ->
		{
			if (x < 0d)
			{
				return Double.POSITIVE_INFINITY;
			}
			return alpha * Math.pow(x, power);
		};
	}

	public double valueAt(double x);
}
