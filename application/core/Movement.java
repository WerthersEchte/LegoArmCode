package application.core;

import static application.core.Devices.getLbr;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.lin;
import static application.core.Logging.log;

import com.kuka.roboticsAPI.conditionModel.ForceCondition;
import com.kuka.roboticsAPI.conditionModel.ICondition;
import com.kuka.roboticsAPI.geometricModel.AbstractFrame;
import com.kuka.roboticsAPI.geometricModel.CartDOF;
import com.kuka.roboticsAPI.geometricModel.Frame;
import com.kuka.roboticsAPI.motionModel.IMotionContainer;
import com.kuka.roboticsAPI.motionModel.PositionHold;
import com.kuka.roboticsAPI.motionModel.controlModeModel.CartesianImpedanceControlMode;
import com.kuka.roboticsAPI.motionModel.controlModeModel.IMotionControlMode;

public class Movement {
	
	ForceCondition a;

	private static IMotionContainer currentMove = null;
	public static void linearMoveToPoint( AbstractFrame aFrame, double aSpeed ){
		stopCurentMove();
		log("LBR-linear move to " + aFrame.toString());
		currentMove = getLbr().move(
				lin(aFrame)
					.setCartVelocity(aSpeed)
				);
	}
	
	public static void linearMoveToPoint( AbstractFrame aFrame, double aSpeed, IMotionControlMode aMode) {
		stopCurentMove();
		log("LBR-linear move to " + aFrame.toString());
		currentMove = getLbr().move(
				lin(aFrame)
					.setCartVelocity(aSpeed)
					.setMode(aMode)
				);
	}

	public static void stopCurentMove(){
		if(currentMove != null){
			currentMove.cancel();
			currentMove = null;
		}
	}
	
	
	private static IMotionContainer positionHoldContainer = null;
	public static void freeMovementStart(){
		freeMovementStop();
		CartesianImpedanceControlMode cartImpCtrlMode = new CartesianImpedanceControlMode();
		cartImpCtrlMode.parametrize(CartDOF.TRANSL, CartDOF.A).setStiffness(1.0);
		cartImpCtrlMode.parametrize(CartDOF.B, CartDOF.C).setStiffness(300.0);
		cartImpCtrlMode.parametrize(CartDOF.ALL).setDamping(0.7);
		cartImpCtrlMode.parametrize(CartDOF.A).setDamping(0.1);
		
		positionHoldContainer = getLbr().moveAsync((new PositionHold(cartImpCtrlMode, -1, null)));
	}
	public static void freeMovementStop(){
		if(positionHoldContainer != null){
			positionHoldContainer.cancel();
			positionHoldContainer = null;
		}
	}

}
