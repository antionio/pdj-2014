package com.flimpure.laser.util;

import com.badlogic.gdx.math.MathUtils;

/**
 * Utility class for getting randomized values.
 * 
 * @author antti
 * 
 */
public final class RandomUtil {

	private RandomUtil() {
		// method hidden
	}

	public static boolean oneOfMany(int many) {
		int selector = MathUtils.random(many);
		if (selector == many) {
			return true;
		}
		return false;
	}

	/**
	 * Randomizes a one-of-three value.
	 * 
	 * @return true if randomized value was one of three, false otherwise.
	 */
	public static boolean oneThird() {
		int selector = MathUtils.random(2);
		if (selector == 0) {
			return true;
		}
		return false;
	}

	/**
	 * Randomizes one-of-fifth value.
	 * 
	 * @return returns true 20% of the time, false 80% of the time.
	 */
	public static boolean oneFifth() {
		int selector = MathUtils.random(4);
		if (selector == 0) {
			return true;
		}
		return false;
	}

	/**
	 * Randomizes a fifty-fifty value.
	 * 
	 * @return true half of the time, false half of the time
	 */
	public static boolean fiftyFifty() {
		int selector = MathUtils.random(10);
		if (selector > 5) {
			return true;
		}
		return false;
	}

	/**
	 * Randomizes a "big range" number using {@link #fiftyFifty()}. Half of the
	 * time the randomized number is a negative range, and half of the time a
	 * positive range.
	 * 
	 * @param range
	 * @return a number that can range from -range through range
	 */
	public static float bigRangeRandom(float range) {
		float randomness = MathUtils.random(range);
		if (fiftyFifty()) {
			randomness = -randomness;
		}
		return randomness;
	}
}
