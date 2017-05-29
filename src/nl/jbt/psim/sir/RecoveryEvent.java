package nl.jbt.psim.sir;

import java.util.HashMap;
import java.util.Map;

import nl.jbt.psim.event.Event;
import nl.jbt.psim.math.CumulativeGammaDistribution;
import nl.jbt.psim.math.IDistribution;

public class RecoveryEvent extends Event {

	private final SirSimulator simulator;
	private final Map<String, Integer> stateChange = new HashMap<>();
	
	public RecoveryEvent(SirSimulator simulator) {
		this.simulator = simulator;
		this.currentRate = getRate();
		
		stateChange.put("S", 0);
		stateChange.put("I", -1);
		stateChange.put("R", 1);
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
		return simulator.I() == 0 || currentRate == 0;
	}

	@Override
	public double getRate() {
		return Math.abs(simulator.recoveryRate());
	}

	public void setNextExecutionTime() {
		IDistribution gamma = new CumulativeGammaDistribution(currentRate);
		setExecutionTime(gamma.nextDouble());
	}

	@Override
	public void beforeExecute() {
		this.currentRate = getRate();
	}

	@Override
	public void afterExecute() {
		
	}
	
	@Override
	public String toString() {
		return "Recovery event: " + getExecutionTime();
	}
}