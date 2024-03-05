/* 2024 Written by Alumiboti FRC 5590 */
package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticHub;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.FlappyConstants;
import frc.robot.Constants.HookConstants;

public class Flappy extends SubsystemBase {

    public enum FlappyMode {
        EXTEND,
        RETRACT;
    }

    private DoubleSolenoid solenoid;

    private DoubleSolenoid.Value FLAPPY_EXTEND = DoubleSolenoid.Value.kForward,
            FLAPPY_RETRACT = DoubleSolenoid.Value.kReverse;

    public Flappy() {
        PneumaticHub pneumHub = new PneumaticHub(HookConstants.kPneumaticsCanId);
        this.solenoid =
                pneumHub.makeDoubleSolenoid(FlappyConstants.kForwardCanChannel, FlappyConstants.kReverseCanChannel);
        this.solenoid.set(Value.kOff);
    }

    public void set(FlappyMode grabMode) {
        this.solenoid.set(grabMode == FlappyMode.EXTEND ? FLAPPY_EXTEND : FLAPPY_RETRACT);
    }
}
