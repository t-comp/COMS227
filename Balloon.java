package hw1;

/**
 * Simulates the flight of a hot air balloon, accounting for numerous factors such as
 * the mass of the balloon, outside air temperature, available fuel, rate of fuel burn, etc.
 * 
 * @author Taylor Bauer
 */
public class Balloon {
	
	private final static double HEAT_LOSS_FACTOR = 0.1;
	private final static int VOLUME_OF_BALLOON_AIR = 61234;
	private final static double ACCELERATION_FROM_GRAVITY = 9.81;
	private final static double GAS_CONSTANT = 287.05;
	private final static double STANDARD_PRESSURE = 1013.25;
	private final static double KEVLINS_AT_0_DEGREES = 273.15;
	
	/** startingAirTemp is the initial temperature of the air at time of constructor call. */
	private double startingAirTemp;
	/** startingWindDirection is the initial temperature of the air at time of constructor call. */
	private double startingWindDirection;
	/** airTemp is the variable used for the air temperature throughout the class. */
	private double airTemp;
	/** windDirection is the variable used for the windDirection throughout the class. */
	private double windDirection;
	/** balloonMass is a variable storing the mass of a balloon. */
	private double balloonMass;
	/** currentTime variable storing the current time of the simulation. */
	private long currentTime;
	/** balloonTemp is a variable storing the temperature of the balloon. */
	private double balloonTemp;
	/** fuelBurnRate is a variable storing the rate at which the fuel is burning. */
	private double fuelBurnRate;
	/** balloonAltitude is a variable storing the altitude of the balloon. */
	private double balloonAltitude;
	/** remainingFuel is a variable storing the remaining fuel that can be used to heat the air in the balloon. */
	private double remainingFuel;
	/** balloonVelocity is a variable storing the velocity of the balloon. */
	private double balloonVelocity;
	/** balloonTether is a variable storing the length of the balloon's tether. */
	private double balloonTether;
	/** tetherRemaining is a variable storing the length of the tether minus the current balloon altitude. */
	private double tetherRemaining;
	
	/**
	 * 
	 * @param airTemp This is the air temperature outside the balloon in C.
	 * @param windDirection This is the wind direction in degrees, it is assumed to be between 0(inclusive) and 360 (exclusive)
	 */
	public Balloon(double airTemp, double windDirection) {
		    this.startingAirTemp = airTemp;
		    this.startingWindDirection = windDirection;
		    reset();	    
	}
	
	/** 
	 * This method gathers the remaining fuel that can be used to heat air balloon.
	 * @return returns remainingFuel within the air balloon.
	 */
	public double getFuelRemaining() {
		return remainingFuel;
	}
	
	/**
	 * This method sets the remaining fuel that the air balloon can use to heat up.
	 * @param fuel The remaining fuel within the air balloon.
	 */
	public void setFuelRemaning(double fuel) {
		this.remainingFuel += fuel;
	}
	
	/**
	 * This method gets the mass of the balloon.
	 * @return returns balloonMass, which is the mass of the balloon.
	 */
	public double getBalloonMass() {
		return balloonMass;
	}
	
	/**
	 * This method sets the mass of the balloon.
	 * @param mass A parameter for the mass of the balloon.
	 */
	public void setBalloonMass(double mass) {
		this.balloonMass = mass;
	}
	
	/** 
	 * This method gets the outside air temperature.
	 * @return airTemp, the temperature of the outside air.
	 */
	public double getOutsideAirTemp() {
		return airTemp; // TODO: Change mock value
	}
	
	/**
	 * This method sets the outside air temperature.
	 * @param temp A parameter for the temperature of the outside air.
	 */
	public void setOutsideAirTemp(double temp) {
		this.airTemp = temp ;
	}
	
	/**
	 * This method gets the fuel burn rate.
	 * @return fuelBurnRate, the rate at which the fuel is burning.
	 */
	public double getFuelBurnRate() {
		return fuelBurnRate;
	}
	/**
	 * This method sets the fuel burn rate
	 * @param rate A parameter for the rate of the fuels burning.
	 */
	public void setFuelBurnRate(double rate) {
		this.fuelBurnRate = rate;
	}
	
	/**
	 * This method gets the balloon temperature.
	 * @return balloonTemp, the temperature of the balloon.
	 */
	public double getBalloonTemp() {
		return balloonTemp;
	}
	
	/**
	 * This method sets the balloon temperature.
	 * @param temp A parameter for the temperature the balloon holds.
	 */
	public void setBalloonTemp(double temp) {
		this.balloonTemp = temp;
	}
	
	/**
	 * This method gets the balloon velocity.
	 * @return balloonVelocity, the velocity of the balloon.
	 */
	public double getVelocity() {
		return balloonVelocity;
	}
	
	/**
	 * This method gets the balloons altitude
	 * @return balloonAltitude, the altitude of the balloon.
	 */
	public double getAltitude() {
		return balloonAltitude;
	}
	
	/**
	 * This method gets the length of the balloons tether.
	 * @return balloonTether, the length of the tether on the balloon.
	 */
	public double getTetherLength() {
		return balloonTether;
	}
	
	/**
	 * This method gets the length of the tether minus the current altitude of the balloon.
	 * @return tetherRemaining, the remains of the balloon's tether length.
	 */
	public double getTetherRemaining() {
		tetherRemaining = balloonTether - balloonAltitude;
		return tetherRemaining;
	}
	
	/**
	 * This method sets the length of the tether.
	 * @param length A parameter for the length of the tether.
	 */
	public void setTetherLength(double length) {
		this.balloonTether = length;
	}
	/**
	 * This method gets the direction of wind in degrees, a number between 0 (inclusive) and 360 (exclusive)
	 * @return windDirection, the direction of the wind
	 */
	public double getWindDirection() {
		return windDirection;
	}
	
	/**
	 * Updates the wind direction by adding the given value on to the current wind direction.
	 * The wind direction must always be between 0 (inclusive) and 360 (exclusive)
	 * @param deg A parameter for the windDirection in degrees
	 */
	public void changeWindDirection(double deg) {
		this.windDirection = (windDirection + 360 + deg) % 360;
	}
	/**
	 * This method gets the number of full minutes that have passed in the simulation.
	 * @return currentTime divided by 60, the number of full minutes passed in the simulation
	 */
	public long getMinutes() {
		return currentTime / 60;
	}
	
	/**
	 * Gets the number of seconds passed the number of full minutes.
	 * @return currentTime mod 60, the number of seconds passed of full minutes.
	 */
	public long getSeconds() {
		return currentTime % 60;
	}
	
	/** 
	 * A call to this method increments the simulation time by 1, representing 1 second of simulated passing time.
	 * The velocity and position of the balloon is updated based on the formulas.
	 */
	public void update() {
		currentTime++;
		
		double getFuelUsed = Math.min(remainingFuel, fuelBurnRate);
		
		remainingFuel = remainingFuel - getFuelUsed;
		
		double changeInBalloonTemp = getFuelUsed + (airTemp - balloonTemp) * HEAT_LOSS_FACTOR;
		
		balloonTemp = balloonTemp + changeInBalloonTemp;
		
		double airDensity = STANDARD_PRESSURE / (GAS_CONSTANT * (airTemp + KEVLINS_AT_0_DEGREES));
		
		double balloonDensity = STANDARD_PRESSURE /  (GAS_CONSTANT * (balloonTemp + KEVLINS_AT_0_DEGREES));

		double forceOfLift = VOLUME_OF_BALLOON_AIR * (airDensity - balloonDensity) * ACCELERATION_FROM_GRAVITY ;
		
		double forceOfGravity = balloonMass * ACCELERATION_FROM_GRAVITY;
		
		double upwardNetForce = forceOfLift - forceOfGravity;
		
		double upwardNetAcceleration = upwardNetForce / balloonMass;
		
		balloonVelocity = balloonVelocity + upwardNetAcceleration;
		
		balloonAltitude = balloonAltitude + balloonVelocity;
		
		balloonAltitude = Math.max(balloonAltitude, 0);
		
		balloonAltitude = Math.min(balloonAltitude, balloonTether);
		
	} 
	
	/**
	 * A call to this method resets the simulation to its initial state (i.e., the state immediately after the constructor was called)
	 * 
	 */
	public void reset() {
		this.airTemp = startingAirTemp;
		this.windDirection = startingWindDirection;
		this.balloonTemp = startingAirTemp;
		this.balloonMass = 0;
	    this.remainingFuel = 0;
	    this.fuelBurnRate = 0;
	    this.balloonTether = 0;
	    this.balloonMass = 0; 
	    this.currentTime = 0;
	    this.balloonAltitude = 0;
	    this.balloonVelocity = 0;		
	}
}