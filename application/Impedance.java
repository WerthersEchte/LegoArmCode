package application;


import com.kuka.roboticsAPI.applicationModel.RoboticsAPIApplication;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.*;

import com.kuka.roboticsAPI.controllerModel.sunrise.SunriseController;
import com.kuka.roboticsAPI.deviceModel.LBR;
import com.kuka.roboticsAPI.geometricModel.CartDOF;
import com.kuka.roboticsAPI.geometricModel.Tool;
import com.kuka.roboticsAPI.motionModel.IMotionContainer;
import com.kuka.roboticsAPI.motionModel.PTP;
import com.kuka.roboticsAPI.motionModel.PositionHold;
import com.kuka.roboticsAPI.motionModel.controlModeModel.CartesianImpedanceControlMode;
import com.kuka.roboticsAPI.uiModel.ApplicationDialogType;


public class Impedance extends RoboticsAPIApplication {

	final static double offsetAxis2And4=Math.toRadians(10);

	private static final int stiffnessZ = 50;
	private static final int stiffnessY = 50;
	private static final int stiffnessX = 50;
	private Tool tool;
	private LBR lbr;
	private SunriseController controller;
	private static double[] startPosition=new double[]{0,offsetAxis2And4,0,offsetAxis2And4-Math.toRadians(90),0,Math.toRadians(90),0};
	
	
	private final static String informationText=
			"This application is intended for floor mounted robots!"+ "\n" +
			"\n" +
			"The robot moves to the start position and holds the position in impedance control mode until you stops the motion " +
			"by confirming the modal dialog on the smartPAD." + "\n" + 
			"The stiffness is set to " +
			"X="+stiffnessX+" Y="+stiffnessY +" Z="+stiffnessZ+" in N/m.";


	public void initialize() {
		//Lichter = new Light(controller);
		lbr = getContext().getDeviceFromType(LBR.class);
		lbr.setESMState("1");
		tool = getApplicationData().createFromTemplate("EGripper");
		tool.attachTo(lbr.getFlange());
		controller = (SunriseController) getController("KUKA_Sunrise_Cabinet_1");
		
	}

	public void run() {
		getLogger().info("Show modal dialog and wait for user to confirm");
        int isCancel = getApplicationUI().displayModalDialog(ApplicationDialogType.QUESTION, informationText, "Cartesian", "Rotation", "Cancel");
        if (isCancel == 2)
        {
            return;
        }

		getLogger().info("Move to start position");
		PTP ptpToStartPosition = ptp(startPosition);
		ptpToStartPosition.setJointVelocityRel(0.15);
		tool.move(ptp(getApplicationData().getFrame("/StartImpedanz")).setJointVelocityRel(0.2));

		getLogger().info("Hold position in impedance control mode");
		CartesianImpedanceControlMode impedanceControlMode = 	new CartesianImpedanceControlMode();
		if(isCancel == 0){
			impedanceControlMode.parametrize(CartDOF.X).setStiffness(stiffnessX);
			impedanceControlMode.parametrize(CartDOF.Y).setStiffness(stiffnessY);
			impedanceControlMode.parametrize(CartDOF.Z).setStiffness(stiffnessZ);
		}else{
			impedanceControlMode.parametrize(CartDOF.A).setStiffness(stiffnessX * 0.1);
			impedanceControlMode.parametrize(CartDOF.B).setStiffness(stiffnessY * 0.1);
			impedanceControlMode.parametrize(CartDOF.C).setStiffness(stiffnessZ * 0.1);
		}

		// The robot is set to position hold and impedance control mode gets activated without a timeout. 
		IMotionContainer positionHoldContainer = tool.moveAsync((new PositionHold(impedanceControlMode, -1, null)));

		getLogger().info("Show modal dialog while executing position hold");
		//Lichter.turnOff(signale.RED);
		
		getApplicationUI().displayModalDialog(ApplicationDialogType.INFORMATION, "Press ok to finish the application.", "OK");

		// As soon as the modal dialog returns, the motion container will be cancelled. This finishes the position hold. 
		positionHoldContainer.cancel();
		//Lichter.turnOffAll();
	}

}
