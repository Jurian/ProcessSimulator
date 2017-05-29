package nl.jbt.psim.math;

import java.util.Random;

import org.apache.commons.math3.distribution.GammaDistribution;

public class CumulativeGammaDistribution implements IDistribution {
	
	public static final Random RANDOM = new Random();
	private final GammaDistribution gamma;
	
	public CumulativeGammaDistribution(double scale) {
		gamma = new GammaDistribution(1, scale);
	}
	
	@Override
	public double nextDouble() {
		return gamma.cumulativeProbability(RANDOM.nextDouble());
	}
}