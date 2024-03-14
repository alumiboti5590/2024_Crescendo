/* 2024 Written by Alumiboti FRC 5590 */
package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants.ClimberConstants;
import frc.robot.Constants.IntakeConstants;
import frc.robot.Constants.LoaderConstants;
import frc.robot.Constants.ShooterConstants;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Flappy;
import frc.robot.subsystems.Flappy.FlappyMode;
import frc.robot.subsystems.Hook;
import frc.robot.subsystems.Hook.HookMode;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Loader;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.SwerveDrive;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class CommandBuilder {
    private static final double ZERO = 0.0;
    private static final double SPEAKER_SPIN_UP_TIME = 1;

    private final SwerveDrive swerveDrive = new SwerveDrive();
    private final Hook hook = new Hook();
    private final Climber climber = new Climber();
    private final Intake intake = new Intake();
    private final Loader loader = new Loader();
    private final Shooter shooter = new Shooter();
    private final Flappy flappy = new Flappy();

    public CommandBuilder() {}

    // ~~~~~~~~~~~~~~~~
    // Climber Commands
    // ~~~~~~~~~~~~~~~~

    public Command startClimber() {
        return new RunCommand(() -> climber.set(ClimberConstants.kClimbSpeed), climber);
    }

    public Command reverseClimber() {
        return new RunCommand(() -> climber.set(ClimberConstants.kDispenseSpeed), climber);
    }

    public Command stopClimber() {
        return new RunCommand(() -> climber.set(ZERO), climber);
    }

    // ~~~~~~~~~~~~~~~
    // Flappy Commands
    // ~~~~~~~~~~~~~~~

    public Command extendFlappy() {
        return new RunCommand(() -> flappy.set(FlappyMode.EXTEND), flappy);
    }

    public Command retractFlappy() {
        return new RunCommand(() -> flappy.set(FlappyMode.RETRACT), flappy);
    }

    // ~~~~~~~~~~~~~~~
    // Hook Commands
    // ~~~~~~~~~~~~~~~

    public Command extendHook() {
        return new RunCommand(() -> hook.set(HookMode.EXTEND), hook);
    }

    public Command retractHook() {
        return new RunCommand(() -> hook.set(HookMode.RETRACT), hook);
    }

    // ~~~~~~~~~~~~~~~
    // Intake Commands
    // ~~~~~~~~~~~~~~~

    public Command startIntake() {
        return new RunCommand(() -> intake.set(IntakeConstants.kIntakeSpeed), intake);
    }

    public Command stopIntake() {
        return new RunCommand(() -> intake.set(ZERO), intake);
    }

    // ~~~~~~~~~~~~~~~
    // Loader Commands
    // ~~~~~~~~~~~~~~~

    public Command startLoaderForIntake() {
        return new RunCommand(() -> loader.set(LoaderConstants.kIntakeSpeed), loader);
    }

    public Command startLoaderForShooter() {
        return new RunCommand(() -> loader.set(LoaderConstants.kLoadShooterSpeed), loader);
    }

    public Command reverseLoader() {
        return new RunCommand(() -> loader.set(LoaderConstants.kExhaustSpeed), loader);
    }

    public Command stopLoader() {
        return new RunCommand(() -> loader.set(ZERO), loader);
    }

    // ~~~~~~~~~~~~~~~~
    // Shooter Commands
    // ~~~~~~~~~~~~~~~~

    public Command startShooterForAmp() {
        return new RunCommand(() -> shooter.set(ShooterConstants.kShootAmpSpeed), shooter);
    }

    public Command startShooterForSpeaker() {
        return new RunCommand(() -> shooter.set(ShooterConstants.kShootSpeakerSpeed), shooter);
    }

    public Command reverseShooter() {
        return new RunCommand(() -> shooter.set(ShooterConstants.kReverseSpeed), shooter);
    }

    public Command stopShooter() {
        return new RunCommand(() -> shooter.set(ZERO), shooter);
    }

    // ~~~~~~~~~~~~~~~~~~~~~
    // Swerve Drive Commands
    // ~~~~~~~~~~~~~~~~~~~~~

    /**
     * Turns all of the wheels inwards so we can't be easily pushed
     */
    public Command plantDriveWheels() {
        return new RunCommand(() -> swerveDrive.setX(), swerveDrive);
    }

    // ~~~~~~~~~~~~~~~~~
    // Complex Sequences
    // ~~~~~~~~~~~~~~~~~

    public Command startGroundIntakeSequence() {
        return startIntake().alongWith(startLoaderForIntake());
    }

    public Command stopGroundIntakeSequence() {
        return Commands.parallel(stopIntake(), stopLoader())
                .withTimeout(.1)
                // Backfeed the shooter for a moment to prevent the note from getting
                // stuck before launching it.
                .andThen(reverseShooter().alongWith(reverseLoader()).withTimeout(.2))
                .andThen(stopShooter().alongWith(stopLoader()));
    }

    public Command shootAmpSequence() {
        return startShooterForAmp().withTimeout(.5).andThen(startLoaderForShooter());
    }

    public Command shootSpeakerSequence() {
        return startShooterForSpeaker()
                .withTimeout(SPEAKER_SPIN_UP_TIME)
                .andThen(startLoaderForShooter())
                .withTimeout(SPEAKER_SPIN_UP_TIME + .1);
    }

    public Command stopShootSequence() {
        return stopShooter().alongWith(stopLoader());
    }

    // ~~~~~~~~~~~~
    // Auto Actions
    // ~~~~~~~~~~~~

    public Command autoShootSpeakerSequence() {
        return (this.shootSpeakerSequence()
                        .withTimeout(SPEAKER_SPIN_UP_TIME + .3)
                        .andThen(new WaitCommand(.1)))
                .withTimeout(SPEAKER_SPIN_UP_TIME + .2);
    }

    public Command autoStopShootSequence() {
        return (stopShootSequence()).withTimeout(.05);
    }

    public Command autoStartGroundIntakeSequence() {
        return (startGroundIntakeSequence()).withTimeout(.05);
    }

    public Command autoStopGroundIntakeSequence() {
        return (stopGroundIntakeSequence()).withTimeout(.3);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Getters for the subsystems
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~

    public Climber getClimber() {
        return this.climber;
    }

    public Flappy getFlappy() {
        return this.flappy;
    }

    public Hook getHook() {
        return this.hook;
    }

    public Intake getIntake() {
        return this.intake;
    }

    public Loader getLoader() {
        return this.loader;
    }

    public Shooter getShooter() {
        return this.shooter;
    }

    public SwerveDrive getSwerveDrive() {
        return this.swerveDrive;
    }

    public Map<String, Command> namedCommands() {
        Map<String, Command> commandMap = new HashMap<String, Command>();

        for (Method method : this.getClass().getDeclaredMethods()) {
            String name = method.getName();
            if (method.getReturnType() == Command.class) {
                try {
                    Command result = (Command) method.invoke(this);
                    commandMap.put(name, result);
                } catch (IllegalAccessException e) {
                } catch (IllegalArgumentException e) {
                } catch (InvocationTargetException e) {
                }
            }
        }
        return commandMap;
    }
}
