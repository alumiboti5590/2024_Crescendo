/* 2024 Written by Alumiboti FRC 5590 */
package frc.robot.subsystems;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.NoteLoaderConstants;

public class NoteLoader extends SubsystemBase {

    private CANSparkMax loaderMotor;

    public NoteLoader() {
        loaderMotor = new CANSparkMax(NoteLoaderConstants.kLoaderCanId, MotorType.kBrushed);
        loaderMotor.setInverted(NoteLoaderConstants.kInvertLoad);
        loaderMotor.setIdleMode(IdleMode.kBrake);
    }

    public void setLoader(double percent) {
        loaderMotor.set(percent);
    }
}
