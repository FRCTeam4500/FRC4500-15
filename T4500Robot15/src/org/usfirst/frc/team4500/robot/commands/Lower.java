package org.usfirst.frc.team4500.robot.commands;

import org.usfirst.frc.team4500.robot.Robot;
import org.usfirst.frc.team4500.robot.RobotMap;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Lower extends Command {

	Timer time;
	
	public Lower() {
        requires(Robot.newElevator);
		
		// Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.newElevator.winchmotor.set(-0.25); //Auto-Lower set to half speed

    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	return time.get() >= RobotMap.lowerTime; //Returns true when lower time is less than current time
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}