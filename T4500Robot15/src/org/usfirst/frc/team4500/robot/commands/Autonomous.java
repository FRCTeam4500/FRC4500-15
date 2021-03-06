package org.usfirst.frc.team4500.robot.commands;

import org.usfirst.frc.team4500.robot.RobotMap;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class Autonomous extends CommandGroup {

	Preferences prefs;
	int mode;

	public void chooseAuto() {
		mode = prefs.getInt("Autonomous Mode # (0 to 3)", 0);
	}

	/*
	 * Mode 0 = Just our stuff Mode 1 = Our and one other team's stuff Mode 2 =
	 * Everybody's stuff Mode 3 = Just grab trash can and run
	 */

	public Autonomous() {

		addSequential(new ToggleTopClaw());
		addSequential(new PickupObject()); // Recycle bin is picked up.
		if (mode == 3) {
			addParallel(new StrafeForSeconds(RobotMap.strafeTime, false,
					RobotMap.strafeSpeed)); // Strafe to auto
		}
		if (mode != 3) {
			addSequential(new DriveUntilSonar(RobotMap.sonar1,
					RobotMap.sonarForwardSpeed));
			// TODO: Vision Alignment Command (sequential)
			addSequential(new DriveUntilSonar(RobotMap.sonar2,
					RobotMap.sonarForwardSpeed)); // Positioned to pick up tote
													// 1

			if (mode == 0) {
				addParallel(new PickupObject());
				addParallel(new StrafeForSeconds(RobotMap.strafeTime, false,
						RobotMap.strafeSpeed)); // Pick up tote and run
			}

			if (mode != 0) {

				addParallel(new PickupObject());
				addParallel(new DriveUntilSonar(RobotMap.sonar1,
						RobotMap.sonarForwardSpeed)); // Tote picked up and
														// robot moves to next
														// tote

				// 	Vision Alignment Command (sequential)
				addSequential(new DriveUntilSonar(RobotMap.sonar2,
						RobotMap.sonarForwardSpeed)); // In position to pick up
														// tote 2

				if (mode == 1) {
					addParallel(new PickupObject());
					addParallel(new StrafeForSeconds(RobotMap.strafeTime,
							false, RobotMap.strafeSpeed)); // Pick up tote 2 and
															// run
				}

				if (mode != 1) {

					addParallel(new PickupObject());
					addParallel(new DriveUntilSonar(RobotMap.sonar1,
							RobotMap.sonarForwardSpeed)); // Pick up tote 2 and drive forward

					// Vision Alignment Command (sequential)
					addSequential(new DriveUntilSonar(RobotMap.sonar2,
							RobotMap.sonarForwardSpeed)); // Align to tote 3

					addParallel(new PickupObject());
					addParallel(new StrafeForSeconds(RobotMap.strafeTime,
							false, RobotMap.strafeSpeed)); // Pick up tote 3 and run

				}
			}
		}
	}
}
