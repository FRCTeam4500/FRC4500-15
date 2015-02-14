package org.usfirst.frc.team4500.robot.subsystems;

import org.usfirst.frc.team4500.robot.RobotMap;
import org.usfirst.frc.team4500.robot.commands.StopBackClaw;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class BackClaw extends Subsystem {
	Talon backClaw = new Talon(RobotMap.backClawMotor);
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        setDefaultCommand(new StopBackClaw());
    }
    
    public void raise() {
    	backClaw.set(.75);	
    }
    
    public void lower() {
    	backClaw.set(-.4);
    }
    
    public void stop() {
    	backClaw.set(.1);
    }
}

