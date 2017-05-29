package nl.jbt.psim.math;

public class LinearDistribution implements IDistribution {

	private final double rate;
	
	public LinearDistribution(double rate) {
		this.rate = rate;
	}
	
	@Override
	public double nextDouble() {
		return rate;
	}
}