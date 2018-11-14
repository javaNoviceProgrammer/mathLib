package mathLib.util;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.jfree.chart.ChartPanel;

import mathLib.plot.MatlabChart;

public class FunctionPlotter extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField variable;
	private JTextField function;
	private JTextField start;
	private JTextField end;
	private JTextField points;
	
	MatlabChart fig ;
	double xStart ;
	double xEndn ;
	int numPoints ;
	double[] x ;
	double[] y ;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FunctionPlotter frame = new FunctionPlotter();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FunctionPlotter() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 616, 555);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0};
		gbl_contentPane.rowHeights = new int[]{118, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JPanel plotPanel = new JPanel();
		plotPanel.setBorder(new TitledBorder(null, "plot", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_plotPanel = new GridBagConstraints();
		gbc_plotPanel.fill = GridBagConstraints.BOTH;
		gbc_plotPanel.gridx = 0;
		gbc_plotPanel.gridy = 1;
		contentPane.add(plotPanel, gbc_plotPanel);
		plotPanel.setLayout(new BorderLayout(0, 0));
		
		fig = new MatlabChart() ;
		fig.plot(new double[0], new double[0]);
		fig.renderPlot();
		ChartPanel chartPanel = new ChartPanel(fig.getChart());
		plotPanel.add(chartPanel) ;
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Setup the function", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		contentPane.add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0};
		gbl_panel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JPanel panel_2 = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.insets = new Insets(5, 5, 5, 5);
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 0;
		panel.add(panel_2, gbc_panel_2);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[]{0, 10, 0, 0, 0};
		gbl_panel_2.rowHeights = new int[]{0, 0};
		gbl_panel_2.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panel_2.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_2.setLayout(gbl_panel_2);
		
		JLabel lblF = new JLabel("f (");
		GridBagConstraints gbc_lblF = new GridBagConstraints();
		gbc_lblF.anchor = GridBagConstraints.EAST;
		gbc_lblF.insets = new Insets(5, 5, 0, 5);
		gbc_lblF.gridx = 0;
		gbc_lblF.gridy = 0;
		panel_2.add(lblF, gbc_lblF);
		
		variable = new JTextField();
		GridBagConstraints gbc_variable = new GridBagConstraints();
		gbc_variable.insets = new Insets(0, 0, 0, 5);
		gbc_variable.fill = GridBagConstraints.HORIZONTAL;
		gbc_variable.gridx = 1;
		gbc_variable.gridy = 0;
		panel_2.add(variable, gbc_variable);
		variable.setColumns(5);
		
		JLabel label = new JLabel(") = ");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 0, 5);
		gbc_label.anchor = GridBagConstraints.EAST;
		gbc_label.gridx = 2;
		gbc_label.gridy = 0;
		panel_2.add(label, gbc_label);
		
		function = new JTextField();
		GridBagConstraints gbc_function = new GridBagConstraints();
		gbc_function.fill = GridBagConstraints.HORIZONTAL;
		gbc_function.gridx = 3;
		gbc_function.gridy = 0;
		panel_2.add(function, gbc_function);
		function.setColumns(10);
		
		JPanel panel_3 = new JPanel();
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.insets = new Insets(5, 5, 5, 5);
		gbc_panel_3.fill = GridBagConstraints.BOTH;
		gbc_panel_3.gridx = 0;
		gbc_panel_3.gridy = 1;
		panel.add(panel_3, gbc_panel_3);
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel_3.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panel_3.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_3.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_3.setLayout(gbl_panel_3);
		
		JLabel lblStartX = new JLabel("Start: x = ");
		GridBagConstraints gbc_lblStartX = new GridBagConstraints();
		gbc_lblStartX.anchor = GridBagConstraints.EAST;
		gbc_lblStartX.insets = new Insets(5, 5, 5, 5);
		gbc_lblStartX.gridx = 0;
		gbc_lblStartX.gridy = 0;
		panel_3.add(lblStartX, gbc_lblStartX);
		
		start = new JTextField();
		GridBagConstraints gbc_start = new GridBagConstraints();
		gbc_start.insets = new Insets(0, 0, 5, 5);
		gbc_start.fill = GridBagConstraints.HORIZONTAL;
		gbc_start.gridx = 1;
		gbc_start.gridy = 0;
		panel_3.add(start, gbc_start);
		start.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 4;
		gbc_lblNewLabel.gridy = 0;
		panel_3.add(lblNewLabel, gbc_lblNewLabel);
		
		JButton btnNewButton = new JButton("Set Parameters");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				xStart = Double.parseDouble(start.getText()) ;
				xEndn = Double.parseDouble(end.getText()) ;
				numPoints = Integer.parseInt(points.getText()) ;
				x = MathUtils.linspace(xStart, xEndn, numPoints) ;
				y = new double[x.length] ;
				fig.clear();
				
				lblNewLabel.setText("All set!");
				
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 3;
		gbc_btnNewButton.gridy = 0;
		panel_3.add(btnNewButton, gbc_btnNewButton);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("Marker");
		chckbxNewCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(chckbxNewCheckBox.isSelected())
					fig.markerON();
				else
					fig.markerOFF();
			}
		});
		GridBagConstraints gbc_chckbxNewCheckBox = new GridBagConstraints();
		gbc_chckbxNewCheckBox.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxNewCheckBox.gridx = 6;
		gbc_chckbxNewCheckBox.gridy = 0;
		panel_3.add(chckbxNewCheckBox, gbc_chckbxNewCheckBox);
		

		
		JLabel lblEndX = new JLabel("End: x = ");
		GridBagConstraints gbc_lblEndX = new GridBagConstraints();
		gbc_lblEndX.anchor = GridBagConstraints.EAST;
		gbc_lblEndX.insets = new Insets(5, 5, 5, 5);
		gbc_lblEndX.gridx = 0;
		gbc_lblEndX.gridy = 1;
		panel_3.add(lblEndX, gbc_lblEndX);
		
		end = new JTextField();
		end.setColumns(10);
		GridBagConstraints gbc_end = new GridBagConstraints();
		gbc_end.insets = new Insets(0, 0, 5, 5);
		gbc_end.fill = GridBagConstraints.HORIZONTAL;
		gbc_end.gridx = 1;
		gbc_end.gridy = 1;
		panel_3.add(end, gbc_end);
		
		JButton appendButton = new JButton("Plot");
		appendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				lblNewLabel.setText("Plotted...");
				for(int i=0; i<x.length; i++) {
					Map<String, Double> vars = new HashMap<>() ;
					vars.put(variable.getText(), x[i]) ;
					y[i] = MathUtils.evaluate(function.getText(), vars) ;
				}
				
				fig.setYLabel("f("+variable.getText()+")");
				fig.setXLabel(variable.getText());
				fig.plot(x, y, "b");
				fig.renderPlot();
				
				if(chckbxNewCheckBox.isSelected())
					fig.markerON();
				else
					fig.markerOFF();
			}
		});
		GridBagConstraints gbc_appendButton = new GridBagConstraints();
		gbc_appendButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_appendButton.weighty = 2.0;
		gbc_appendButton.insets = new Insets(0, 0, 5, 5);
		gbc_appendButton.gridx = 3;
		gbc_appendButton.gridy = 1;
		panel_3.add(appendButton, gbc_appendButton);
		
		JLabel lblPoints = new JLabel("Points: ");
		GridBagConstraints gbc_lblPoints = new GridBagConstraints();
		gbc_lblPoints.anchor = GridBagConstraints.EAST;
		gbc_lblPoints.insets = new Insets(5, 5, 0, 5);
		gbc_lblPoints.gridx = 0;
		gbc_lblPoints.gridy = 2;
		panel_3.add(lblPoints, gbc_lblPoints);
		
		points = new JTextField();
		points.setColumns(10);
		GridBagConstraints gbc_points = new GridBagConstraints();
		gbc_points.insets = new Insets(0, 0, 0, 5);
		gbc_points.fill = GridBagConstraints.HORIZONTAL;
		gbc_points.gridx = 1;
		gbc_points.gridy = 2;
		panel_3.add(points, gbc_points);		
	}
}
