package org.usfirst.frc.team4500.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class PickupTote extends CommandGroup {
    
    public  PickupTote() {
        addSequential(new HalfLower());
        addSequential(new OpenBottomClaw());
        addSequential(new HalfLower());
        addSequential(new GrabTote());
        addSequential(new Lift());
    }
}