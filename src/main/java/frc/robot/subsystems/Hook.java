/* 2024 Written by Alumiboti FRC 5590 */
package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.HookConstants;

public class Hook extends SubsystemBase {

    public enum HookMode {
        EXTEND,
        RETRACT;
    }

    private DoubleSolenoid solenoid;

    private DoubleSolenoid.Value HOOK_EXTEND = DoubleSolenoid.Value.kForward,
            HOOK_RETRACT = DoubleSolenoid.Value.kReverse;

    public Hook() {
        this.solenoid = new DoubleSolenoid(
                PneumaticsModuleType.REVPH, HookConstants.kForwardCanChannel, HookConstants.kReverseCanChannel);
        this.solenoid.set(Value.kOff);

        // Invert if open & close is backwards
        if (HookConstants.kSolenoidInverted) {
            HOOK_EXTEND = DoubleSolenoid.Value.kReverse;
            HOOK_RETRACT = DoubleSolenoid.Value.kReverse;
        }
    }

    public void set(HookMode grabMode) {
        this.solenoid.set(grabMode == HookMode.EXTEND ? HOOK_EXTEND : HOOK_RETRACT);
    }
}
