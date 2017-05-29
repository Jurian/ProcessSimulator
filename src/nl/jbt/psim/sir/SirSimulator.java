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
	 * The average number of infections
	 */
	private float beta = 1/2f;

	/**
	 * The average duration of infection
	 */
	private float gamma = 1/3f;

	/**
	 * Number of times state has changed
	 */
	private int stateCount = 0;
	
	/**
	 * Number of susceptible individuals
	 * @return
	 */
	float s() {
		return state.get("s");
	}
	
	/**
	 * Number of infected individuals
	 * @return
	 */
	float i() {
		return state.get("i");
	}
	
	/**
	 * Number of recovered individuals
	 * @return
	 */
	float r() {
		return state.get("r");
	}
	
	private ChartLogger chartLogger;


	public SirSimulator(Scheduler scheduler,  Map<String, Float> beginState) {
		super(scheduler, beginState);
		
		this.chartLogger = new ChartLogger(state.keySet(), "SIR Model", "SIR Model", "time", "population");
		
		Event infectionEvent = new InfectionEvent(this);
		getScheduler().schedule(infectionEvent);

		Event recoveryEvent = new RecoveryEvent(this);
		getScheduler().schedule(recoveryEvent);
	}

	public float susceptibleRate(){
		return -beta * s() * i();
	}
	
	public float infectionRate() {
		return beta * s() * i() - gamma * i();
	}

	public float recoveryRate() {
		return gamma * i();
	}

	@Override
	public String toString() {
		return "s: " + s() + " i:" + i() + " r:" + r();
	}
	
	@Override
	public boolean stopCondition() {
		return stateCount > 1000;
	}
	
	public void updateStates(Map<String, Float> changes, double time) {
		changes.entrySet().forEach(entry -> {
			float oldState = state.get(entry.getKey());
			float newState = oldState += entry.getValue();
			state.put(entry.getKey(), newState);
		});
		stateCount++;
		chartLogger.addState(time, state);
	}

	public static void main(String[] args) {

		Map<String, Float> state = new HashMap<>();
		state.put("s", 1f);
		state.put("i", 10/7900000f);
		state.put("r", 0f);
		
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