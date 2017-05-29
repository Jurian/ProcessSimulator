package nl.jbt.psim.sis;

import java.util.HashMap;
import java.util.Map;

import nl.jbt.psim.event.Event;
import nl.jbt.psim.math.CumulativeGammaDistribution;
import nl.jbt.psim.math.IDistribution;

public class InfectionEvent extends Event {

	private final SisSimulator simulator;
	private final Map<String, Float> stateChange = new HashMap<>();
	
	public InfectionEvent(SisSimulator simulator) {
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
		return false;
	}

	@Override
	public float getRate() {
		return (float) Math.abs(simulator.infectionRate());
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