package nl.jbt.psim.simulator;

import java.util.Map;

import nl.jbt.psim.event.Event;
import nl.jbt.psim.scheduler.Scheduler;
import nl.jbt.psim.state.StateLogger;

public abstract class Simulator {
	
	private int maxStates = 1000;
	private int ingoreStop = 100;
	private int nrOfPrevStates = 10;
	private float minDelta = .00000001f;
	private float[] prevStates = new float[nrOfPrevStates];
	
	private StateLogger logger;
	
	/**
	 * Number of times state has changed
	 */
	private int stateCount = 0;
	
	/**
	 * State of system
	 */
	private final Map<String, Float> state;

	public Map<String, Float> getStates(){
		return state;
	}
	
	private Scheduler scheduler;
	
	public Simulator(Scheduler scheduler, Map<String, Float> state) {
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
	
	public boolean stopCondition() {
		float delta = 0;
		for(int i = 0; i < prevStates.length-1; i++) {
			delta += Math.abs(prevStates[i] - prevStates[i+1]);
		}

		if(stateCount < ingoreStop){
			return false;
		}
		
		if(delta < minDelta) {
			return true;
		}
		
		return stateCount > maxStates;
	}
	
	public void updateStates(Map<String, Float> changes, double time) {
		System.arraycopy(prevStates, 1, prevStates, 0, nrOfPrevStates - 1);
		prevStates[nrOfPrevStates - 1] = 0;
		
		changes.entrySet().forEach(entry -> {
			float oldState = state.get(entry.getKey());
			float newState = oldState += entry.getValue();
			float deltaState = Math.abs( state.get(entry.getKey()) -  entry.getValue());

			prevStates[nrOfPrevStates - 1] += deltaState;
			state.put(entry.getKey(), newState);
		});

		stateCount++;
		
		if(logger != null) {
			logger.addStates(time, state);
		}
	}
	
	public float getState(String name){
		return state.get(name);
	}

	public StateLogger getLogger() {
		return logger;
	}

	public void setLogger(StateLogger logger) {
		this.logger = logger;
	}
}