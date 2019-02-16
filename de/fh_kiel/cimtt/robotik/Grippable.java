package de.fh_kiel.cimtt.robotik;

import com.kuka.roboticsAPI.geometricModel.AbstractFrame;
import com.kuka.roboticsAPI.geometricModel.Frame;
import com.kuka.roboticsAPI.geometricModel.math.CoordinateAxis;

public interface Grippable {
	public boolean open();
	public boolean close();
	public boolean getPart(AbstractFrame destination);
	public boolean putPart(AbstractFrame destination);
	public boolean putPartSpline(AbstractFrame destination);
	public boolean getPartSpline(AbstractFrame destination);
	public boolean transferSpline(AbstractFrame destination);
	public Frame findX(double distance);
	public Frame findY(double distance);
	public Frame findZ(double distance);
	public boolean findAndGetXY(double distace, CoordinateAxis direction);
}
