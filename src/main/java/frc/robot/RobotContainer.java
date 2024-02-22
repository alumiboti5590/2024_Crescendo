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
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.SwerveControllerCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Constants.AutoConstants;
import frc.robot.Constants.ClimberConstants;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.IntakeConstants;
import frc.robot.Constants.LoaderConstants;
import frc.robot.Constants.OIConstants;
import frc.robot.Constants.ShooterConstants;
import frc.robot.controllers.XboxController;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Loader;
import frc.robot.subsystems.Shooter;
import java.util.List;

/*
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    private static final double ZERO = 0.0;

    private final DriveSubsystem robotDrive = new DriveSubsystem();
    //     private final ClawSubsystem clawClimber = new ClawSubsystem();
    private final Climber climber = new Climber();
    private final Intake intake = new Intake();
    private final Loader loader = new Loader();
    private final Shooter shooter = new Shooter();

    // The driver's controller
    XboxController driverController = new XboxController(OIConstants.kDriverControllerPort);
    XboxController operatorController = new XboxController(OIConstants.kOperatorControllerPort);

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        // Configure the button bindings
        configureButtonBindings();

        // The left stick controls translation of the robot.
        // Turning is controlled by the X axis of the right stick.
        RunCommand swerveDriveCmd = new RunCommand(
                () -> robotDrive.drive(
                        -MathUtil.applyDeadband(driverController.getLeftY(), OIConstants.kDriveDeadband),
                        -MathUtil.applyDeadband(driverController.getLeftX(), OIConstants.kDriveDeadband),
                        -MathUtil.applyDeadband(driverController.getRightX(), OIConstants.kDriveDeadband),
                        true,
                        true),
                robotDrive);
        robotDrive.setDefaultCommand(swerveDriveCmd);
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
        new JoystickButton(driverController, Button.kR1.value)
                .whileTrue(new RunCommand(() -> robotDrive.setX(), robotDrive));

        // Basic Actions that the robot can take
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        Runnable startIntake = () -> intake.set(IntakeConstants.kIntakeSpeed),
                startIntakeLoader = () -> loader.set(LoaderConstants.kIntakeSpeed),
                startShooterLoader = () -> loader.set(LoaderConstants.kLoadShooterSpeed),
                stopIntake = () -> intake.set(ZERO),
                stopLoader = () -> loader.set(ZERO),
                startShooter = () -> shooter.set(ShooterConstants.kShootSpeakerSpeed),
                stopShooter = () -> shooter.set(ZERO),
                reverseShooter = () -> shooter.set(ShooterConstants.kReverseSpeed),
                startClimb = () -> climber.set(ClimberConstants.kClimbSpeed),
                stopClimb = () -> climber.set(ZERO);

        // Actions that combine to form sequences and Commands
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        // Start the intake and the loader to pick up notes off the ground
        Command startGroundIntake = run(startIntake, intake)
                .alongWith(run(startIntakeLoader, loader))
                .unless(loader::isNoteLoaded);
        // Stop the intake and feeder at the same time
        Command stopGroundIntake = Commands.parallel(run(stopIntake, intake), run(stopLoader, loader))
                .withTimeout(.1)
                // Backfeed the shooter for a moment to prevent the note from getting
                // stuck before launching it.
                .andThen(run(reverseShooter, shooter).withTimeout(.2))
                .andThen(run(stopShooter, shooter));

        // Start the shooter for a moment to spin up, then spin the loader to feed the note
        Command shootSequenceCmd = run(startShooter, shooter).withTimeout(.7).andThen(run(startShooterLoader, loader));
        // Stop both the shooter and the feeder
        Command stopShooterCmd = run(stopShooter, shooter).alongWith(run(stopLoader, loader));

        // Loading from feeder system
        operatorController.getYButtonTrigger().onTrue(startGroundIntake);
        operatorController.getYButtonTrigger().onFalse(stopGroundIntake);

        operatorController.getStartButtonTrigger().onTrue(shootSequenceCmd);
        operatorController.getStartButtonTrigger().onFalse(stopShooterCmd);
        operatorController.getBackButtonTrigger().onTrue(run(stopShooter, shooter));

        driverController.getStartButtonTrigger().onTrue(run(startClimb, climber));
        driverController.getStartButtonTrigger().onFalse(run(stopClimb, climber));
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
                robotDrive::getPose, // Functional interface to feed supplier
                DriveConstants.kDriveKinematics,

                // Position controllers
                new PIDController(AutoConstants.kPXController, 0, 0),
                new PIDController(AutoConstants.kPYController, 0, 0),
                thetaController,
                robotDrive::setModuleStates,
                robotDrive);

        // Reset odometry to the starting pose of the trajectory.
        robotDrive.resetOdometry(exampleTrajectory.getInitialPose());

        // Run path following command, then stop at the end.
        return swerveControllerCommand.andThen(() -> robotDrive.drive(0, 0, 0, false, false));
    }

    private static Command run(Runnable action, Subsystem... subsystems) {
        return new RunCommand(action, subsystems);
    }
}
