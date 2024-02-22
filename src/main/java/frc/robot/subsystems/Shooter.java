/* 2024 Written by Alumiboti FRC 5590 */
package frc.robot.subsystems;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ShooterConstants;

public class Shooter extends SubsystemBase {

    private CANSparkMax motor;

    public Shooter() {
        motor = new CANSparkMax(ShooterConstants.kShootCanId, MotorType.kBrushed);
        motor.setInverted(ShooterConstants.kInvertMotor);
        motor.setIdleMode(IdleMode.kCoast);
    }

    public void set(double percent) {
        motor.set(percent);
    }
}
