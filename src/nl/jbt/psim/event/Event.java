package nl.jbt.psim.event;

public abstract class Event implements  Comparable<Event> {

	private double executionTime;
	protected double currentRate;
	
	public abstract void execute();
	public abstract void beforeExecute();
	public abstract void afterExecute();
	
	public Event() {
		this(0);
	}
	
	public Event(long executionTime) {
		this.executionTime = executionTime;
	}
	
	public void setExecutionTime(double executionTime) {
		this.executionTime = executionTime;
	}

	public double getExecutionTime() {
		return executionTime;
	}
	

	public abstract boolean stopCondition();
	public abstract double getRate();
	
	@Override
	public int compareTo(Event other) {
		if (this.executionTime < other.executionTime) {
			return -1;
		} else if (this.executionTime > other.executionTime) {
			return 1;
		} else {
			return 0;
		}
	}
}
