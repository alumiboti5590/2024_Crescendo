/* 2024 Written by Alumiboti FRC 5590 */
package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.PS4Controller.Button;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Constants.OIConstants;
import frc.robot.commands.CommandBuilder;
import frc.robot.controllers.XboxController;
import frc.robot.subsystems.SwerveDrive;

/*
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    private final SendableChooser<Command> autoChooser;
    private final CommandBuilder commands = new CommandBuilder();

    // The driver's controller
    XboxController driverController = new XboxController(OIConstants.kDriverControllerPort);
    XboxController operatorController = new XboxController(OIConstants.kOperatorControllerPort);

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        CameraServer.startAutomaticCapture();

        // Configure the button bindings
        configureButtonBindings();

        // The left stick controls translation of the robot.
        // Turning is controlled by the X axis of the right stick.
        SwerveDrive swerveDrive = commands.getSwerveDrive();
        RunCommand swerveDriveCmd = new RunCommand(
                () -> swerveDrive.drive(
                        -MathUtil.applyDeadband(getXSpeed(), OIConstants.kDriveDeadband),
                        -MathUtil.applyDeadband(getYSpeed(), OIConstants.kDriveDeadband),
                        -MathUtil.applyDeadband(driverController.getRightX(), OIConstants.kDriveDeadband),
                        true,
                        true),
                swerveDrive);
        swerveDrive.setDefaultCommand(swerveDriveCmd);

        // This allows PathPlanner to call our actual commands
        NamedCommands.registerCommands(commands.namedCommands());

        autoChooser = AutoBuilder.buildAutoChooser();
        SmartDashboard.putData("Auto Chooser", autoChooser);
    }

    /**
     * Use this method to define your button->command mappings. Buttons can be
     * created by instantiating a {@link edu.wpi.first.wpilibj.GenericHID} or one of its
     * subclasses ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}),
     * and then calling passing it to a {@link JoystickButton}.
     */
    private void configureButtonBindings() {
        // Right Trigger on drive controller locks the drive wheels in place
        new JoystickButton(driverController, Button.kR1.value).whileTrue(commands.plantDriveWheels());

        // Y button controls ground intake
        operatorController.getYButtonTrigger().onTrue(commands.startGroundIntakeSequence());
        operatorController.getYButtonTrigger().onFalse(commands.stopGroundIntakeSequence());

        // Left Bumper shoots speaker
        operatorController.getLeftBumperTrigger().onTrue(commands.shootSpeakerSequence());
        operatorController.getLeftBumperTrigger().onFalse(commands.stopShootSequence());

        // Right bumper shoots amp
        operatorController.getRightBumperTrigger().onTrue(commands.shootAmpSequence());
        operatorController.getRightBumperTrigger().onFalse(commands.stopShootSequence());

        // B button reverses the loader
        operatorController.getBButtonTrigger().onTrue(commands.reverseLoader());
        operatorController.getBButtonTrigger().onFalse(commands.stopLoader());

        // X extends flappy, A retracts it
        operatorController.getXButtonTrigger().onTrue(commands.extendFlappy());
        operatorController.getAButtonTrigger().onTrue(commands.retractFlappy());

        // Start starts the climber
        driverController.getStartButtonTrigger().onTrue(commands.startClimber());
        driverController.getStartButtonTrigger().onFalse(commands.stopClimber());

        // Back reverses the climber
        driverController.getBackButtonTrigger().onTrue(commands.reverseClimber());
        driverController.getBackButtonTrigger().onFalse(commands.stopClimber());

        // X extends the hook, Y retracts the hook
        driverController.getXButtonTrigger().onTrue(commands.extendHook());
        driverController.getYButtonTrigger().onTrue(commands.retractHook());
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        return autoChooser.getSelected();
    }

    public double getXSpeed() {
        double val = driverController.getLeftY();
        if (driverController.getLeftBumper()) {
            val = -val / 1.5;
        }
        return val;
    }

    public double getYSpeed() {
        double val = driverController.getLeftX();
        if (driverController.getLeftBumper()) {
            val = -val / 1.5;
        }
        return val;
    }
}
