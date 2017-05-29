package nl.jbt.psim.sir;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import org.jfree.chart.ChartPanel;

import nl.jbt.psim.chart.ChartLogger;
import nl.jbt.psim.event.Event;
import nl.jbt.psim.scheduler.Scheduler;
import nl.jbt.psim.simulator.Simulator;

public class SirSimulator extends Simulator {

	/**
	 * The parameter controlling how often a susceptible-infected contact
	 * results in a new infection.
	 */
	private double beta = 0.8;

	/**
	 * The rate an infected recovers and moves into the resistant phase.
	 */
	private double gamma = 0.25;

	/**
	 * Number of times state has changed
	 */
	private int stateCount = 0;
	
	/**
	 * Number of susceptible individuals
	 * @return
	 */
	int S() {
		return state.get("S");
	}
	
	/**
	 * Number of infected individuals
	 * @return
	 */
	int I() {
		return state.get("I");
	}
	
	/**
	 * Number of recovered individuals
	 * @return
	 */
	int R() {
		return state.get("R");
	}
	
	private ChartLogger chartLogger;


	public SirSimulator(Scheduler scheduler,  Map<String, Integer> beginState) {
		super(scheduler, beginState);
		
		this.chartLogger = new ChartLogger(state.keySet(), "SIR Model", "SIR Model", "time", "population");
		
		Event infectionEvent = new InfectionEvent(this);
		getScheduler().schedule(infectionEvent);

		Event recoveryEvent = new RecoveryEvent(this);
		getScheduler().schedule(recoveryEvent);
	}

	public double infectionRate() {
		return (beta * I() * S()) / N() - (gamma * I());
	}

	public double recoveryRate() {
		return gamma * I();
	}

	@Override
	public String toString() {
		return "S: " + S() + " I:" + I() + " R:" + R();
	}
	
	@Override
	public boolean stopCondition() {
		return stateCount > 1000;
	}
	
	public void updateStates(Map<String, Integer> changes, double time) {
		changes.entrySet().forEach(entry -> {
			int oldState = state.get(entry.getKey());
			int newState = oldState += entry.getValue();
			state.put(entry.getKey(), newState);
		});
		stateCount++;
		chartLogger.addState(time, state);
	}

	public static void main(String[] args) {

		Map<String, Integer> state = new HashMap<>();
		state.put("S", 499);
		state.put("I", 1);
		state.put("R", 0);
		
		Scheduler scheduler = new Scheduler();
		SirSimulator simulator = new SirSimulator(scheduler, state);

		JFrame frame = new JFrame();
		frame.setSize(800, 500);
		frame.add(new ChartPanel(simulator.chartLogger.chart));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		simulator.start();
	}
}