/*
 * view/GUI.java
 *
 * Author: Taylor Heimbichner
 *
 * Defines the GUI of the program.
 */

package view;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GUI
{
	// Contains all other components in the GUI
	private final JPanel contentPanel;

	// Components for entering integration parameters
	private final JTextField energyEntry;
	private final JCheckBox parityCheckBox;
	private final JTextField xMaxEntry;

	// JPanel that displays the graph of the wavefunction
	private final SimplePlotter plotter;

	// Buttons for exporting the wavefunction
	private final JButton exportButton;
	private final JButton extendedExportButton;

	/*
	 * Constructs a GUI with the given default values for the components.
	 */
	public GUI(double defaultEnergy, boolean defaultParity, double defaultXMax)
	{
		contentPanel = new JPanel();

		energyEntry = new JTextField("" + defaultEnergy, 10);
		parityCheckBox = new JCheckBox("Even Parity", defaultParity);
		xMaxEntry = new JTextField("" + defaultXMax, 10);
		plotter = new SimplePlotter();
		exportButton = new JButton("Export half wavefunction");
		extendedExportButton = new JButton("Export full wavefunction");
		layoutGUI();
	}

	/*
	 * Initializes the contentPanel to contain the appropriate components.
	 */
	private void layoutGUI()
	{
		contentPanel.setLayout(new BorderLayout());

		// Add the user controls to a single JPanel
		JPanel controlsPanel = new JPanel();
		controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS));
		JPanel controlsWrapper = new JPanel();
		controlsWrapper.add(controlsPanel);
		contentPanel.add(controlsWrapper, BorderLayout.EAST);

		// Energy selection components
		JPanel energyPanel = new JPanel();
		energyPanel.add(new JLabel("Energy (eV): "));
		energyPanel.add(energyEntry);
		controlsPanel.add(energyPanel);

		// Parity selection
		controlsPanel.add(parityCheckBox);

		// xMax selection
		JPanel xMaxPanel = new JPanel();
		xMaxPanel.add(new JLabel("X Max: "));
		xMaxPanel.add(xMaxEntry);
		controlsPanel.add(xMaxPanel);

		// Export buttons
		controlsPanel.add(exportButton);
		controlsPanel.add(extendedExportButton);

		// Add the plotter to the contentPanel
		contentPanel.add(plotter, BorderLayout.CENTER);
	}

	/*
	 * Sets the action to be taken when a generic update should occur. These
	 * updates occur when the user changes one of the parameters of integration.
	 */
	public void setUpdateAction(ActionListener listener)
	{
		energyEntry.addActionListener(listener);
		parityCheckBox.addActionListener(listener);
		xMaxEntry.addActionListener(listener);
	}

	/*
	 * Sets the action to be taken when the exportButton is clicked.
	 */
	public void setExportAction(ActionListener listener)
	{
		exportButton.addActionListener(listener);
	}

	/*
	 * Sets the action to be taken when the extendedExportButton is clicked.
	 */
	public void setExtendedExportAction(ActionListener listener)
	{
		extendedExportButton.addActionListener(listener);
	}

	/*
	 * Returns a JPanel containing the contents of the GUI.
	 */
	public JPanel getContents()
	{
		return contentPanel;
	}

	/*
	 * Returns a reference the the plotter JPanel.
	 */
	public SimplePlotter getPlotter()
	{
		return plotter;
	}

	/*
	 * Returns the current user selected eigenenergy.
	 */
	public double getEnergy()
	{
		return Double.parseDouble(energyEntry.getText());
	}

	/*
	 * Returns the current user selected parity. True indicates even parity.
	 * False indicates odd parity.
	 */
	public boolean getParity()
	{
		return parityCheckBox.isSelected();
	}

	/*
	 * Returns the current user selected maximum x value.
	 */
	public double getXMax()
	{
		return Double.parseDouble(xMaxEntry.getText());
	}
}
