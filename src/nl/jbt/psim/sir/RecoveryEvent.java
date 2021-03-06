package nl.jbt.psim.sir;

import java.util.HashMap;
import java.util.Map;

import nl.jbt.psim.event.Event;
import nl.jbt.psim.math.CumulativeGammaDistribution;
import nl.jbt.psim.math.IDistribution;

public class RecoveryEvent extends Event {

	private final SirSimulator simulator;
	private final Map<String, Float> stateChange = new HashMap<>();
	
	public RecoveryEvent(SirSimulator simulator) {
		this.simulator = simulator;
		this.currentRate = getRate();

		setNextExecutionTime();
	}

	@Override
	public void execute() {

		stateChange.put("s", simulator.susceptibleRate());
		stateChange.put("i", simulator.infectionRate());
		
		setNextExecutionTime();
		simulator.updateStates(stateChange, simulator.getScheduler().getCurrentTime());
		simulator.getScheduler().schedule(this);
	}

	@Override
	public boolean stopCondition() {
		return currentRate <= 0.0000001;
	}

	@Override
	public float getRate() {
		return (float) Math.abs(simulator.recoveryRate());
	}

	public void setNextExecutionTime() {
		IDistribution gamma = new CumulativeGammaDistribution(getRate());
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