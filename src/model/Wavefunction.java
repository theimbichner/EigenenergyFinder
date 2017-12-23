/*
 * model/Wavefunction.java
 *
 * Author: Taylor Heimbichner
 *
 * Defines a representation of a wavefunction. Based on some initial conditions
 * supplied to its constructor, a Wavefunction object can numerically integrate
 * Schrodinger's equation to determine the wavefunction associated with a given
 * energy, mass, and potential.
 */

package model;

public class Wavefunction
{
	/*
	 * Represents a single point in the wavefunction.
	 */
	private class WavePoint
	{
		private double x;
		private double slope = 0;
		private double value = 0;

		/*
		 * Constructs a WavePoint to represent the given point.
		 */
		public WavePoint(double x)
		{
			this.x = x;
		}

		/*
		 * Returns the second derivative of the wavefunction at this point.
		 */
		public double getCurvature()
		{
			return scaledMass * (potential.valueAt(x) - energy) * value;
		}

		/*
		 * Sets the slope and value of the wavefunction at this point based on
		 * its slope, value, and curvature at the previous point.
		 */
		private void step(WavePoint prev)
		{
			double dx = x - prev.x;
			slope = prev.slope + prev.getCurvature() * dx;
			value = prev.value + prev.slope * dx + prev.getCurvature() * dx * dx / 2;
		}
	}

	// Contains every WavePoint that makes up this wavefunction
	private final WavePoint[] points;

	// Parameters of integration
	private Potential potential;
	private double scaledMass = 0;
	private double energy = 0;

	/*
	 * Constructs a Wavefunction that allows integration between x0 and xf, with
	 * step size dx. The Wavefunction is initialized to contain a value and
	 * slope of startVal and startSlope, respectively, in the 0th WavePoint.
	 */
	public Wavefunction(double x0, double xf, double dx, double startVal, double startSlope)
	{
		int steps = 1 + (int) ((xf - x0) / dx);
		points = new WavePoint[steps];
		for (int i = 0; i < steps; i++)
		{
			points[i] = new WavePoint(x0 + i * dx);
		}
		points[0].slope = startSlope;
		points[0].value = startVal;
	}

	/*
	 * Performs the integration with the given parameters to calculate the value
	 * of the wavefunction at every point.
	 */
	public void integrate(double scaledMass, double energy, Potential potential)
	{
		this.scaledMass = scaledMass;
		this.energy = energy;
		this.potential = potential;
		for (int i = 1; i < points.length; i++)
		{
			points[i].step(points[i - 1]);
		}
	}

	/*
	 * Returns an array containing the x positions of every defined point in the
	 * wavefunction.
	 */
	public double[] getXData()
	{
		double[] ret = new double[points.length];
		for (int i = 0; i < points.length; i++)
		{
			ret[i] = points[i].x;
		}
		return ret;
	}

	/*
	 * Returns an array containing the value of the wavefunction at every point.
	 */
	public double[] getYData()
	{
		double[] ret = new double[points.length];
		for (int i = 0; i < points.length; i++)
		{
			ret[i] = points[i].value;
		}
		return ret;
	}

	/*
	 * Returns an array containing the value of the potential at every point in
	 * the wavefunction.
	 */
	public double[] getPotentialData()
	{
		double[] ret = new double[points.length];
		for (int i = 0; i < points.length; i++)
		{
			ret[i] = potential.valueAt(points[i].x);
		}
		return ret;
	}

	/*
	 * Returns the energy currently associated with the wavefunction.
	 */
	public double getEnergy()
	{
		return energy;
	}
}
