/* 2024 Written by Alumiboti FRC 5590 */
package frc.robot.subsystems;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.NoteShooterConstants;

public class NoteShooter extends SubsystemBase {

    private CANSparkMax shooterMotor;

    public NoteShooter() {
        shooterMotor = new CANSparkMax(NoteShooterConstants.kShootCanId, MotorType.kBrushed);
        shooterMotor.setInverted(NoteShooterConstants.kInvertMotor);
        shooterMotor.setIdleMode(IdleMode.kCoast);
    }

    public void setShooter(double percent) {
        shooterMotor.set(percent);
    }
}
