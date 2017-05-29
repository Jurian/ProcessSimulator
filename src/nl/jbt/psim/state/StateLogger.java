package nl.jbt.psim.state;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public abstract class StateLogger {
	protected XYSeriesCollection dataseries = new XYSeriesCollection();
	private Map<String, XYSeries> series = new HashMap<>();

	public StateLogger(Set<String> states) {
		for (String state : states) {
			final XYSeries xyseries = new XYSeries(state);
			series.put(state, xyseries);
			dataseries.addSeries(xyseries);
		}
	}

	public void addState(double time, Map<String, Float> data) {
		data.entrySet().forEach(entry -> {
			series.get(entry.getKey()).add(time, entry.getValue());
		});
	}
}
