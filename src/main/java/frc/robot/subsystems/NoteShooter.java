/* 2024 Written by Alumiboti FRC 5590 */
package frc.robot.subsystems;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkAbsoluteEncoder.Type;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.NoteShooterConstants;

public class NoteShooter extends SubsystemBase {

    private CANSparkMax shooterMotor;
    private RelativeEncoder m_shooterRelEncoder;
    private AbsoluteEncoder m_shooterAbsEncoder;
    private final boolean m_brushless_shooter;

    public boolean isM_brushless_shooter() {
        return m_brushless_shooter;
    }

    public NoteShooter() {
        m_brushless_shooter = false;
        m_shooterAbsEncoder = null;
        m_shooterRelEncoder = null;

        if (m_brushless_shooter != true) {
            shooterMotor = new CANSparkMax(NoteShooterConstants.kShootCanId, MotorType.kBrushed);
            shooterMotor.setInverted(NoteShooterConstants.kInvertMotor);
            shooterMotor.setIdleMode(IdleMode.kCoast);
        } else {
            // Setup the brushless motor
            shooterMotor = new CANSparkMax(NoteShooterConstants.kShootCanId, MotorType.kBrushless);

            shooterMotor.restoreFactoryDefaults();
            shooterMotor.setInverted(NoteShooterConstants.kInvertMotor);
            shooterMotor.setIdleMode(IdleMode.kCoast);

            m_shooterRelEncoder = shooterMotor.getEncoder();
            m_shooterAbsEncoder = shooterMotor.getAbsoluteEncoder(Type.kDutyCycle);
            m_shooterRelEncoder.setVelocityConversionFactor(NoteShooterConstants.kShootingEncoderVelocityFactor);
            m_shooterAbsEncoder.setVelocityConversionFactor(NoteShooterConstants.kShootingEncoderVelocityFactor);
        }
    }

    public void setShooter(double percent) {
        shooterMotor.set(percent);
    }
}
