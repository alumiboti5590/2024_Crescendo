/* 2024 Written by Alumiboti FRC 5590 */
package frc.robot.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.wpi.first.wpilibj2.command.Command;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class CommandBuilderTest {

    @Test
    public void givesProperNamedCommands() {
        CommandBuilder cb = new CommandBuilder();
        Map<String, Command> namedCommands = cb.namedCommands();
        assertEquals(namedCommands, null);
    }
}
