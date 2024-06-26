/* 2023 Written by Alumiboti FRC 5590 */
package frc.robot.controllers;

import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * A subclass of the WPILib XboxController containing useful additions such as deadzone management
 * and Triggers for Command-based programming. It aims to be a mix between XboxController and
 * CommandXboxController which are provided by WPILib. This class works for both Xbox 360 and Xbox One controllers.
 * @see <a href="https://github.wpilib.org/allwpilib/docs/release/java/edu/wpi/first/wpilibj/XboxController.html">WPILib XboxController</a>
 * @see <a href="https://docs.wpilib.org/en/stable/docs/software/basic-programming/joystick.html">Joysticks on WPILib</a>
 */
public class XboxController extends edu.wpi.first.wpilibj.XboxController {

    /** The minimum value needed by the left and right triggers to be considered "pressed" like a button */
    private double triggerAsTriggerDeadzones = .2;

    private static final double POV_UP = 0, POV_RIGHT = 90, POV_DOWN = 180, POV_LEFT = 270;

    /**
     * Creates a new XboxController listener on the provided port. The port is managed by
     * the USB tab of the Driver Station, and you can 'Rescan' and 'Drag-n-Drop' reorder
     * the controllers to make them fit what is expected.
     * @param port the port of the controller in Driver Station
     */
    public XboxController(int port) {
        super(port);
    }

    /**
     * Creates a new XboxController listener on the provided port with the provided
     * deadzone values. The port is managed by the USB tab of the Driver Station,
     * and you can 'Rescan' and 'Drag-n-Drop' reorder the controllers to make
     * them fit what is expected.
     * @param port the port of the controller in Driver Station
     * @param triggerAsTriggerDeadzones the minimum value to register a trigger as being "pressed"
     */
    public XboxController(int port, double triggerAsTriggerDeadzones) {
        super(port);
        this.triggerAsTriggerDeadzones = triggerAsTriggerDeadzones;
    }

    /**
     * Returns the A button press as a Trigger to be used in Command-based programming.
     * @see <a href="https://docs.wpilib.org/en/stable/docs/software/commandbased/binding-commands-to-triggers.html#trigger-bindings">Binding Commands to Triggers</a>
     * @return the button as a Trigger
     */
    public Trigger getAButtonTrigger() {
        return new Trigger(this::getAButton);
    }

    /**
     * Returns the Back button press as a Trigger to be used in Command-based programming.
     * @see <a href="https://docs.wpilib.org/en/stable/docs/software/commandbased/binding-commands-to-triggers.html#trigger-bindings">Binding Commands to Triggers</a>
     * @return the button as a Trigger
     */
    public Trigger getBackButtonTrigger() {
        return new Trigger(this::getBackButton);
    }

    /**
     * Returns the B button press as a Trigger to be used in Command-based programming.
     * @see <a href="https://docs.wpilib.org/en/stable/docs/software/commandbased/binding-commands-to-triggers.html#trigger-bindings">Binding Commands to Triggers</a>
     * @return the button as a Trigger
     */
    public Trigger getBButtonTrigger() {
        return new Trigger(this::getBButton);
    }

    /**
     * Returns the Left Bumper button press as a Trigger to be used in Command-based programming.
     * @see <a href="https://docs.wpilib.org/en/stable/docs/software/commandbased/binding-commands-to-triggers.html#trigger-bindings">Binding Commands to Triggers</a>
     * @return the button as a Trigger
     */
    public Trigger getLeftBumperTrigger() {
        return new Trigger(this::getLeftBumper);
    }

    /**
     * Returns the Left Stick button press as a Trigger to be used in Command-based programming.
     * @see <a href="https://docs.wpilib.org/en/stable/docs/software/commandbased/binding-commands-to-triggers.html#trigger-bindings">Binding Commands to Triggers</a>
     * @return the button as a Trigger
     */
    public Trigger getLeftStickButtonTrigger() {
        return new Trigger(this::getLeftStickButton);
    }

    /**
     * Returns if the Left Trigger axis is pushed in enough to be considered "pressed", using {@link #triggerAsTriggerDeadzones}
     * as the minimum value required to be considered "pressed".
     * @return if the trigger is pressed or not
     */
    public boolean getLeftTriggerPressed() {
        return this.getLeftTriggerAxis() > triggerAsTriggerDeadzones;
    }

    /**
     * Returns the Left Trigger pressed as a Trigger to be used in Command-based programming.
     * @see <a href="https://docs.wpilib.org/en/stable/docs/software/commandbased/binding-commands-to-triggers.html#trigger-bindings">Binding Commands to Triggers</a>
     * @return the button as a Trigger
     */
    public Trigger getLeftTrigger() {
        return new Trigger(this::getLeftTriggerPressed);
    }

    /**
     * Returns the Right Bumper button press as a Trigger to be used in Command-based programming.
     * @see <a href="https://docs.wpilib.org/en/stable/docs/software/commandbased/binding-commands-to-triggers.html#trigger-bindings">Binding Commands to Triggers</a>
     * @return the button as a Trigger
     */
    public Trigger getRightBumperTrigger() {
        return new Trigger(this::getRightBumper);
    }

    /**
     * Returns the Right Stick button press as a Trigger to be used in Command-based programming.
     * @see <a href="https://docs.wpilib.org/en/stable/docs/software/commandbased/binding-commands-to-triggers.html#trigger-bindings">Binding Commands to Triggers</a>
     * @return the button as a Trigger
     */
    public Trigger getRightStickButtonTrigger() {
        return new Trigger(this::getRightStickButton);
    }

    /**
     * Returns if the Right Trigger axis is pushed in enough to be considered "pressed", using {@link #triggerAsTriggerDeadzones}
     * as the minimum value required to be considered "pressed".
     * @return if the trigger is pressed or not
     */
    public boolean getRightTriggerPressed() {
        return this.getRightTriggerAxis() > triggerAsTriggerDeadzones;
    }

    /**
     * Returns the Right Trigger pressed as a Trigger to be used in Command-based programming.
     * @see <a href="https://docs.wpilib.org/en/stable/docs/software/commandbased/binding-commands-to-triggers.html#trigger-bindings">Binding Commands to Triggers</a>
     * @return the button as a Trigger
     */
    public Trigger getRightTrigger() {
        return new Trigger(this::getLeftTriggerPressed);
    }

    /**
     * Returns the Start button press as a Trigger to be used in Command-based programming.
     * @see <a href="https://docs.wpilib.org/en/stable/docs/software/commandbased/binding-commands-to-triggers.html#trigger-bindings">Binding Commands to Triggers</a>
     * @return the button as a Trigger
     */
    public Trigger getStartButtonTrigger() {
        return new Trigger(this::getStartButton);
    }

    /**
     * Returns the X button press as a Trigger to be used in Command-based programming.
     * @see <a href="https://docs.wpilib.org/en/stable/docs/software/commandbased/binding-commands-to-triggers.html#trigger-bindings">Binding Commands to Triggers</a>
     * @return the button as a Trigger
     */
    public Trigger getXButtonTrigger() {
        return new Trigger(this::getXButton);
    }

    /**
     * Returns the Y button press as a Trigger to be used in Command-based programming.
     * @see <a href="https://docs.wpilib.org/en/stable/docs/software/commandbased/binding-commands-to-triggers.html#trigger-bindings">Binding Commands to Triggers</a>
     * @return the button as a Trigger
     */
    public Trigger getYButtonTrigger() {
        return new Trigger(this::getYButton);
    }

    public boolean getDPadUp() {
        return getPOV() == POV_UP;
    }

    public boolean getDPadRight() {
        return getPOV() == POV_RIGHT;
    }

    public boolean getDPadLeft() {
        return getPOV() == POV_LEFT;
    }

    public boolean getDPadDown() {
        return getPOV() == POV_DOWN;
    }
}
