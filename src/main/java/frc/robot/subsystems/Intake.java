/* 2024 Written by Alumiboti FRC 5590 */
package frc.robot.subsystems;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeConstants;

public class Intake extends SubsystemBase {

    private CANSparkMax motor;

    public Intake() {
        motor = new CANSparkMax(IntakeConstants.kIntakeCanId, MotorType.kBrushless);
        motor.setInverted(IntakeConstants.kInvertIntake);
        motor.setIdleMode(IdleMode.kBrake);
    }

    public void set(double percent) {
        motor.set(percent);
    }
}
