/*
 * view/SimplePlotter.java
 *
 * Author: Taylor Heimbichner
 *
 * Defines a JPanel that will plot the information supplied to it, allowing the
 * program to immediately display the results of the integration. The plotter
 * allows for two variables (y and z) to be plotted against a third
 * variable (x).
 */

package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Arrays;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class SimplePlotter extends JPanel
{
	// The width of an axis
	private static final float AXIS_WIDTH = 2f;

	// The width of the y and z data, respectively
	private static final int Y_WIDTH = 2;
	private static final int Z_WIDTH = 2;

	// The preferred size of a plotter panel.
	private static final int PREFERRED_SIZE = 800;

	/*
	 * Defines a transformation that takes a datum and outputs the approrpriate
	 * location on the screen.
	 */
	private class Scale
	{
		// The leftmost/lowest data value allowed
		private final double min;

		// The rightmost/highest data value allowed
		private final double max;

		/*
		 * Constructs a scale with the specified min and max values.
		 */
		public Scale(double min, double max)
		{
			this.min = min;
			this.max = max;
		}

		/*
		 * Transforms an x datum into a horizontal screen coordinate.
		 */
		public int scaleHoriz(double x)
		{
			return (int) ((x - min) / (max - min) * getWidth());
		}

		/*
		 * Transforms a y or z datum into a vertical screen coordinate.
		 */
		public int scaleVert(double y)
		{
			return (int) ((max - y) / (max - min) * getHeight());
		}

		/*
		 * Returns a new Scale that:
		 * 1) Transforms 0.0 to the same location as this.
		 * 2) Uses newMax as its max.
		 */
		public Scale expandToMax(double newMax)
		{
			return new Scale(min * newMax / max, newMax);
		}
	}

	// The data to plot
	private double[] xs = null;
	private double[] ys = null;
	private double[] zs = null;

	// The Scales for plotting x, y, and z data
	private Scale xScale = new Scale(0d, 0d);
	private Scale yScale = new Scale(0d, 0d);
	private Scale zScale = new Scale(0d, 0d);

	// The locations to draw the axes
	private double xAxis = 0d;
	private double yAxis = 0d;
	private double zAxis = 0d;

	/*
	 * Constructs a new SimplePlotter
	 */
	public SimplePlotter()
	{
		setPreferredSize(new Dimension(PREFERRED_SIZE, PREFERRED_SIZE));
		setBackground(Color.WHITE);
	}

	/*
	 * Sets the data that this plotter is plotting to the specified xs, ys, and
	 * zs. Rescales the axes approrpriately.
	 */
	public void plot(double[] xs, double[] ys, double[] zs)
	{
		if (xs.length != ys.length)
		{
			throw new IllegalArgumentException();
		}

		this.xs = xs;
		this.ys = ys;
		this.zs = zs;

		// Scale the x axis
		double[] sortedX = xs.clone();
		Arrays.sort(sortedX);
		double xMin = sortedX[0];
		double xMax = sortedX[xs.length - 1];
		xScale = new Scale(xMin, xMax);

		// Scale the y axis
		double[] sortedY = ys.clone();
		Arrays.sort(sortedY);
		double yMin = sortedY[0];
		double yMax = sortedY[ys.length - 1];
		if (yMin > -0.5)
		{
			yMin = -0.5;
		}
		yScale = new Scale(yMin, yMax);

		// Scale the z axis
		double[] sortedZ = zs.clone();
		Arrays.sort(sortedZ);
		double zMax = sortedZ[zs.length - 1];
		zScale = yScale.expandToMax(zMax);
	}

	/*
	 * Sets the location of the axes.
	 */
	public void setAxes(double xAxis, double yAxis, double zAxis)
	{
		this.xAxis = xAxis;
		this.yAxis = yAxis;
		this.zAxis = zAxis;
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D graphics = (Graphics2D) g;

		// Draw the axes
		int x = xScale.scaleHoriz(xAxis);
		int y = yScale.scaleVert(yAxis);
		int z = zScale.scaleVert(zAxis);
		graphics.setStroke(new BasicStroke(AXIS_WIDTH));
		graphics.setColor(Color.GREEN);
		graphics.drawLine(0, z, getWidth(), z);
		graphics.setColor(Color.BLACK);
		graphics.drawLine(x, 0, x, getHeight());
		graphics.drawLine(0, y, getWidth(), y);

		// Draw the z data
		if (xs != null && zs != null)
		{
			graphics.setColor(Color.BLUE);
			for (int i = 0; i < xs.length; i++)
			{
				drawDot(graphics, Z_WIDTH, zScale, xs[i], zs[i]);
			}
		}

		// Draw the y data
		if (xs != null && ys != null)
		{
			graphics.setColor(Color.RED);
			for (int i = 0; i < xs.length; i++)
			{
				drawDot(graphics, Y_WIDTH, yScale, xs[i], ys[i]);
			}
		}
	}

	/*
	 * Draws a dot at the specified data point, using the specified size, scale,
	 * and graphics context.
	 */
	private void drawDot(Graphics2D graphics, int size, Scale vertScale, double x, double y)
	{
		graphics.fillOval(xScale.scaleHoriz(x) - size, vertScale.scaleVert(y) - size, 2 * size, 2 * size);
	}
}
