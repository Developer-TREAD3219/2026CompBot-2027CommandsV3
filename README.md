** NOTE: If we do not change from Commands V2 to V3, we won't need to change structure of Command files and Subsystem files.

WPILib docs state that both the v2 and v3 libraries will be supported side-by-side for several seasons. 
The v2 library will only be removed when usage reporting shows nearly zero usage.

Should we change now or wait?
   

Steps used to import 2026 project into 2027 for conversion to Systemcore from roboRIO:

1. Copy 2026 project to a new folder
2. Upgrade to Gradle 9.4.1
   - Open project in VS Code 2026
   - Open gradle\wrapper\gradle-wrapper.properties file
   - Change version to 9.4.1 in the distributionUrl
   - Save changes
   - Refresh the gradle project by closing the IDE and then re-opening
   - Verify the version is updated by running "./gradlew --version" in Terminal window
4. Remove unused imports, save project
5. Open copied project in VS Code 2027
6. Say Yes to conversion, Click Next when prompted for build.gradle file
7. Install vendor dependencies:
   - CTRE Phoenix (v6)
   - PathplannerLib
   - RevLib
   - Commands v3 (need to uninstall v2 first)(this will break xxxx)
9. Verify Java version is 25 or higher. (type "java -version" in Terminal window
10. Add libraries for:
    - sendableChooser replaced by Selectable from org.wpilib.tunable.util.*    requires >= Elastic alpha 9
    - SmartDashboard replace with Network Tables for putData, putBoolean, putNumber, putString (create class and method to call?)
    -    can use Telemetry.Log("my data", myData); if only displaying. (org.wpilib.telemetry.Telemetry)
    -    if need to retrieve data, use Tunable instead of Telemetry (org.wpilib.tunable.util.*)

changes:
- remove robotInit() from Robot.java
- replace "positionConversionFactor" with ??? (see SPARKMAX ChangeLog on https://github.com/wpilibsuite/SystemcoreTesting/blob/main/REV.md)
- ??silenceJoystickConnectionWarning replaced by ????
- XboxController replace by org.wpilib.driverstation.Gamepad
- References to CAN need CAN Port and Device ID (SparkMax, Talon, etc)
- SparkMax need to be configured for a motor type of VOLTAGE, and use the .SetVoltage instead of .Set for speed (What voltage should be used? 12V?)
- Relative encoders used with SparkMax need to add .Get() to .GetPosition()   For example, climberEncoder.getPosition().get()
- Lots of changes to DriveSubsystem for swerve.  E.g. SwerveModuleState renamed to SwerveModuleVelocity, .getState() gone.
- Limelight - removed Jackson (com.fasterxml.jackson) and replaced it with Avaje Jsonb (can convert or manually import FasterXML libraries)
- Swerve - SwerveModuleState was renamed to SwerveModuleVelocity (and ChassisSpeeds is now ChassisVelocities). Google "wpilib 2027 maxswervemodule.getstate migration to velocity java example"
- org.wpilib.hardware.hal.FRCNetComm and HAL.report() replaced by new Usage Reporting API in org.wpilib.util (org.wpilib.util.UsageReporting)
   Usage reporting is strictly telemetry collected by the Field Management System (FMS) so FIRST can track hardware and library adoption across teams.
   Removing or updating the call has zero effect on your swerve module's kinematics, SPARK MAX/Flex motor control, or odometry.
  Should we just remove it?

  COMMAND V3 SPECIFIC CHANGES:
- Commands no longer extend Command, now extend Mechanism (import org.wpilib.command3.Mechanism;)
- Commands must be named, even InstantCommands (e.g. add .named("sample name") to end of .setDefaultCommand)
- Subsystems now use "implements Mechanism" instead of "extends SubsystemBase"

