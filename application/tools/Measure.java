package application.tools;

import static application.core.Gripper.closeGripper;
import static application.core.Gripper.openGripper;
import static application.core.Devices.getLbr;
import static application.core.Devices.getUI;
import static application.core.Devices.getTool;
import static application.tools.Points.getOrigin;

import java.text.DecimalFormat;

import static application.core.Logging.log;

import application.core.Movement;

import com.kuka.roboticsAPI.geometricModel.CartDOF;
import com.kuka.roboticsAPI.geometricModel.Frame;
import com.kuka.roboticsAPI.motionModel.IMotionContainer;
import com.kuka.roboticsAPI.motionModel.PositionHold;
import com.kuka.roboticsAPI.motionModel.controlModeModel.CartesianImpedanceControlMode;
import com.kuka.roboticsAPI.uiModel.ApplicationDialogType;

public class Measure {
	
	public static Frame mReference, mMeasure;
	
	private static String getDifference( Frame aA, Frame aB){
		return "[" + new DecimalFormat("##.#").format((aA.getX()-aB.getX())) + " " + new DecimalFormat("##.#").format((aA.getY()-aB.getY())) + " " + new DecimalFormat("##.#").format((aA.getZ()-aB.getZ())) + "] [" 
				+ new DecimalFormat("##.#").format(Math.toDegrees((aA.getAlphaRad()-aB.getAlphaRad()))) + " " + new DecimalFormat("##.#").format(Math.toDegrees((aA.getBetaRad()-aB.getBetaRad()))) + " " + new DecimalFormat("##.#").format(Math.toDegrees((aA.getGammaRad()-aB.getGammaRad()))) + "]";
	}
	
	public static void measure(){
		if(getOrigin() == null){
			log("Measure", "Please calibrate");
			getUI().displayModalDialog(ApplicationDialogType.INFORMATION, "Please Calibrate", "OK");
			return;
		}
		
		mReference = getOrigin();
		mMeasure = getOrigin();
		
		Movement.freeMovementStart();
		
		int direction = 0;
		do{
			direction = getUI().displayModalDialog(
				ApplicationDialogType.QUESTION,
				"Messen\n Origin->Messpunkt" + getDifference(getOrigin(), mMeasure) + "\n Reference->Messpunkt" + getDifference(mReference, mMeasure),
				 "Zurück", "Gripper auf", "Gripper zu", "Referenz setzen", "Messen");
		
			switch (direction) {
				case 0:
					break;
				case 1:
					openGripper();
					break;
				case 2:
					closeGripper();
					break;
				case 3:
					mReference = getLbr().getCurrentCartesianPosition(getLbr().getFlange());
					break;
				case 4:
					mMeasure = getLbr().getCurrentCartesianPosition(getLbr().getFlange());
					break;
			}
		} while(direction != 0);

		Movement.freeMovementStop();
	}

}
