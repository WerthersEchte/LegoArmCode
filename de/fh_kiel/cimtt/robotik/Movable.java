package de.fh_kiel.cimtt.robotik;

import com.kuka.roboticsAPI.geometricModel.AbstractFrame;
import com.kuka.roboticsAPI.geometricModel.ObjectFrame;

public interface Movable {
	
	public boolean movePTP(AbstractFrame destination);
	public boolean movePTP(double a1, double a2, double a3 ,double a4, double a5, double a6, double a7);
	public boolean moveLin(AbstractFrame destination);
	public boolean moveCirc(AbstractFrame Center, double angel);
	public boolean moveCirc(AbstractFrame Center, double angel, double MaxTorque);
	public boolean moveX(double distance);
	public boolean moveY(double distance);
	public boolean moveZ(double distance);
	public boolean moveA(double distance);
	public boolean moveB(double distance);
	public boolean moveC(double distance);
	public boolean moveNear(AbstractFrame destination);
	public boolean moveNear(AbstractFrame destination, double distance);
	public boolean moveRel(double x, double y, double z, double a, double b, double c);
	public boolean moveJointPosition(int Joint, double Angel);
	public void attach(ObjectFrame object);
	public void detach();
	public Double getDistance(AbstractFrame frame1, AbstractFrame frame2);
	public void moveSaveLin(AbstractFrame destination);
}
