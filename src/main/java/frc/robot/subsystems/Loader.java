/* 2024 Written by Alumiboti FRC 5590 */
package frc.robot.subsystems;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.LoaderConstants;

public class Loader extends SubsystemBase {

    private CANSparkMax motor;
    private DigitalInput loadedSwitch;

    public Loader() {
        motor = new CANSparkMax(LoaderConstants.kLoaderCanId, MotorType.kBrushed);
        motor.setInverted(LoaderConstants.kInvertLoad);
        motor.setIdleMode(IdleMode.kCoast);

        loadedSwitch = new DigitalInput(LoaderConstants.kSwitchId);
    }

    public void set(double percent) {
        motor.set(percent);
    }

    public boolean isNoteLoaded() {
        return this.loadedSwitch.get();
    }
}
