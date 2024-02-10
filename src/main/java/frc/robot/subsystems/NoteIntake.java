/* 2024 Written by Alumiboti FRC 5590 */
package frc.robot.subsystems;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.NoteIntakeConstants;

public class NoteIntake extends SubsystemBase {

    private CANSparkMax intakeMotor;

    public NoteIntake() {
        intakeMotor = new CANSparkMax(NoteIntakeConstants.kIntakeCanId, MotorType.kBrushed);
        intakeMotor.setInverted(false);
        intakeMotor.setIdleMode(IdleMode.kBrake);
    }

    public void setIntake(double percent) {
        intakeMotor.set(percent);
    }
}
