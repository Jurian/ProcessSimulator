package nl.jbt.psim.sir;

import java.util.HashMap;
import java.util.Map;

import nl.jbt.psim.event.Event;
import nl.jbt.psim.math.CumulativeGammaDistribution;
import nl.jbt.psim.math.IDistribution;

public class InfectionEvent extends Event {

	private final SirSimulator simulator;
	private final Map<String, Integer> stateChange = new HashMap<>();
	
	public InfectionEvent(SirSimulator simulator) {
		this.simulator = simulator;
		this.currentRate = getRate();
		
		stateChange.put("S", -1);
		stateChange.put("I", 1);
		stateChange.put("R", 0);
		
		setNextExecutionTime();
	}

	@Override
	public void execute() {
		setNextExecutionTime();
		simulator.updateStates(stateChange, simulator.getScheduler().getCurrentTime());
		simulator.getScheduler().schedule(this);
	}

	@Override
	public boolean stopCondition() {
		return simulator.S() == 0 || simulator.I() == 0 || currentRate == 0;
	}

	@Override
	public double getRate() {
		return Math.abs(simulator.infectionRate());
	}

	public void setNextExecutionTime() {
		IDistribution gamma = new CumulativeGammaDistribution(getRate());
		setExecutionTime(gamma.nextDouble());
	}

	@Override
	public void beforeExecute() {
		currentRate = getRate();
	}

	@Override
	public void afterExecute() {
		
	}
	
	@Override
	public String toString() {
		return "Infecton event: " + getExecutionTime();
	}
}