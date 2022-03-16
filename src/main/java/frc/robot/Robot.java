// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";

  private static final int kMotor1 = 0;
  private static final int kMotor2 = 1;
  private static final int kMotor3 = 2;
  private static final int kMotor4 = 3;

  private static final int joystick_trigger_id = 1;


  private String m_autoSelected;


  MotorController motor1 = new PWMVictorSPX(kMotor1);
  MotorController motor2 = new PWMVictorSPX(kMotor2);
  MotorController motor3 = new PWMVictorSPX(kMotor3);
  MotorController motor4 = new PWMVictorSPX(kMotor4);
  DifferentialDrive arcade = new DifferentialDrive(motor1,motor2);
  XboxController controls = new XboxController(0);
  Joystick joystick = new Joystick(0);



  int throwspeed =  1;
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
    motor2.setInverted(true);

    
    
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {}

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
      double start_time = Timer.getFPGATimestamp();
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {

  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {


  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    // axis[0] right positive
    // axis[1] down positive
    // axis[2] twist right positive
    // axis[3] down positive (slider)
    // trigger button[1]
    double x = joystick.getX();
    double y = joystick.getY();


    arcade.arcadeDrive(-y, x);
    if (joystick.getRawButtonPressed(joystick_trigger_id)){
      motor3.set(throwspeed);
      motor4.set(throwspeed);
      
    }
    else if(joystick.getRawButtonReleased(joystick_trigger_id)){
      motor3.stopMotor();
      motor4.stopMotor();
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
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}
