package nl.jbt.psim.scheduler;

import java.util.PriorityQueue;
import java.util.Queue;

import nl.jbt.psim.event.Event;


public class Scheduler {
	
	private Queue<Event> queue;
	private double currentTime;
	
	public Scheduler() {
		this.setEventQueue(new PriorityQueue<>());
	}

	public Queue<Event> getEventQueue() {
		return queue;
	}

	public void setEventQueue(Queue<Event> eventQueue) {
		this.queue = eventQueue;
	}

	public void schedule(Event event) {
		event.setExecutionTime(currentTime + event.getExecutionTime());
		queue.add(event);
	}

	public Event getNextEvent() {
		return queue.poll();
	}

	public double getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(double currentTime) {
		this.currentTime = currentTime;
	}
}