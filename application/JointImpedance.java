package application;

import com.kuka.roboticsAPI.applicationModel.RoboticsAPIApplication;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.*;

import com.kuka.roboticsAPI.deviceModel.LBR;
import com.kuka.roboticsAPI.geometricModel.CartDOF;
import com.kuka.roboticsAPI.motionModel.IMotionContainer;
import com.kuka.roboticsAPI.motionModel.PTP;
import com.kuka.roboticsAPI.motionModel.PositionHold;
import com.kuka.roboticsAPI.motionModel.controlModeModel.CartesianImpedanceControlMode;
import com.kuka.roboticsAPI.motionModel.controlModeModel.JointImpedanceControlMode;
import com.kuka.roboticsAPI.uiModel.ApplicationDialogType;

public class JointImpedance extends RoboticsAPIApplication {

	final static double offsetAxis2And4=Math.toRadians(10);

	private static final int stiffnessA1 = 10;
	private static final int stiffnessA2 = 10;
	private static final int stiffnessA3 = 10;
	private static final int stiffnessA4 = 10;
	private static final int stiffnessA5 = 10;
	private static final int stiffnessA6 = 10;
	private static final int stiffnessA7 = 10;
	private LBR lbr;
	private static double[] startPosition=new double[]{0,offsetAxis2And4,0,offsetAxis2And4-Math.toRadians(90),0,Math.toRadians(90),0};
	private final static String informationText=
			"This application is intended for floor mounted robots!"+ "\n" +
			"\n" +
			"The robot moves to the start position and holds the position in impedance control mode until you stops the motion " +
			"by confirming the modal dialog on the smartPAD." + "\n" + 
			"The stiffness is set to " +
			"A1="+stiffnessA1+" A2="+stiffnessA2 +" A3="+stiffnessA3+" A4="+stiffnessA4+" A5="+stiffnessA5+" A6="+stiffnessA6+" A7="+stiffnessA7+" in Nm/rad.";


	public void initialize() {
		lbr = getContext().getDeviceFromType(LBR.class);
	}

	public void run() {
		getLogger().info("Show modal dialog and wait for user to confirm");
        int isCancel = getApplicationUI().displayModalDialog(ApplicationDialogType.QUESTION, informationText, "OK", "Cancel");
        if (isCancel == 1)
        {
            return;
        }

		getLogger().info("Move to start position");
		PTP ptpToStartPosition = ptp(startPosition);
		ptpToStartPosition.setJointVelocityRel(0.25);
		lbr.move(ptpToStartPosition);

		getLogger().info("Hold position in impedance control mode");
		JointImpedanceControlMode impedanceControlMode = 	new JointImpedanceControlMode(
				stiffnessA1, stiffnessA2, stiffnessA3, stiffnessA4, stiffnessA5, stiffnessA6, stiffnessA7);

		// The robot is set to position hold and impedance control mode gets activated without a timeout. 
		IMotionContainer positionHoldContainer = lbr.moveAsync((new PositionHold(impedanceControlMode, -1, null)));

		getLogger().info("Show modal dialog while executing position hold");
		getApplicationUI().displayModalDialog(ApplicationDialogType.INFORMATION, "Press ok to finish the application.", "OK");

		// As soon as the modal dialog returns, the motion container will be cancelled. This finishes the position hold. 
		positionHoldContainer.cancel();
	}

}
