package de.fh_kiel.cimtt.robotik;

import static com.kuka.roboticsAPI.motionModel.BasicMotions.linRel;

import com.kuka.roboticsAPI.controllerModel.Controller;
import com.kuka.roboticsAPI.deviceModel.LBR;
import com.kuka.roboticsAPI.geometricModel.ObjectFrame;
import com.kuka.roboticsAPI.geometricModel.Workpiece;

public class Piece extends Items{
	private Workpiece piece;
	
	public Piece(Controller controller, LBR robot, Workpiece piece) {
		super(controller, robot, piece);
		this.piece = piece;
	}
	
	public boolean moveRel(double x, double y, double z, double a, double b, double c){
		this.piece.getDefaultMotionFrame().move(linRel(x, y, z, Math.toRadians(a), Math.toRadians(b), Math.toRadians(c)));
		return false;
	}
	
	public void atach(ObjectFrame object){
		this.piece.attachTo(object);
	}
	
	public void detach(){
		this.piece.detach();
	}
}
