/* 2024 Written by Alumiboti FRC 5590 */
package frc.robot.subsystems;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ClimberConstants;

public class Climber extends SubsystemBase {

    private CANSparkMax motor;

    public Climber() {
        motor = new CANSparkMax(ClimberConstants.kCanId, MotorType.kBrushless);
        motor.setInverted(ClimberConstants.kInvertMotor);
        motor.setIdleMode(IdleMode.kCoast);
    }

    public void set(double percent) {
        motor.set(percent);
    }
}
