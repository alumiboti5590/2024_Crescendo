# 2024 FIRST Crescendo Robot Code

Team 5590's 2024 FRC robot code for the FIRST Crescendo robot. The code is written in Java and is based off of [RevRobotic's MAXSwerve Robot template code](https://github.com/REVrobotics/MAXSwerve-Java-Template).

A template project for an FRC swerve drivetrain that uses REV MAXSwerve Modules.

Note that this is meant to be used with a drivetrain composed of four MAXSwerve Modules, each configured with two SPARKS MAX, a NEO as the driving motor, a NEO 550 as the turning motor, and a REV Through Bore Encoder as the absolute turning encoder.

To get started, make sure you have calibrated the zero offsets for the absolute encoders in the Hardware Client using the `Absolute Encoder` tab under the associated turning SPARK MAX devices.

The code is divided into several packages, each responsible for a different aspect of the robot function. This README document explains the function of each package, some of the variable naming conventions used, and setup instructions. Additional information about each specific class can be found in that class's `.java` file.

Note that terminal commands include the `$`, but should not be included int the command. They simply show that they are a terminal command and not programming code. Exclude the `$` when you run it.
