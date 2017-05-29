package nl.jbt.psim.chart;

import java.util.Set;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;

import nl.jbt.psim.state.StateLogger;

public class ChartLogger extends StateLogger {

	public final JFreeChart chart;
	
	public ChartLogger(Set<String> stateNames, String title, String name, String xAxisLabel, String yAxisLabel) {
		super(stateNames);
		chart = ChartFactory.createScatterPlot(title, xAxisLabel, yAxisLabel, dataseries);
	}
}
