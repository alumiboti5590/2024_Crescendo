/* 2024 Written by Alumiboti FRC 5590 */
package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ClawSubsystem extends SubsystemBase {

    public enum ClawMode {
        RAISE,
        LOWER;
    }

    private DoubleSolenoid clawLiftSolenoid;

    private DoubleSolenoid.Value CLAW_LIFT = DoubleSolenoid.Value.kForward, CLAW_LOWER = DoubleSolenoid.Value.kReverse;

    public ClawSubsystem() {
        this.clawLiftSolenoid = new DoubleSolenoid(PneumaticsModuleType.REVPH, 0, 1);
        this.clawLiftSolenoid.set(Value.kOff);

        // Invert if open & close is backwards
        if (false) {
            CLAW_LIFT = DoubleSolenoid.Value.kReverse;
            CLAW_LOWER = DoubleSolenoid.Value.kReverse;
        }
    }

    public void setLiftMode(ClawMode grabMode) {
        this.clawLiftSolenoid.set(grabMode == ClawMode.RAISE ? CLAW_LIFT : CLAW_LOWER);
    }
}
