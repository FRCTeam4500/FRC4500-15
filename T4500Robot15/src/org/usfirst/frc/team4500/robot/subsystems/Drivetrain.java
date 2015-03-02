package org.usfirst.frc.team4500.robot.subsystems;

import org.usfirst.frc.team4500.robot.RobotMap;
import org.usfirst.frc.team4500.robot.commands.DriveWithJoystick;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import extension.CorrectedGyro;

/**
 *
 */
public class Drivetrain extends Subsystem {

	// Drive Motors
	Talon flMotor = new Talon(RobotMap.flmotorPort);
	Talon frMotor = new Talon(RobotMap.frmotorPort);
	Talon blMotor = new Talon(RobotMap.blmotorPort);
	Talon brMotor = new Talon(RobotMap.brmotorPort);

	public Gyro gyroscope = new CorrectedGyro(0, 1.0);
	public RobotDrive drive = new RobotDrive(flMotor, blMotor, frMotor, brMotor);
	AnalogInput sonar = new AnalogInput(1);

	public Drivetrain() {
		gyroscope.setSensitivity(7.850195562631942);
		gyroscope.initGyro();
		gyroscope.reset();
		sonar.setOversampleBits(4);
		sonar.setAverageBits(4);
	}

	public void initDefaultCommand() {
		setDefaultCommand(new DriveWithJoystick());
	}

	int tick = 0;
	int sampleSize = 25;
	double[] sample = new double[sampleSize+1];
	double median;

	public double getSonarInches() {
		/*sample[tick] = getRawSonarInches();

		if (tick == sampleSize) {
			Arrays.sort(sample);
			median = sample[sampleSize / 2];
		}

		if (tick == sampleSize) {
			tick = 0;
		} else {
			tick++;
		}*/

		return getRawSonarInches();
	}

	public double getRawSonarInches() {
		SmartDashboard.putNumber("Sonar", sonar.getAverageVoltage()/0.009766);
		return sonar.getAverageVoltage() / 0.009766;
	}

	/**
	 * Resets the gyroscope reading
	 */
	public void resetGyro() {
		gyroscope.reset();
	}

	public double getAngle() {
		return 1.04 * gyroscope.getAngle();
	}

	/**
	 * Drive the robot using x, y, and rotation values. 
	 * Takes: 
	 * @param double x
	 * @param double y
	 * @param double rotation
	 */
	
	public void driveWithJoystick(double x, double y, double rotation) {
		SmartDashboard.putNumber("Gyro Angle", getAngle());
		SmartDashboard.putNumber("Ultrasonic distance", getSonarInches());
		drive.mecanumDrive_Cartesian(x, y, rotation, getAngle());
	}

	public void invertDriveMotors() {
		drive.setInvertedMotor(MotorType.kFrontRight, true);
		drive.setInvertedMotor(MotorType.kRearRight, true);
	}

	public void driveBack(double speed) {
		drive.mecanumDrive_Cartesian(0, -speed, 0, 0);
	}

	/**
	 * Stops the robot from driving
	 */
	public void stop() {
		drive.mecanumDrive_Cartesian(0, 0, 0, 0);
	}

	/**
	 * Drives robot forward at a specified speed
	 * 
	 * @param speed
	 *            (0 to 1)
	 */
	public void driveForward(double speed) {
		drive.mecanumDrive_Cartesian(0, speed, 0, 0);
	}

	private boolean firstRun = true;
	private double correctAngle = 0;
	private double deviation;
	private double correction;

	/**
	 * Takes the gyro value the first time it is called, and uses that as a
	 * correct orientation, and tries to keep the robot facing that direction as
	 * it drives in a straight line. NOTE: Call 'ResetFirstRun()' each time you
	 * finish running this function to reset correctAngle
	 * 
	 * @param speed
	 *            : Speed to drive in line
	 * @param gy
	 *            : Gyro to use for orientation
	 * @param RoboDrive
	 *            : RobotDrive object to drive with
	 */
	public void driveStraight(double speed, Gyro gy, RobotDrive roboDrive) {
		if (firstRun) {
			correctAngle = gy.getAngle();
			firstRun = false;
		}

		deviation = gy.getAngle() - correctAngle;
		correction = -deviation / 20; // 10 is an arbitrary value, subject to
										// change. This means that if the robot
										// strayed 10 degrees off course, it
										// would hit the maximum correction rate
										// of z = 1.
		final double SPEEDLIMIT = .35;
		if(correction >= SPEEDLIMIT	) { //Speed limit
			correction = SPEEDLIMIT;
		}
		if(correction <= -SPEEDLIMIT) {
			correction = -SPEEDLIMIT;
		}
		roboDrive.mecanumDrive_Cartesian(0, speed, correction, 0);
	}
	
	/**
	 * Overload method which allows an angle to drive at to be specified
	 * @param speed
	 * @param gy
	 * @param roboDrive
	 * @param atAngle: Angle to be driven at.
	 */
	public void driveStraight(double speed, Gyro gy, RobotDrive roboDrive, double atAngle) {
		firstRun = false;
		correctAngle = atAngle;
		driveStraight(speed, gy, roboDrive);
	}
	
	/**
	 * Uses the driveStraight function to instead just turn to a gyroscopic angle
	 * @param angle: Angle to face
	 * @param gy: Gyro to use
	 * @param roboDrive: RobotDrive object to use
	 */
	public void turnToFace(double angle, Gyro gy, RobotDrive roboDrive) {
		driveStraight(0, gy, roboDrive, angle);
	}

	/**
	 * Call this each time you finish using driveStraight to reset correctAngle
	 */
	public void ResetFirstRun() {
		firstRun = true;
	}

	public void turnDegrees(double angle, double speed, Gyro gy,
			RobotDrive roboDrive) {
		double initialAngle = gy.getAngle();
		double destinationAngle = initialAngle + angle;

		if (angle > 0) {
			while (gy.getAngle() < destinationAngle) {
				roboDrive.mecanumDrive_Cartesian(0, 0, speed, 0);
			}
		}
		if (angle < 0) {
			while (gy.getAngle() > destinationAngle) {
				roboDrive.mecanumDrive_Cartesian(0, 0, -speed, 0);
			}
		}
	}

	/**
	 * 
	 * @param range
	 *            (in inches)
	 * @return
	 */
	public boolean isInRange(double range) {
		if (getSonarInches() < range) {
			return true;
		} else
			return false;
	}

	/**
	 * Makes the robot strafe at a given speed and direction
	 * 
	 * @param speed
	 * @param direction
	 *            : true = right, false = left.
	 */
	public void strafe(double speed, boolean direction) {
		double dir;
		if (direction)
			dir = 1;
		else
			dir = -1;
		drive.mecanumDrive_Cartesian(dir * speed, 0, 0, 0);
	}
}