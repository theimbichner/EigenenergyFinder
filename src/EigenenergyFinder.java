/*
 * EigenenergyFinder.java
 *
 * Author: Taylor Heimbichner
 *
 * Contains the main entry point for the program, as well as definitions for
 * functions that output the results of the integration to a file that is
 * readable by more sophisticated plotters.
 */

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javax.swing.JFrame;

import model.Potential;
import model.Wavefunction;
import view.GUI;

public class EigenenergyFinder
{
	// Default mass factor. Equal to the electron mass when energies are in eV
	// and lengths are in angstroms
	public static final double STD_BETA = 0.26246;

	// X step size
	public static final double STEP = 0.0001d;

	// Default parameters of integration
	public static final double DEFAULT_ENERGY = 1d;
	public static final double DEFAULT_XMAX = 4d;

	// The most recently plotted wavefunction
	public static Wavefunction psi = null;

	public static void main(String[] args)
	{
		GUI gui = new GUI(DEFAULT_ENERGY, true, DEFAULT_XMAX);

		gui.setUpdateAction(ae -> plot(gui));
		gui.setExportAction(ae -> export(psi));
		gui.setExtendedExportAction(ae -> exportExtended(psi, gui.getParity()));

		plot(gui);

		JFrame frame = new JFrame();
		frame.add(gui.getContents());
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	/*
	 * Updates the GUI based on user input.
	 */
	public static void plot(GUI gui)
	{
		// Read the parameters of the integration from the gui
		double startVal = gui.getParity() ? 1d : 0d;
		double startSlope = gui.getParity() ? 0d : 1d;
		double energy;
		double xMax;
		try
		{
			energy = gui.getEnergy();
			xMax = gui.getXMax();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}

		// Calculate the wavefunction
		psi = new Wavefunction(0d, xMax, STEP, startVal, startSlope);
		psi.integrate(STD_BETA, energy, Potential.halfHarmonic);

		// Update the graph
		gui.getPlotter().plot(psi.getXData(), psi.getYData(), psi.getPotentialData());
		gui.getPlotter().setAxes(0d, 0d, energy);
		gui.getPlotter().repaint();
	}

	/*
	 * Uses the favored export method (currently exportPython()) to save the
	 * half wavefunction (the wavefunction corresponding to the half potential)
	 * to a file.
	 */
	public static void export(Wavefunction psi)
	{
		String filename = String.format("half-wavefunction-%f", psi.getEnergy());
		exportPython(filename, psi.getXData(), psi.getYData());
	}

	/*
	 * Uses the favored export method (currently exportPython()) to save the
	 * full (mirrored about x = 0) wavefunction to a file.
	 */
	public static void exportExtended(Wavefunction psi, boolean parity)
	{
		// Determine whether the mirrored portion should be sign flipped
		double parityFactor = parity ? 1d : -1d;
		double[] xData = psi.getXData();
		double[] yData = psi.getYData();
		double[] xs = new double[xData.length * 2];
		double[] ys = new double[yData.length * 2];

		// Copy the wavefunction data into xs and ys and mirror
		for (int i = 0; i < xData.length; i++)
		{
			xs[xData.length - 1 - i] = -xData[i];
			xs[i + xData.length] = xData[i];
			ys[yData.length - 1 - i] = parityFactor * yData[i];
			ys[i + yData.length] = yData[i];
		}
		String filename = String.format("%s-wavefunction-%f", parity ? "even" : "odd", psi.getEnergy());

		// Export the data
		exportPython(filename, xs, ys);
	}

	/*
	 * Exports the data specified in xs and ys to a .csv file specified by
	 * filename.
	 */
	public static void exportCSV(String filename, double[] xs, double[] ys)
	{
		if (xs.length != ys.length)
		{
			throw new IllegalArgumentException();
		}

		// Open the file
		PrintWriter writer = null;
		try
		{
			writer = new PrintWriter(filename + ".csv");
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		if (writer == null)
		{
			return;
		}

		// Write the data
		for (int i = 0; i < xs.length; i++)
		{
			writer.printf("%f, %f%n", xs[i], ys[i]);
		}

		// Close the file
		writer.close();
	}

	/*
	 * Exports the data specified in xs and ys to a file specified by filename.
	 * The produced file is readable by plotter.py, which will create a
	 * detailed, polished plot of the data.
	 */
	public static void exportPython(String filename, double[] xs, double[] ys)
	{
		if (xs.length != ys.length)
		{
			throw new IllegalArgumentException();
		}

		// Open the file
		PrintWriter writer = null;
		try
		{
			writer = new PrintWriter(filename + ".txt");
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		if (writer == null)
		{
			return;
		}

		// Print the xs on line 1
		for (double x : xs)
		{
			writer.format("%.5f ", x);
		}
		writer.println();

		// Print the ys on line 2
		for (double y : ys)
		{
			writer.format("%.5f ", y);
		}
		writer.println();

		// Close the file
		writer.close();
	}
}
