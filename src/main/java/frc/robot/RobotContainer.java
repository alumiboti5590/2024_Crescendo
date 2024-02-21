/* 2024 Written by Alumiboti FRC 5590 */
package frc.robot;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.PS4Controller.Button;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SwerveControllerCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Constants.AutoConstants;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.NoteIntakeConstants;
import frc.robot.Constants.NoteLoaderConstants;
import frc.robot.Constants.NoteShooterConstants;
import frc.robot.Constants.OIConstants;
import frc.robot.controllers.XboxController;
import frc.robot.subsystems.ClawSubsystem;
import frc.robot.subsystems.ClawSubsystem.ClawMode;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.NoteIntake;
import frc.robot.subsystems.NoteLoader;
import frc.robot.subsystems.NoteShooter;
import java.util.List;

/*
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    // The robot's subsystems
    private final DriveSubsystem m_robotDrive = new DriveSubsystem();
    private final ClawSubsystem m_clawClimber = new ClawSubsystem();
    private final NoteIntake m_noteIntake = new NoteIntake();
    private final NoteLoader m_noteLoader = new NoteLoader();
    private final NoteShooter m_noteShooter = new NoteShooter();

    // The driver's controller
    XboxController m_driverController = new XboxController(OIConstants.kDriverControllerPort);
    XboxController m_operatorController = new XboxController(OIConstants.kOperatorControllerPort);

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        // Configure the button bindings
        configureButtonBindings();

        // The left stick controls translation of the robot.
        // Turning is controlled by the X axis of the right stick.
        RunCommand swerveDriveCmd = new RunCommand(
                () -> m_robotDrive.drive(
                        -MathUtil.applyDeadband(m_driverController.getLeftY(), OIConstants.kDriveDeadband),
                        -MathUtil.applyDeadband(m_driverController.getLeftX(), OIConstants.kDriveDeadband),
                        -MathUtil.applyDeadband(m_driverController.getRightX(), OIConstants.kDriveDeadband),
                        true,
                        true),
                m_robotDrive);

        // Configure default commands
        m_robotDrive.setDefaultCommand(swerveDriveCmd);

        // m_driverController.getAButtonTrigger().whileTrue(m_robotDrive.alignToAprilTag("0"));

        // Claw Climber Command Mappings

        RunCommand clawRaiseCmd = new RunCommand(() -> m_clawClimber.setLiftMode(ClawMode.RAISE), m_clawClimber),
                clawLowerCmd = new RunCommand(() -> m_clawClimber.setLiftMode(ClawMode.LOWER), m_clawClimber);

        m_driverController.getBButtonTrigger().onTrue(clawRaiseCmd);
        m_driverController.getXButtonTrigger().onTrue(clawLowerCmd);

        // /end Claw Climber Command Mappings

        // Intake Command Mappings

        RunCommand stopIntakeCmd = new RunCommand(() -> m_noteIntake.setIntake(0), m_noteIntake),
                forwardIntakeCmd =
                        new RunCommand(() -> m_noteIntake.setIntake(NoteIntakeConstants.kIntakeSpeed), m_noteIntake),
                reverseIntakeCmd =
                        new RunCommand(() -> m_noteIntake.setIntake(NoteIntakeConstants.kExhaustSpeed), m_noteIntake);

        m_noteIntake.setDefaultCommand(stopIntakeCmd);
        m_operatorController.getYButtonTrigger().whileTrue(forwardIntakeCmd);
        m_operatorController.getAButtonTrigger().whileTrue(reverseIntakeCmd);

        // /end Intake Command Mappings

        // Loader Command Mappings

        RunCommand stopLoaderCmd = new RunCommand(() -> m_noteLoader.setLoader(0), m_noteLoader),
                forwardLoaderCmd =
                        new RunCommand(() -> m_noteLoader.setLoader(NoteLoaderConstants.kIntakeSpeed), m_noteLoader),
                reverseLoaderCmd =
                        new RunCommand(() -> m_noteLoader.setLoader(NoteLoaderConstants.kExhaustSpeed), m_noteLoader);

        m_noteLoader.setDefaultCommand(stopLoaderCmd);
        m_operatorController.getXButtonTrigger().whileTrue(forwardLoaderCmd);
        m_operatorController.getBButtonTrigger().whileTrue(reverseLoaderCmd);

        RunCommand stopShooterCmd = new RunCommand(() -> m_noteShooter.setShooter(0), m_noteShooter),
                forwardShooterCmd =
                        new RunCommand(() -> m_noteShooter.setShooter(Constants.NoteShooterConstants.kShootSpeed), m_noteShooter),
                reverseShooterCmd =
                        new RunCommand(
                                () -> m_noteShooter.setShooter(Constants.NoteShooterConstants.kReverseSpeed), m_noteShooter);

        m_noteShooter.setDefaultCommand(stopShooterCmd);
        m_operatorController.getStartButtonTrigger().whileTrue(forwardShooterCmd);
        m_operatorController.getBackButtonTrigger().whileTrue(reverseShooterCmd);

        // /end Loader Command Mappings
    }

    /**
     * Use this method to define your button->command mappings. Buttons can be
     * created by
     * instantiating a {@link edu.wpi.first.wpilibj.GenericHID} or one of its
     * subclasses ({@link
     * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then calling
     * passing it to a
     * {@link JoystickButton}.
     */
    private void configureButtonBindings() {
        new JoystickButton(m_driverController, Button.kR1.value)
                .whileTrue(new RunCommand(() -> m_robotDrive.setX(), m_robotDrive));
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        // Create config for trajectory
        TrajectoryConfig config = new TrajectoryConfig(
                        AutoConstants.kMaxSpeedMetersPerSecond, AutoConstants.kMaxAccelerationMetersPerSecondSquared)
                // Add kinematics to ensure max speed is actually obeyed
                .setKinematics(DriveConstants.kDriveKinematics);

        // An example trajectory to follow. All units in meters.
        Trajectory exampleTrajectory = TrajectoryGenerator.generateTrajectory(
                // Start at the origin facing the +X direction
                new Pose2d(0, 0, new Rotation2d(0)),
                // Pass through these two interior waypoints, making an 's' curve path
                List.of(new Translation2d(1, 1), new Translation2d(2, -1)),
                // End 3 meters straight ahead of where we started, facing forward
                new Pose2d(3, 0, new Rotation2d(0)),
                config);

        var thetaController = new ProfiledPIDController(
                AutoConstants.kPThetaController, 0, 0, AutoConstants.kThetaControllerConstraints);
        thetaController.enableContinuousInput(-Math.PI, Math.PI);

        SwerveControllerCommand swerveControllerCommand = new SwerveControllerCommand(
                exampleTrajectory,
                m_robotDrive::getPose, // Functional interface to feed supplier
                DriveConstants.kDriveKinematics,

                // Position controllers
                new PIDController(AutoConstants.kPXController, 0, 0),
                new PIDController(AutoConstants.kPYController, 0, 0),
                thetaController,
                m_robotDrive::setModuleStates,
                m_robotDrive);

        // Reset odometry to the starting pose of the trajectory.
        m_robotDrive.resetOdometry(exampleTrajectory.getInitialPose());

        // Run path following command, then stop at the end.
        return swerveControllerCommand.andThen(() -> m_robotDrive.drive(0, 0, 0, false, false));
    }
}
