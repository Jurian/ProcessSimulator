package nl.jbt.psim.sis;

import java.util.Map;

import nl.jbt.psim.event.Event;
import nl.jbt.psim.scheduler.Scheduler;
import nl.jbt.psim.simulator.Simulator;

public class SisSimulator extends Simulator {


	/**
	 * The average number of infections
	 */
	private float beta = 1/2f;

	/**
	 * The average duration of infection
	 */
	private float gamma = 1/3f;
	
	/**
	 * Number of susceptible individuals
	 * @return
	 */
	float s() {
		return getState("s");
	}
	
	/**
	 * Number of infected individuals
	 * @return
	 */
	float i() {
		return getState("i");
	}

	public float susceptibleRate(){
		return -beta * s() * i() + gamma * i();
	}
	
	public float infectionRate() {
		return beta * s() * i() - gamma * i();
	}

	@Override
	public String toString() {
		return "s: " + s() + " i:" + i();
	}
	
	public SisSimulator(Scheduler scheduler,  Map<String, Float> beginState) {
		super(scheduler, beginState);
		
		Event infectionEvent = new InfectionEvent(this);
		getScheduler().schedule(infectionEvent);

		Event recoveryEvent = new RecoveryEvent(this);
		getScheduler().schedule(recoveryEvent);
	}
}