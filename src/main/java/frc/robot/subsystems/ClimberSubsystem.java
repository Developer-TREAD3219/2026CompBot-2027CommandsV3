// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import org.wpilib.hardware.discrete.DigitalInput;
import org.wpilib.command3.Command;
//2026 version: import org.wpilib.command2.SubsystemBase;
import org.wpilib.command3.Mechanism;
import frc.robot.Constants.ClimberConstants;
//2026 version: import org.wpilib.smartdashboard.SmartDashboard;
import org.wpilib.telemetry.Telemetry;

//2026 version: public class ClimberSubsystem extends SubsystemBase {
public class ClimberSubsystem implements Mechanism {

  //2027 version: need to add value for canPort in the SparkMax constructor
  private final SparkMax climberMotor = new SparkMax(null,ClimberConstants.kClimberMotorCanId, MotorType.kBrushless);
  private final RelativeEncoder climberEncoder = climberMotor.getEncoder();
  // Limit switch on the retract (bottom) side of the climber. When this
  // DigitalInput returns true (or false depending on wiring) the climber
  // is fully retracted and we should not drive further inward.
  private final DigitalInput m_retractLimit = new DigitalInput(ClimberConstants.kClimberRetractLimitChannel);
  
  /*2026 version
  private enum ClimberStates {
    EXTENDING,
    RETRACTING,
    STOPPED
  }
  private ClimberStates state = ClimberStates.STOPPED;
  */

  //not used
  // private double retractionSpeed = ClimberConstants.kClimberReverseMotorSpeed;
  // private double extensionSpeed = ClimberConstants.kClimberMotorSpeed;

  private double climberMaxHeight = ClimberConstants.kClimberEncoderHighPoint;
  

  /** Creates a new ClimberSubsystem. */
  public ClimberSubsystem() {
  }

  //2026 version 
  // TO DO: add code to limit the extension and retraction of the climber, either
  // through encoder values or limit switches, to prevent damage to the mechanism.
  // For now, we will just have the motors run at the given speed when extending
  // or retracting, and stop when the stopClimber function is called.
 /*  private void extendClimber() {
    // Code to extend the climber mechanism
    // Allow extension regardless of the retract limit switch state
    climberMotor.set(-extensionSpeed);
  }
  
  private void retractClimber() {
    climberMotor.set(retractionSpeed);
  }
  
  public void startRetracting() {
    if (!isRetracted())
      state = ClimberStates.RETRACTING;
  }
  
  public void startExtending() {
    if (climberEncoder.getPosition() > climberMaxHeight){
        state = ClimberStates.EXTENDING;        
      }
  }
*/
  public void stopClimber() {
    // Code to stop the climber mechanism
    //2026 version: climberMotor.set(0);
    climberMotor.setVoltage(0);
    //2026 version: state = ClimberStates.STOPPED;
  }

  /*
   * Returns true when the retract limit switch is triggered. Depending on
   * wiring the DigitalInput may return true when pressed or when released.
   * If your switch is wired NC/NO differently, invert this logic here.
   */
  public boolean isRetracted() {
    // Return true when the physical retract switch is pressed. The wiring
    // so compare against the team-configurable constant in Constants.
    return !m_retractLimit.get();
  }

  // start 2027 code
  public boolean isExtended(){
    return climberEncoder.getPosition().get() >= climberMaxHeight;
  }
  /*
  If the climber is retracting and has fully retracted, 
    stop the climber and set the encoder position to 0, 
    else continue retracting.
  If the climber is extending and has reached the maximum height, 
    stop the climber, 
    else continue extending.
   */

public void updateInputs() {
  // 2027 version: update the input values for the climber subsystem here
    
// reset encoder
    if (isRetracted()) {
      climberEncoder.setPosition(0);
    }
    Telemetry.log("Climber Limit switch", m_retractLimit.get());
    Telemetry.log("[CLIMBER ENCODER]", climberEncoder.getPosition());
}
// TO DO: replace voltage with constant from Constants class
public Command retractClimber() {
    return run(coroutine -> {
      climberMotor.setVoltage(-12); // retract the climber
      try {
      while (true) {
          coroutine.yield(); // Let scheduler run other commands
      }
      } finally {
          stopClimber(); // Stop climber motor when command ends
      }
    }).named("retractClimber");
  }

  public Command retractClimberFully() {
  return run(coroutine -> {
    climberMotor.setVoltage(-12); // retract the climber
    coroutine.waitUntil(this::isRetracted);
    climberMotor.setVoltage(0);
  }).named("retractClimber");
  }
// TO DO: replace voltage with constant from Constants class
public Command extendClimber() {
    return run(coroutine -> {
      climberMotor.setVoltage(12); // extend the climber
      try {
      while (true) {
          coroutine.yield(); // Let scheduler run other commands
      }
      } finally {
          stopClimber(); // Stop climber motor when command ends
      }
    }).named("extendClimber");
  }

public Command extendClimberFully() {
  return run(coroutine -> {
    climberMotor.setVoltage(12); // extend the climber
    coroutine.waitUntil(this::isExtended);
    climberMotor.setVoltage(0);
  }).named("extendClimber");
}
// end 2027 code
/*2026 version:
  @Override
  public void periodic() {
    //Retraction logic    
    if (state == ClimberStates.RETRACTING) {
      // stop if we've hit the limit
      if(isRetracted()) {
        stopClimber();
        //if limit isn't hit, retract
      } else {
        retractClimber();
      }
    }
    //Extension logic
    if (state == ClimberStates.EXTENDING){
      if (climberEncoder.getPosition() <= climberMaxHeight) {
        stopClimber();
      } else {
        extendClimber();
      }
    }
    // reset encoder
    if (isRetracted()) {
      climberEncoder.setPosition(0);
    }
 
    SmartDashboard.putBoolean("Climber Limit switch", m_retractLimit.get());
    SmartDashboard.putNumber("[CLIMBER ENCODER]", climberEncoder.getPosition());
    SmartDashboard.putString("[CLIMBER STATE]", state.name());
    */

  }
