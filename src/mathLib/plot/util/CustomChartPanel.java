package mathLib.plot.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.border.Border;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import flanagan.io.FileOutput;
import mathLib.plot.MatlabChart;
import mathLib.util.CustomJFileChooser;

public class CustomChartPanel extends ChartPanel {
	private static final long serialVersionUID = 1L;
	private ImageSelection imgsel;
	private int cwidth;
	private int cheight;
	private MatlabChart fig = null ;

	public CustomChartPanel(JFreeChart chart, int width, int height, int minimumDrawWidth, int minimumDrawHeight,
			int maximumDrawWidth, int maximumDrawHeight, boolean useBuffer, boolean properties, boolean save,
			boolean print, boolean zoom, boolean tooltips) {
		super(chart, width, height, minimumDrawWidth, minimumDrawHeight, maximumDrawWidth, maximumDrawHeight, useBuffer,
				true, true, true, true, true);
		this.cwidth = width;
		this.cheight = height;
		initialize();
	}

	public CustomChartPanel(JFreeChart chart) {
		super(chart) ;
		initialize() ;
	}

	public CustomChartPanel(MatlabChart fig) {
		super(fig.getChart()) ;
		this.fig = fig ;
		initialize() ;
	}

	private void initialize() {
		// avoiding the bad scaling of fonts
	    setMinimumDrawHeight(Integer.MIN_VALUE);
	    setMinimumDrawWidth(Integer.MIN_VALUE);
	    setMaximumDrawHeight(Integer.MAX_VALUE);
	    setMaximumDrawWidth(Integer.MAX_VALUE);
	    // updating popup menu
		getPopupMenu().add(new JSeparator());
		// for copying to clipboard
		JMenuItem copyAsBitmap = new JMenuItem("Copy to Clipboard");
		copyAsBitmap.setActionCommand("copyToClipboard");
		copyAsBitmap.addActionListener(this);
		getPopupMenu().add(copyAsBitmap);
		imgsel = new ImageSelection();
		// for fast configuring for saving
		JMenuItem configureForCopy = new JMenuItem("Fast Configure");
		configureForCopy.setActionCommand("fastConfigure");
		configureForCopy.addActionListener(this);
		getPopupMenu().add(configureForCopy);
		// for exporting to matlab
		getPopupMenu().add(new JSeparator());
		JMenuItem exportToMATLAB = new JMenuItem("Export to MATLAB");
		exportToMATLAB.setActionCommand("exportToMATLAB");
		exportToMATLAB.addActionListener(this);
		getPopupMenu().add(exportToMATLAB);
		// for exporting to Python lists
		JMenuItem exportToPython = new JMenuItem("Export to Python");
		exportToPython.setActionCommand("exportToPython");
		exportToPython.addActionListener(this);
		getPopupMenu().add(exportToPython);

		Border padding = BorderFactory.createEmptyBorder(0, 0, 0, 0);
		setBorder(padding);

		// implement fig functions
		if(fig!= null) {
			// do stuff
		}
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equals("copyToClipboard")) {
			setClipboard(getChart().createBufferedImage(cwidth, cheight, BufferedImage.TYPE_INT_ARGB, null));
		} else if (ae.getActionCommand().equals("fastConfigure")) {
			// background color
			getChart().setBackgroundPaint(Color.WHITE);
			// set xy plot stroke and paint
			getChart().getXYPlot().setOutlinePaint(Color.BLACK);
			getChart().getXYPlot().setOutlineStroke(new BasicStroke(2));
			// setting up the font
			Font font = JFreeChart.DEFAULT_TITLE_FONT;
			int fontSize = 20;
			font = new Font(font.getName(), Font.PLAIN, fontSize);
			getChart().getXYPlot().getDomainAxis().setLabelFont(font);
			getChart().getXYPlot().getDomainAxis().setTickLabelFont(font);
			getChart().getXYPlot().getRangeAxis().setLabelFont(font);
			getChart().getXYPlot().getRangeAxis().setTickLabelFont(font);
			int N = getChart().getXYPlot().getDataset().getSeriesCount();
			for (int i = 0; i < N; i++) {
				getChart().getXYPlot().getRenderer().setSeriesStroke(i, new BasicStroke(3f));
			}
		} else if (ae.getActionCommand().equals("exportToMATLAB")) {
			// get all XY data
			// check linewidths and colors
			int N = getChart().getXYPlot().getDataset().getSeriesCount();
			// get custom jfilechooser and set the path
			try {
				CustomJFileChooser fc = new CustomJFileChooser();
				fc.saveFile();
				FileOutput fo = new FileOutput(fc.getSelectedFile().getAbsolutePath() + ".m");
				// print matlab commands to the m file
				fo.println("figure");
				fo.println("hold on ;");
				for (int i = 0; i < N; i++) {
					exportSeries(i, fo);
				}
				fo.println("grid on;");
				String xlabel = getChart().getXYPlot().getDomainAxis().getLabel();
				fo.println("xlabel('" + xlabel + "');");
				String ylabel = getChart().getXYPlot().getRangeAxis().getLabel();
				fo.println("ylabel('" + ylabel + "');");
				fo.close();
				System.gc();
			} catch (Exception e) {
			}

		}
		else if (ae.getActionCommand().equals("exportToPython")) {
			// get all XY data
			// check linewidths and colors
			int N = getChart().getXYPlot().getDataset().getSeriesCount();
			// get custom jfilechooser and set the path
			try {
				CustomJFileChooser fc = new CustomJFileChooser();
				fc.saveFile();
				FileOutput fo = new FileOutput(fc.getSelectedFile().getAbsolutePath() + ".txt");
				// print matlab commands to the m file
				for (int i = 0; i < N; i++) {
					exportSeriesToPython(i, fo);
				}
				fo.close();
				System.gc();
			} catch (Exception e) {
			}

		}
		else
			super.actionPerformed(ae);
	}

	private void exportSeries(int seriesNum, FileOutput fo) {
		int M = getChart().getXYPlot().getDataset().getItemCount(seriesNum);
		// printing x variable
		fo.println("x_" + seriesNum + "= [");
		for (int i = 0; i < M; i++) {
			double x = getChart().getXYPlot().getDataset().getXValue(seriesNum, i);
			fo.println(x);
		}
		fo.println("];");
		// printing y variable
		fo.println("y_" + seriesNum + "= [");
		for (int i = 0; i < M; i++) {
			double y = getChart().getXYPlot().getDataset().getYValue(seriesNum, i);
			fo.println(y);
		}
		fo.println("];");
		// getting linewidth
		// printing plot command
		fo.println("ph" + seriesNum + "= " + "plot(x_" + seriesNum + ", y_" + seriesNum + ", 'linewidth', 3" + ") ;");
	}

	private void exportSeriesToPython(int seriesNum, FileOutput fo) {
		int M = getChart().getXYPlot().getDataset().getItemCount(seriesNum);
		// printing x variable
		fo.print("x_" + seriesNum + " = [");
		for (int i = 0; i < M-1; i++) {
			double x = getChart().getXYPlot().getDataset().getXValue(seriesNum, i);
			fo.print(x + ", ");
		}
		double x = getChart().getXYPlot().getDataset().getXValue(seriesNum, M-1);
		fo.print(x);
		fo.println("];");
		// printing y variable
		fo.print("y_" + seriesNum + " = [");
		for (int i = 0; i < M-1; i++) {
			double y = getChart().getXYPlot().getDataset().getYValue(seriesNum, i);
			fo.print(y + ", ");
		}
		double y = getChart().getXYPlot().getDataset().getYValue(seriesNum, M-1);
		fo.print(y);
		fo.println("];");
		// getting linewidth
		// printing plot command
//		fo.println("plt.plot(x_" + seriesNum + ", y_" + seriesNum + ")" );
	}


	private void setClipboard(Image image) {
		imgsel.setImage(image);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(imgsel, null);
	}

	private class ImageSelection implements Transferable {
		private Image image;

		public void setImage(Image image) {
			this.image = image;
		}

		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] { DataFlavor.imageFlavor };
		}

		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return DataFlavor.imageFlavor.equals(flavor);
		}

		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
			if (!DataFlavor.imageFlavor.equals(flavor)) {
				throw new UnsupportedFlavorException(flavor);
			}
			return image;
		}
	}

}
