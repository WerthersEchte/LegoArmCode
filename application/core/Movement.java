package application.core;

import static application.core.Devices.getLbr;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.lin;
import static application.core.Logging.log;

import com.kuka.roboticsAPI.geometricModel.AbstractFrame;
import com.kuka.roboticsAPI.geometricModel.CartDOF;
import com.kuka.roboticsAPI.motionModel.IMotionContainer;
import com.kuka.roboticsAPI.motionModel.PositionHold;
import com.kuka.roboticsAPI.motionModel.controlModeModel.CartesianImpedanceControlMode;

public class Movement {

	public static void linearMoveToPoint( AbstractFrame aFrame, double aSpeed){
		log("LBR-linear move to " + aFrame.toString());
		getLbr().move(
				lin(aFrame)
					.setCartVelocity(aSpeed)
				);
	}
	
	private static IMotionContainer positionHoldContainer = null;
	public static void freeMovementStart(){
		CartesianImpedanceControlMode cartImpCtrlMode = new CartesianImpedanceControlMode();
		cartImpCtrlMode.parametrize(CartDOF.TRANSL, CartDOF.A).setStiffness(1.0);
		cartImpCtrlMode.parametrize(CartDOF.B, CartDOF.C).setStiffness(300.0);
		cartImpCtrlMode.parametrize(CartDOF.ALL).setDamping(0.7);
		cartImpCtrlMode.parametrize(CartDOF.A).setDamping(0.1);
		
		positionHoldContainer = getLbr().moveAsync((new PositionHold(cartImpCtrlMode, -1, null)));
	}
	public static void freeMovementStop(){
		positionHoldContainer.cancel();
	}

}
