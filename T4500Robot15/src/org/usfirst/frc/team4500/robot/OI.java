package org.usfirst.frc.team4500.robot;

import org.usfirst.frc.team4500.robot.commands.ControlActuator;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.Trigger;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    //// CREATING BUTTONS
    // One type of button is a joystick button which is any button on a joystick.
    // You create one by telling it which joystick it's on and which button
    // number it is.
    // Joystick stick = new Joystick(port);
    // Button button = new JoystickButton(stick, buttonNumber);
    
   
	Joystick drivestick = new Joystick(1);
	Trigger mainTrigger = new JoystickButton(drivestick, 1);
	
	public OI() {
		mainTrigger.whenActive(new ControlActuator());
	}
	
	//Made the joystick always return 0 for now so we can test the pneumatics
	public double getX() {
		return 0;//Math.abs(drivestick.getX()) > RobotMap.joyDead ? drivestick.getX() : 0;
	}
	
	public double getY() {
		return 0;//Math.abs(drivestick.getY()) > RobotMap.joyDead ? drivestick.getY() : 0;		
	}
	
	public double getTwist() {
		return 0;//Math.abs(drivestick.getTwist()) > RobotMap.joyDead ? drivestick.getTwist() : 0;
	}
	
	public boolean getTrigger() {
		return drivestick.getRawButton(1);
	}
	
}
    
