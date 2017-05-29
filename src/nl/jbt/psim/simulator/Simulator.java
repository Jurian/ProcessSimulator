package nl.jbt.psim.simulator;

import java.util.Map;
import nl.jbt.psim.event.Event;
import nl.jbt.psim.scheduler.Scheduler;

public abstract class Simulator {
	
	/**
	 * Initial state of population
	 */
	protected final Map<String, Integer> state;
	
	/**
	 * Total population
	 * 
	 * @return
	 */
	protected int N() {
		return state.values().stream().mapToInt(x -> x).sum();
	}

	private Scheduler scheduler;
	
	public Simulator(Scheduler scheduler, Map<String, Integer> state) {
		this.scheduler = scheduler;
		this.state = state;
	}
	
	public void start() {
		Event event;
		while((event = scheduler.getNextEvent()) != null) {
			event.beforeExecute();
			if(!stopCondition() && !event.stopCondition()) {
				scheduler.setCurrentTime(event.getExecutionTime());
				event.execute();
			}
			event.afterExecute();
		}
	}
	
	public Scheduler getScheduler() {
		return scheduler;
	}
	
	public boolean finished() {
		return scheduler.getEventQueue().size() == 0;
	}
	
	public abstract boolean stopCondition();
}