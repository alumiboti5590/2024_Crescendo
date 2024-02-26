/* 2024 Written by Alumiboti FRC 5590 */
package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.TargetConstants;

public class LimeLightTargetingSystem {

    public void EstimateDistance() {
        NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
        NetworkTableEntry ty = table.getEntry("ty");
        double targetOffsetAngle_Vertical = ty.getDouble(0.0);

        // how many degrees back is your limelight rotated from perfectly vertical?
        double limelightMountAngleDegrees = TargetConstants.kLimelightLensAngle;

        // distance from the center of the Limelight lens to the floor
        double limelightLensHeightInches = TargetConstants.kLimelightLensHeight;

        // distance from the target to the floor
        double goalHeightInches = TargetConstants.kSpeakerTagHeight;

        double angleToGoalDegrees = limelightMountAngleDegrees + targetOffsetAngle_Vertical;
        double angleToGoalRadians = angleToGoalDegrees * (3.14159 / 180.0);

        // calculate distance
        double distanceFromLimelightToGoalInches =
                (goalHeightInches - limelightLensHeightInches) / Math.tan(angleToGoalRadians);

        SmartDashboard.putNumber("Distance from Goal", distanceFromLimelightToGoalInches);
    }
}
