package nl.jbt.psim.main;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import nl.jbt.psim.chart.ChartLogger;
import nl.jbt.psim.scheduler.Scheduler;
import nl.jbt.psim.sir.SirSimulator;
import nl.jbt.psim.sis.SisSimulator;

import org.jfree.chart.ChartPanel;

public class Launcher {

	public static void main(String[] args) {
		Map<String, Float> state = new HashMap<>();
		state.put("s", 1f);
		state.put("i", 10/7900000f);
		state.put("r", 0f);
		
		Scheduler scheduler = new Scheduler();
		SirSimulator simulator = new SirSimulator(scheduler, state);
		ChartLogger logger = new ChartLogger(state.keySet(), "SIR Model", "days", "population");
		simulator.setLogger(logger);
		
		JFrame frame = new JFrame();
		frame.setSize(800, 500);
		frame.add(new ChartPanel(logger.getChart()));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		simulator.start();
		
		/*
		Map<String, Float> state = new HashMap<>();
		state.put("s", 1f);
		state.put("i", 10/7900000f);
		
		Scheduler scheduler = new Scheduler();
		SisSimulator simulator = new SisSimulator(scheduler, state);
		ChartLogger logger = new ChartLogger(state.keySet(), "SIS Model", "days", "population");
		simulator.setLogger(logger);

		JFrame frame = new JFrame();
		frame.setSize(800, 500);
		frame.add(new ChartPanel(logger.getChart()));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		simulator.start();
		*/
	}

}
