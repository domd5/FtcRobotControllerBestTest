package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.util.ElapsedTime;
@TeleOp
@Disabled
public class decTeleop extends LinearOpMode {
    /*
     * This file contains an example of a Linear "OpMode".
     * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
     * The names of OpModes appear on the menu of the FTC Driver Station.
     * When a selection is made from the menu, the corresponding OpMode is executed.
     *
     * This particular OpMode illustrates driving a 4-motor Omni-Directional (or Holonomic) robot.
     * This code will work with either a Mecanum-Drive or an X-Drive train.
     * Both of these drives are illustrated at https://gm0.org/en/latest/docs/robot-design/drivetrains/holonomic.html
     * Note that a Mecanum drive must display an X roller-pattern when viewed from above.
     *
     * Also note that it is critical to set the correct rotation direction for each motor.  See details below.
     *
     * Holonomic drives provide the ability for the robot to move in three axes (directions) simultaneously.
     * Each motion axis is controlled by one Joystick axis.
     *
     * 1) Axial:    Driving forward and backward               Left-joystick Forward/Backward
     * 2) Lateral:  Strafing right and left                     Left-joystick Right and Left
     * 3) Yaw:      Rotating Clockwise and counter clockwise    Right-joystick Right and Left
     *
     * This code is written assuming that the right-side motors need to be reversed for the robot to drive forward.
     * When you first test your robot, if it moves backward when you push the left stick forward, then you must flip
     * the direction of all 4 motors (see code below).
     *
     * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
     * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
     */




    // Declare OpMode members for each of the 4 motors.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftFrontDrive = null;
    private DcMotor leftBackDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor rightBackDrive = null;
    private DcMotorEx slideLeft = null;
    private DcMotorEx slideRight = null;
    private ServoImplEx claw = null;
    private ServoImplEx rotate = null;
    private ServoImplEx extend = null;
    private DcMotorEx arm = null;


    private double armPower = 0;




    @Override
    public void runOpMode() {


        // Initialize the hardware variables. Note that the strings used here must correspond
        // to the names assigned during the robot configuration step on the DS or RC devices.
        leftFrontDrive = hardwareMap.get(DcMotor.class, "frontLeft");
        leftBackDrive = hardwareMap.get(DcMotorEx.class, "backLeft");
        rightFrontDrive = hardwareMap.get(DcMotorEx.class, "frontRight");
        rightBackDrive = hardwareMap.get(DcMotorEx.class, "backRight");
        slideLeft = hardwareMap.get(DcMotorEx.class, "slideLeft");
        slideRight = hardwareMap.get(DcMotorEx.class, "slideRight");
        rotate = hardwareMap.get(ServoImplEx.class, "rotate");
        claw = hardwareMap.get(ServoImplEx.class, "claw");
        extend = hardwareMap.get(ServoImplEx.class, "extend");
        arm = hardwareMap.get(DcMotorEx.class, "arm");


        // ########################################################################################
        // !!!            IMPORTANT Drive Information. Test your motor directions.            !!!!!
        // ########################################################################################
        // Most robots need the motors on one side to be reversed to drive forward.
        // The motor reversals shown here are for a "direct drive" robot (the wheels turn the same direction as the motor shaft)
        // If your robot has additional gear reductions or uses a right-angled drive, it's important to ensure
        // that your motors are turning in the correct direction.  So, start out with the reversals here, BUT
        // when you first test your robot, push the left joystick forward and observe the direction the wheels turn.
        // Reverse the direction (flip FORWARD <-> REVERSE ) of any wheel that runs backward
        // Keep testing until ALL the wheels move the robot forward when you push the left joystick forward.
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);
        slideLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slideRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slideLeft.setDirection(DcMotor.Direction.REVERSE);
        slideRight.setDirection(DcMotor.Direction.REVERSE);
        rotate.setDirection(Servo.Direction.REVERSE);
        extend.setDirection(Servo.Direction.REVERSE);
        claw.setDirection(Servo.Direction.REVERSE);
        arm.setDirection(DcMotor.Direction.REVERSE);


        rotate.setPosition(0.2);
        extend.setPosition(-0.6);
        claw.setPosition(-0.3);




        //slideLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //slideRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        // Wait for the game to start (driver presses START)
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();
        runtime.reset();


        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            telemetry.addData("pos slide right", slideRight.getCurrentPosition());
            telemetry.addData("pos slide left", slideLeft.getCurrentPosition());
            telemetry.addData("slide right power", slideRight.getPower());
            telemetry.addData("slide left power", slideLeft.getPower());
            telemetry.addData("slide right velocity", slideRight.getVelocity());
            telemetry.addData("slide left velocity", slideLeft.getVelocity());
            telemetry.addData("claw pos", claw.getPosition());
            //slideLeft.setPower(slideRight.getPower());
            if (gamepad2.right_bumper) {
               /*armPower = 1;
               slideRight.setTargetPosition(800);
               slideRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
               slideRight.setVelocity(400);
               //slideLeft.setVelocity(slideRight.getVelocity());
               //slideLeft.setPower(slideRight.getPower());
               slideLeft.setTargetPosition(800);
               slideLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
               slideLeft.setVelocity(400);*/


                slideRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                if (slideRight.getCurrentPosition() > -1000) {
                    slideRight.setPower(0.6);
                    slideLeft.setPower(0.6);
                    //slideLeft.setPower(slideRight.getPower());
                } else {
                    slideRight.setPower(0);
                    slideLeft.setPower(0);
                }
            } else if (gamepad2.right_trigger > 0.4) {
               /*slideLeft.setPower(-0.4);
               slideRight.setPower(-0.4);
               slideRight.setTargetPosition(-3);
               slideRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
               slideRight.setVelocity(-400);
               slideLeft.setTargetPosition(3);
               slideLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
               slideLeft.setVelocity(400);*/
                //armPower = -1;
            } else {
                slideLeft.setVelocity(0);
                slideRight.setVelocity(0);
                //armPower = 0;
            }
           /*if(slideRight.getCurrentPosition() < -2000){
               armPower = Math.min(armPower, 0);
           } else if (slideRight.getCurrentPosition() > -50) {
               armPower = Math.max(armPower, 0);
           }*/






            if (gamepad2.dpad_up) {
                slideRight.setTargetPosition(-500);
                slideRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                slideRight.setVelocity(400);
                //slideLeft.setVelocity(slideRight.getVelocity());
                //slideLeft.setPower(slideRight.getPower());
                slideLeft.setTargetPosition(500);
                slideLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                slideLeft.setVelocity(400);
            } else {
               /*slideRight.setPower(armPower);
               slideLeft.setPower(armPower);*/
            }








            if (gamepad2.left_stick_x > 0.2) {
                arm.setPower(1);
            } else if (gamepad2.left_stick_x < -0.2) {
                arm.setPower(-1);
            } else {
                arm.setPower(0);
            }
            if (gamepad2.left_bumper) {
                extend.setPosition(0.6);
            } else if (gamepad2.left_trigger > 0.1) {
                extend.setPosition(-0.7);
            } else {
                extend.setPosition(-0.7);
            }


            if (gamepad2.cross) {
                claw.setPosition(-0.3);
            } else {
                claw.setPosition(0.5);
            }


            if (gamepad2.circle) {
                rotate.setPosition(0.5);
            } else if (gamepad2.square) {
                rotate.setPosition(2);
            }




            double max;


            // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
            double axial; // Note: pushing stick forward gives negative value
            double lateral;
            double yaw;


            if(gamepad1.right_trigger >= 0.05) {
                axial = -gamepad1.left_stick_y * 0.5;
                lateral = gamepad1.left_stick_x * 0.5;
                yaw = gamepad1.right_stick_x * 0.5;
            }
            else {
                axial= -gamepad1.left_stick_y;
                lateral = gamepad1.left_stick_x;
                yaw = gamepad1.right_stick_x;
            }
            // Combine the joystick requests for each axis-motion to determine each wheel's power.
            // Set up a variable for each drive wheel to save the power level for telemetry.
            double leftFrontPower = axial + lateral + yaw;
            double leftBackPower = axial - lateral + yaw;
            double rightFrontPower = axial - lateral - yaw;
            double rightBackPower = axial + lateral - yaw;


            // Normalize the values so no wheel power exceeds 100%
            // This ensures that the robot maintains the desired motion.
            max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
            max = Math.max(max, Math.abs(leftBackPower));
            max = Math.max(max, Math.abs(rightBackPower));


            if (max > 1.0) {
                leftFrontPower /= max;
                rightFrontPower /= max;
                leftBackPower /= max;
                rightBackPower /= max;
            }


            // This is test code:
            //
            // Uncomment the following code to test your motor directions.
            // Each button should make the corresponding motor run FORWARD.
            //   1) First get all the motors to take to correct positions on the robot
            //      by adjusting your Robot Configuration if necessary.
            //   2) Then make sure they run in the correct direction by modifying the
            //      the setDirection() calls above.
            // Once the correct motors move in the correct direction re-comment this code.


           /*
           leftFrontPower  = gamepad1.x ? 1.0 : 0.0;  // X gamepad
           leftBackPower   = gamepad1.a ? 1.0 : 0.0;  // A gamepad
           rightFrontPower = gamepad1.y ? 1.0 : 0.0;  // Y gamepad
           rightBackPower  = gamepad1.b ? 1.0 : 0.0;  // B gamepad
           */


            // Send calculated power to wheels
            leftFrontDrive.setPower(leftFrontPower);
            rightFrontDrive.setPower(rightFrontPower);
            leftBackDrive.setPower(leftBackPower);
            rightBackDrive.setPower(rightBackPower);


            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Front left/Right", "%4.2f, %4.2f", leftFrontPower, rightFrontPower);
            telemetry.addData("Back  left/Right", "%4.2f, %4.2f", leftBackPower, rightBackPower);
            telemetry.update();
        }
    }




}

