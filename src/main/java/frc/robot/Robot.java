/ Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;

import javax.management.InstanceAlreadyExistsException;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import org.ejml.interfaces.linsol.ReducedRowEchelonForm;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import pabeles.concurrency.ConcurrencyOps.Reset;
import edu.wpi.first.wpilibj.AnalogInput;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";

  private static final int motorId1 = 0;
  private static final int motorId2 = 1;
  private static final int motorThrow_id1 = 2;
  private static final int motorThrow_id2 = 3;
  private static final int motorScoopId = 6;
  private static final int joystickLeftBottomId = 16;
  private static final int joystickMiddleBottomId = 15;
  private static final int analogInputId = 0;

  final AnalogInput distance = new AnalogInput(analogInputId);
  final double motorThrow1DifferentialDefault = 1;
  final double motorThrow2DifferentialDefault = 1;
  final double increment = 0.1;
  final double throttleMultiplier = 0.5;
 
  private static final int joystickTriggerId = 1;


  private String m_autoSelected;

  MotorType type = MotorType.kBrushed;
  MotorController motor_drive1 = new PWMSparkMax(motorId1);
  MotorController motor_drive2 = new PWMSparkMax(motorId2);
  MotorController  motor_throw1 = new PWMVictorSPX(motorThrow_id1);
  MotorController motor_throw2 = new PWMVictorSPX(motorThrow_id2);
  MotorController motor_scoop = new PWMVictorSPX(motorScoopId);
  ControlMode percentage_output_control = ControlMode.PercentOutput;
  DifferentialDrive arcade = new DifferentialDrive(motor_drive1,motor_drive2);

  Joystick joystick = new Joystick(0);
  Joystick throttle = new Joystick(1);

  double time_since_auto_start;
  double throw_speed =  1;
  double engine_differential = 1;
  double test_time;

  boolean scoop_and_throw_master = false;
  boolean move_master = false;

  double motor_throw1_differential = motorThrow1DifferentialDefault;
  double motor_throw2_differential = motorThrow2DifferentialDefault;

  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    motor_drive2.setInverted(true); // change to motor 1 if the robot goes reverse
    motor_throw2.setInverted(true);
    CameraServer.startAutomaticCapture();
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    if(joystick.getRawButtonPressed(joystickMiddleBottomId) == true)
    {
      if(move_master == false)
      {
        move_master = true;
      }
      else
      {
        move_master = false;
      }
  
    }

    if(joystick.getRawButtonPressed(joystickLeftBottomId) == true)
    {
      if(scoop_and_throw_master == false)
      {
        scoop_and_throw_master = true;
      }
      else
      {
        scoop_and_throw_master = false;
      }
    }}

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);

    time_since_auto_start = Timer.getFPGATimestamp();
  }



  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    double current_time = Timer.getFPGATimestamp() - time_since_auto_start;
    System.out.println(current_time);
    
    if(current_time < 1)
    {
      motor_throw1.set(0.77); //max
      motor_throw2.set(0.77);
    }
    else
    {
      motor_throw1.set(0);
      motor_throw2.set(0);
    }
    
    if(current_time >= 2 && current_time < 5)
    {
      arcade.arcadeDrive(0.7, 0);
    }
    else
    {
      arcade.arcadeDrive(0, 0);
    }
    

    
    /*
    double time_since_auto_start = Timer.getFPGATimestamp() - time_timer;
    double auto_drive_speed = 1;
    double auto_drive_time = 6;
    double auto_drive_throw_time = 3;

    //Auto-Drive
    if(time_since_auto_start < auto_drive_throw_time ){
      //setthrowmotors
    }
    else
    {
      //setthrowmotors
    }

    //Auto-Throw 3 modes maybe
    if(auto_drive_throw_time < time_since_auto_start && time_since_auto_start < auto_drive_time)
    {
      motor_drive1.set(-auto_drive_speed);
      motor_drive2.set(-auto_drive_speed);
    }
    else if(joystick.getRawButtonReleased(joystickTriggerId)){
      motor_drive1.stopMotor();
      motor_drive2.stopMotor();
    }    
    */
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
    double time_since_teleop_start = Timer.getFPGATimestamp();
    motor_throw1.set(0);
    motor_throw2.set(0);
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    
    // IMPORTANT, CHANGE IF NEEDED
    


    if(joystick.getRawButtonPressed(2))
    {
      motor_throw1_differential = motorThrow1DifferentialDefault;
      motor_throw2_differential = motorThrow2DifferentialDefault;
    }

    // decrease angle
    if(joystick.getRawButtonPressed(3))
    {
      
      if(motor_throw1_differential == motorThrow1DifferentialDefault && motor_throw2_differential == motorThrow2DifferentialDefault)
      {
        System.out.println("dec");
        motor_throw1_differential -= increment;
      }
      else if(motor_throw2_differential < motorThrow2DifferentialDefault)
      {
          motor_throw2_differential += increment;
      }
      else if(motor_throw1_differential < motorThrow1DifferentialDefault)
      {
        if(motor_throw1_differential > increment)
        {
          motor_throw1_differential -= increment;
        }
      }
    }

    // increase angle
    if(joystick.getRawButtonPressed(4))
    {
      if(motor_throw1_differential == motorThrow1DifferentialDefault && motor_throw2_differential == motorThrow2DifferentialDefault)
      {
        System.out.println("inc");
        motor_throw2_differential -= increment;
      }
      else if(motor_throw1_differential < motorThrow2DifferentialDefault)
      {
          motor_throw1_differential += increment;
      }
      else if(motor_throw2_differential < motorThrow1DifferentialDefault)
      {
        if(motor_throw2_differential > increment)
        {
          motor_throw2_differential -= increment;
        }
      }
    }

    // axis[0] right positive
    // axis[1] down positive
    // axis[2] twist right positive
    // axis[3] down positive (slider)
    // trigger button[1]
    //System.out.println("Move Master: " + String.valueOf(move_master));
    //System.out.println("Other Master: " + String.valueOf(scoop_and_throw_master));

    if(move_master == true)
    {
      double x = joystick.getX();
      double y = joystick.getY();
      double raw_slider = joystick.getRawAxis(3);

      // Turns axis into linear slider, 1 at top, 0 at bottom
      double sensitivity = convertMinMaxToLinear(raw_slider);

      //joystickin x ve y sine göre değişebilir emin değilim
      arcade.arcadeDrive(y * sensitivity , -x * sensitivity);
    }
    else
    {
      arcade.arcadeDrive(0, 0);
    }
    

    if(scoop_and_throw_master == true)
    { 
      // throwspeed value depending on current state
        double raw_throttle = throttle.getRawAxis(2);
        throw_speed = convertMinMaxToLinear(raw_throttle);
      

      //throw code
      if (joystick.getRawButtonPressed(joystickTriggerId)){
        // IMPORTANT, might have to change motor multipliers
        System.out.println("1diff " + String.valueOf(motor_throw1_differential));
        System.out.println("2diff " + String.valueOf(motor_throw2_differential));
        motor_throw1.set(throw_speed * motor_throw1_differential);
        // VERY IMPORTANT ENABLE
        motor_throw2.set(throw_speed * motor_throw2_differential);
      }
      else if(joystick.getRawButtonReleased(joystickTriggerId)){
        motor_throw1.set(0);
        motor_throw2.set(0);
      }

      // ball_motor code
      // Retrieves raw axis from 1 to -1
      double throttle_engine_value = throttle.getRawAxis(5) * throttleMultiplier;
      motor_scoop.set(throttle_engine_value);
    }
  }
  
  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {
    test_time = Timer.getFPGATimestamp();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {
    if(test_time - Timer.getFPGATimestamp() > 2)
    {
      double rawValue = distance.getValue();
      SmartDashboard.putNumber("Distance", rawValue);
      test_time = Timer.getFPGATimestamp();
    }
    
    
  }

  private void setMotor(MotorController motor, double speed) {
    motor.set(speed);
  }
  
  private void setThrowMotors(double speed){
    MotorController[] motors = {motor_throw1, motor_throw2};
    for(int i = 0; i < motors.length; i++)
    {
      motors[i].set(speed);
    }
  }

  private double convertMinMaxToLinear(double axis)
  {
    double converted_axis = 1 - ((axis + 1) / 2);
    return converted_axis;
  }

  
}
