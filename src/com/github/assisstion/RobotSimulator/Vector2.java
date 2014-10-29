package com.github.assisstion.RobotSimulator;

/**
 * A Vector2 class used for representing two doubles.
 * Many operations are built-in.
 * Most operations return the instance provided to allow chaining.
 * @author markusfeng
 *
 */
public class Vector2{
	/**
	 * The x value of the Vector
	 */
	public double x;

	/**
	 * The y value of the Vector
	 */
	public double y;

	/**
	 * Creates a vector2 with this.x = x and this.y = y;
	 * @param x the x value of the vector
	 * @param y the y value of the vector
	 */
	public Vector2(double x, double y){
		this.x = x;
		this.y = y;
	}

	/**
	 * Copies the data from another vector to this
	 * #csharp done automatically in C# because it is a Struct
	 */
	public Vector2(Vector2 v2){
		x = v2.x;
		y = v2.y;
	}

	/**
	 * Adds another vector to this vector
	 * Calls add(double, double)
	 * @param v2 the vector to add
	 * @return this instance for chaining
	 */
	public Vector2 add(Vector2 v2){
		return add(v2.x, v2.y);
	}

	/**
	 * Adds an x and y to this vector
	 * @param x the x to add
	 * @param y the y to add
	 * @return this instance for chaining
	 */
	public Vector2 add(double x, double y){
		this.x += x;
		this.y += y;
		return this;
	}

	/**
	 * Subtracts another vector from this vector
	 * Calls negate() and add(Vector2)
	 * @param v2 the vector to subtract
	 * @return this instance for chaining
	 */
	public Vector2 subtract(Vector2 v2){
		return negate().add(v2).negate();
	}

	/**
	 * Multiplies this vector by -1
	 * Calls multiply(double)
	 * @return this instance for chaining
	 */
	public Vector2 negate(){
		return multiply(-1);
	}

	/**
	 * Multiplies this vector by a double
	 * @param f the double to multiply by
	 * @return this instance for chaining
	 */
	public Vector2 multiply(double f){
		x *= f;
		y *= f;
		return this;
	}

	/**
	 * Divides this vector by a double
	 * @param f the double to divide by
	 * @return this instance for chaining
	 */
	public Vector2 divide(double f){
		return multiply(1/f);
	}

	/**
	 * Takes this vector to the power of another vector
	 * @param f the double to take the power by
	 * @return this instance for chaining
	 */
	public Vector2 pow(double f){
		x = Math.pow(x, f);
		y = Math.pow(y, f);
		return this;
	}

	/**
	 * Square roots this vector
	 * Calls pow(double);
	 * @return this instance for chaining
	 */
	public Vector2 sqrt(){
		return pow(0.5f);
	}

	/**
	 * Returns the Pythagorean distance
	 * Does not modify the vector
	 * @return the distance, or sqrt(x**2 + y**2)
	 */
	public double distance(){
		return Math.sqrt(x * x + y * y);
	}

	/**
	 * Changes the values of this vector to the
	 * minimum value of each of the values of the vectors
	 * @param v2 the vector to compare to
	 * @return this instance for chaining
	 */
	public Vector2 min(Vector2 v2){
		x = x < v2.x ? v2.x : x;
		y = y < v2.y ? v2.y : y;
		return this;
	}

	/**
	 * Changes the values of this vector to the
	 * maximum value of each of the values of the vectors
	 * @param v2 the vector to compare to
	 * @return this instance for chaining
	 */
	public Vector2 max(Vector2 v2){
		x = x > v2.x ? v2.x : x;
		y = y > v2.y ? v2.y : y;
		return this;
	}

	/**
	 * Changes the value of this vector so that
	 * each of the values lie between the minimum and maximum vectors
	 * @param min the minimum values of the resulting vector
	 * @param max the maximum values of the resulting vector
	 * @return this instance for chaining
	 */
	public Vector2 clamp(Vector2 min, Vector2 max){
		if(!min.strictlyLessOrEqual(max)){
			throw new IllegalArgumentException(
					"min must be strictly less than or equal to max!");
		}
		return max(min).min(max);
	}

	/**
	 * Returns true if the x and y of this vector is less than the other vector
	 * Does not modify the vector
	 * @param v2 the vector to compare to
	 * @return whether this vector is strictly less
	 */
	public boolean strictlyLess(Vector2 v2){
		return !(x >= v2.x || y >= v2.y);
	}

	/**
	 * Returns true if the x and y of this vector is less than or equal to the other vector
	 * Does not modify the vector
	 * @param v2 the vector to compare to
	 * @return whether this vector is strictly less or equal
	 */
	public boolean strictlyLessOrEqual(Vector2 v2){
		return !(x > v2.x || y > v2.y);
	}

	/**
	 * Returns true if the x and y of this vector is more than the other vector
	 * Does not modify the vector
	 * @param v2 the vector to compare to
	 * @return whether this vector is strictly more
	 */
	public boolean strictlyMore(Vector2 v2){
		return !(x <= v2.x || y <= v2.y);
	}

	/**
	 * Returns true if the x and y of this vector is more than or equal to the other vector
	 * Does not modify the vector
	 * @param v2 the vector to compare to
	 * @return whether this vector is strictly more
	 */
	public boolean strictlyMoreOrEqual(Vector2 v2){
		return !(x < v2.x || y < v2.y);
	}

	/**
	 * Returns true if the x and y of this vector is equal to the other vector
	 * Does not modify the vector
	 * @param v2 the vector to compare to
	 * @return whether this vector is strictly less
	 */
	public boolean strictlyEqual(Vector2 v2){
		return x == v2.x && y == v2.y;
	}

	/**
	 * Returns true if the two vectors are equal
	 * Calls strictlyEqual(Vector2)
	 */
	@Override
	public boolean equals(Object o){
		if(o == null){
			return false;
		}
		if(o instanceof Vector2){
			return strictlyEqual((Vector2) o);
		}
		return false;
	}

	@Override
	public int hashCode(){
		return (int)(Double.doubleToLongBits(x) ^ Double.doubleToLongBits(y));
	}

	@Override
	public String toString(){
		return "[" + x + "," + y + "]";
	}

	//If I had lambadas from Java1.8, I may make some Vector2 -> int operations;
}
