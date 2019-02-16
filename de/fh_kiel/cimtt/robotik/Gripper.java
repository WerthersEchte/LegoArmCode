package de.fh_kiel.cimtt.robotik;

import static com.kuka.roboticsAPI.motionModel.BasicMotions.circ;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.lin;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.linRel;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.ptp;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.spl;

import java.util.concurrent.TimeUnit;

import javax.naming.LinkRef;

import com.kuka.common.ThreadUtil;
import com.kuka.roboticsAPI.conditionModel.CartesianTorqueCondition;
import com.kuka.roboticsAPI.conditionModel.ForceCondition;
import com.kuka.roboticsAPI.conditionModel.JointTorqueCondition;
import com.kuka.roboticsAPI.conditionModel.TorqueComponentCondition;
import com.kuka.roboticsAPI.controllerModel.Controller;
import com.kuka.roboticsAPI.deviceModel.JointEnum;
import com.kuka.roboticsAPI.deviceModel.JointPosition;
import com.kuka.roboticsAPI.deviceModel.LBR;
import com.kuka.roboticsAPI.executionModel.IFiredConditionInfo;
import com.kuka.roboticsAPI.geometricModel.AbstractFrame;
import com.kuka.roboticsAPI.geometricModel.CartDOF;
import com.kuka.roboticsAPI.geometricModel.Frame;
import com.kuka.roboticsAPI.geometricModel.ObjectFrame;
import com.kuka.roboticsAPI.geometricModel.Tool;
import com.kuka.roboticsAPI.geometricModel.math.CoordinateAxis;
import com.kuka.roboticsAPI.geometricModel.math.CoordinateDirection;
import com.kuka.roboticsAPI.geometricModel.math.ITransformation;
import com.kuka.roboticsAPI.geometricModel.math.Transformation;
import com.kuka.roboticsAPI.geometricModel.math.Vector;
import com.kuka.roboticsAPI.motionModel.CIRC;
import com.kuka.roboticsAPI.motionModel.IMotionContainer;
import com.kuka.roboticsAPI.motionModel.PositionHold;
import com.kuka.roboticsAPI.motionModel.Spline;
import com.kuka.roboticsAPI.motionModel.SplineOrientationType;
import com.kuka.roboticsAPI.motionModel.controlModeModel.CartesianImpedanceControlMode;
import com.kuka.roboticsAPI.motionModel.controlModeModel.CartesianSineImpedanceControlMode;
import com.kuka.roboticsAPI.requestModel.GetCurrentConfigurationRequest;
import com.kuka.roboticsAPI.requestModel.SetSimulationSafetySignalsRequest;
import com.kuka.roboticsAPI.sensorModel.ForceSensorData;
import com.sun.org.apache.xml.internal.serializer.ToUnknownStream;

public class Gripper extends Items implements  Movable,Grippable{

	private Controller controller;
	private LBR robot;
	private Tool tool;
	private static double maxCartVelocity = 300.0;
	private static double maxCartAcceleration = 500.0;
	private static double maxCartJerk = 5000.0;
	
	private static double maxJointVelocity = 0.15;
	private static double maxJointAcceleration = 0.2;
	private static double maxJointJerk = 0.02;
	
	private static double cartStiffness = 2000.0;
	private static double nullStiffness = 0.5;
	


	public Gripper(Controller controllerUse, LBR robotUse, Tool Tool) {
		super(controllerUse, robotUse, Tool);
		this.controller = controllerUse;
		this.robot = robotUse;
		this.tool = Tool;
	}

	@Override
	public boolean open() {
		// TODO Automatisch generierter Methodenstub
		
		return false;
	}

	@Override
	public boolean close() {
		// TODO Automatisch generierter Methodenstub
		return false;
	}

	/**
	 * Die Methode greift ein Teil, das sich an der
	 * angegebenen Position befindet.
	 * @param destination - Position des Teils
	 * @return true - wenn das Teil gegriffen wurde
	 */
	@Override
	public boolean getPart(AbstractFrame destination) {
		return getPart(destination, 50);
	}
		
	public boolean getPart(AbstractFrame destination, double distance){
		// TODO: Ein Teil an der gegebenen Position abholen
		
		open();
		moveNear(destination, distance);
		moveLin(destination);
		close();
		moveNear(destination, distance);
		

		return false;
	}

	/**
	 * Die Methode legt ein Teil an der angegebenen
	 * Position ab.
	 * @param destination - Ablageposition des Teils
	 * @return true - wenn das Teil korrekt abgelegt wurde
	 * @return false - wemm das Teil nicht abgelegt wurde
	 */
	@Override
	public boolean putPart(AbstractFrame destination) {
		return putPart(destination, 50);
	}
		

	public boolean putPart(AbstractFrame destination, double distance){
		// TODO: Ein Teil an der gegebenen Position ablegen
		moveNear(destination, distance);
		moveLin(destination);
		open();
		moveNear(destination, distance);
		return false;
	}

	/**
	 * Bewegt den Greifer PTP zum angegebenen Ziel
	 * @param destination - Zielpunkt
	 * @return true - das Ziel wurde angefahren
	 * @return false - das Ziel konnte nicht angefahren werde
	 */
	@Override
	public boolean movePTP(AbstractFrame destination) {
		try{
			this.tool.getDefaultMotionFrame().move(ptp(destination).setJointVelocityRel(maxJointVelocity).setJointAccelerationRel(maxJointAcceleration).setJointJerkRel(maxJointJerk));
		}catch(Exception e){
			System.out.println("Der Frame '"+destination.getName()+"' konnte nicht PTP angefahren werden");
			return false;
		}
		return true;	
	}
	
	public boolean movePTP(double[] destination){
		try{
			this.tool.getDefaultMotionFrame().move(ptp(destination).setJointVelocityRel(maxJointVelocity).setJointAccelerationRel(maxJointAcceleration).setJointJerkRel(maxJointJerk));
		}catch(Exception e){
			System.out.println("Die Winkelstellungen '"+destination+"' konnte nicht PTP angefahren werden");
			return false;
		}
		return true;
	}

	/**
	 * Bringt den Roboter in die angegebene Winkelstellung
	 * @param a1-a7 - Zielwinkel (in Grad) der Achsen 1 bis 7
	 * @return true - das Ziel wurde angefahren
	 * @return false - das Ziel konnte nicht angefahren werde
	 */
	@Override
	public boolean movePTP(double a1, double a2, double a3, double a4,
			double a5, double a6, double a7) {
		try{
			this.robot.move(ptp(Math.toRadians(a1),Math.toRadians(a2),Math.toRadians(a3),Math.toRadians(a4),Math.toRadians(a5),Math.toRadians(a6),Math.toRadians(a7)).setJointVelocityRel(maxJointVelocity).setJointAccelerationRel(maxJointAcceleration).setJointJerkRel(maxJointJerk));
		}catch(Exception e){
			String temp= "";
			double[] array = new double[7];
			array[0] = a1;
			array[1] = a2;
			array[2] = a3;
			array[3] = a4;
			array[4] = a5;
			array[5] = a6;
			array[6] = a7;
			for(int i = 1; i < 8; i++){
				temp +="\nAchse "+i+" = "+array[i-1]+" °";
			}
			System.out.println("Die Roboterstellung konnte nicht angefahren werden"+temp);
			
			return false;
		}
		return true;
	}

	/**
	 * Die Methode verfährt das Werkzeug auf einer geraden zum
	 * angegebenen Zielpunkt
	 * @param destination - Zielpunkt der liniaren Bewegung
	 * @return true - Bewegung wurde Ausgeführt
	 * @return false - Bewegugn konnte nicht ausgeführt werden
	 */
	@Override
	public boolean moveLin(AbstractFrame destination) {
		CartesianImpedanceControlMode cartImpCtrlMode = new	CartesianImpedanceControlMode();
		cartImpCtrlMode.parametrize(CartDOF.TRANSL).setStiffness(cartStiffness);
		cartImpCtrlMode.setNullSpaceStiffness(nullStiffness);
		try{
			this.tool.getDefaultMotionFrame().move(lin(destination)
					.setCartVelocity(maxCartVelocity)
					.setCartAcceleration(maxCartAcceleration)
					.setCartJerk(maxCartJerk)
					.setJointVelocityRel(maxJointVelocity)
					.setJointAccelerationRel(maxJointAcceleration)
					.setJointJerkRel(maxJointJerk)
					.setMode(cartImpCtrlMode));
		}catch(Exception e){
			System.out.println("Der Frame '"+destination.getName()+"' konnte nicht Linear Angefahren werden");
			return false;
		}
		return true;
	}

	/**
	 * Die Methode bewegt das Werkzeug in X-Richtung um die angegebene
	 * Distanz
	 * @param distance - Entvernung im mm
	 * @return true - Bewegung wurde ausgeführt
	 * @return false - Bewegung konnte nicht ausgeführt werden
	 */
	@Override
	public boolean moveX(double distance) {
		return moveRel(distance,0.0,0.0,0.0,0.0,0.0);
	}

	/**
	 * Die Methode bewegt das Werkzeug in Y-Richtung um die angegebene
	 * Distanz
	 * @param distance - Entvernung im mm
	 * @return true - Bewegung wurde ausgeführt
	 * @return false - Bewegung konnte nicht ausgeführt werden
	 */
	@Override
	public boolean moveY(double distance) {
		return moveRel(0.0,distance,0.0,0.0,0.0,0.0);
	}

	/**
	 * Die Methode bewegt das Werkzeug in Z-Richtung um die angegebene
	 * Distanz
	 * @param distance - Entvernung im mm
	 * @return true - Bewegung wurde ausgeführt
	 * @return false - Bewegung konnte nicht ausgeführt werden
	 */
	@Override
	public boolean moveZ(double distance) {
		return moveRel(0.0,0.0,distance,0.0,0.0,0.0);
	}

	/**
	 * Die Methode dreht das Werkzeug in A-Richtung um die angegebene
	 * Distanz
	 * @param distance - Entfernung in Grad
	 * @return true - Bewegung wurde ausgeführt
	 * @return false - Bewegung konnte nicht ausgeführt werden
	 */
	@Override
	public boolean moveA(double distance) {
		return moveRel(0.0,0.0,0.0,distance,0.0,0.0);
	}

	/**
	 * Die Methode dreht das Werkzeug in B-Richtung um die angegebene
	 * Distanz
	 * @param distance - Entfernung in Grad
	 * @return true - Bewegung wurde ausgeführt
	 * @return false - Bewegung konnte nicht ausgeführt werden
	 */
	@Override
	public boolean moveB(double distance) {
		return moveRel(0.0,0.0,0.0,0.0,distance,0.0);
	}

	/**
	 * Die Methode dreht das Werkzeug in C-Richtung um die angegebene
	 * Distanz
	 * @param distance - Entfernung in Grad
	 * @return true - Bewegung wurde ausgeführt
	 * @return false - Bewegung konnte nicht ausgeführt werden
	 */
	@Override
	public boolean moveC(double distance) {
		return moveRel(0.0,0.0,0.0,0.0,0.0,distance);
	}

	/**
	 * Die Methode bringt das Werkzeug in die Nähe einer Zielposition im
	 * Abstand von 50 mm
	 * @param destination - Zielposition
	 * @return true - Position wurde angefahren
	 * @return false - Position konnte nicht angefahren werden
	 */
	@Override
	public boolean moveNear(AbstractFrame destination) {
		return moveNear(destination, 50.0);
	}

	/**
	 * Die Methode bringt das Werkzeug in die Nähe einer Zielposition im
	 * angegebenen Abstand
	 * @param destination - Zielposition
	 * @param distance - Abstand zur Zielposition
	 * @return true - Position wurde angefahren
	 * @return false - Position konnte nicht angefahren werden
	 */
	@Override
	public boolean moveNear(AbstractFrame destination, double distance) {
		Frame temp;
		temp = destination.copy();
		temp.setZ(temp.getZ() + distance);
		return movePTP(temp);
	}

	/**
	 * Die Methode führt eine Relativbewegung des Werkzeuges aus
	 * @param x,y,z - Bewegung in X,Y und Z in mm
	 * @param a,b,c - Drehung in A,B und C in Grad
	 * @return true - Relatifbewegung wurde ausgeführt
	 * @return false - Relatifbewegung konnte nicht durchgeführt werden 
	 */
	@Override
	public boolean moveRel(double x, double y, double z, double a, double b,double c) {
		double[] array = new double[6];
		array[0] = x;
		array[1] = y;
		array[2] = z;
		array[3] = a;
		array[4] = b;
		array[5] = c;
		String temp = "";
		CartesianImpedanceControlMode cartImpCtrlMode = new	CartesianImpedanceControlMode();
		cartImpCtrlMode.parametrize(CartDOF.TRANSL).setStiffness(cartStiffness);
		cartImpCtrlMode.setNullSpaceStiffness(nullStiffness);
		try{
			this.tool.getDefaultMotionFrame().move(linRel(x,y,z,Math.toRadians(a),Math.toRadians(b),Math.toRadians(c)).setCartVelocity(maxCartVelocity).setCartAcceleration(maxCartAcceleration).setCartJerk(maxCartJerk).setMode(cartImpCtrlMode));
		}catch(Exception e){
			for(int i = 1; i < 7;i++){
				if(array[i-1] != 0.0){
					switch(i){
					case 1: temp+="\nX = "+x+" mm"; break;
					case 2: temp+="\nY = "+y+" mm"; break;
					case 3: temp+="\nZ = "+z+" mm"; break;
					case 4: temp+="\nA = "+a+" °"; break;
					case 5: temp+="\nB = "+b+" °"; break;
					case 6: temp+="\nC = "+c+" °"; break;
					}
				}
			}
			System.out.println("Relativbewegung nicht möglich"+temp);
			return false;
		}
		return true;
	}

	/**
	 * Die Methode sucht in X-Richtug die angegebene Länge nach einem Gegenstand ab
	 * @param distance - Strecke die abgesucht werden soll
	 * @return Frame - Wenn ein Gegenstand gefunden wurde
	 * @return null - wenn kein gegenstand oder ein fehler aufgetreten ist
	 */
	@Override
	public Frame findX(double distance) {
		return find(distance, CoordinateAxis.X);
	}

	/**
	 * Die Methode sucht in Y-Richtug die angegebene Länge nach einem Gegenstand ab
	 * @param distance - Strecke die abgesucht werden soll
	 * @return Frame - Wenn ein Gegenstand gefunden wurde
	 * @return null - wenn kein gegenstand oder ein fehler aufgetreten ist
	 */
	@Override
	public Frame findY(double distance) {
		return find(distance, CoordinateAxis.Y);
	}

	/**
	 * Die Methode sucht in Z-Richtug die angegebene Länge nach einem Gegenstand ab
	 * @param distance - Strecke die abgesucht werden soll
	 * @return Frame - Wenn ein Gegenstand gefunden wurde
	 * @return null - wenn kein gegenstand oder ein fehler aufgetreten ist
	 */
	@Override
	public Frame findZ(double distance) {
		return find(distance, CoordinateAxis.Z);
	}

	/**
	 * Die Methode verbindet den Greifer mit einem andern Objekt
	 * @param object - Frame des zu verbindenen Gegenstandes
	 */
	@Override
	public void attach(ObjectFrame object) {
		tool.attachTo(object);		
	}

	/**
	 * Löst den Greifer
	 */
	@Override
	public void detach() {
		tool.detach();
	}

	/**
	 * Die Methode berechnet den Z-Abstand zweier Frames
	 * @param frame1 - Erster Frame
	 * @param frame2 - Zweiter Frame
	 * @return Double - Z-Abstand zwischen beiden Frames
	 * @return null - wenn einer oder beide Frames keine Wert haben
	 */
	@Override
	public Double getDistance(AbstractFrame frame1, AbstractFrame frame2) {
		Frame temp1, temp2;
		double distance;
		int temp;
		if(frame1 == null){
			System.out.println("1. Frame wurde kein Wert zugewiesen");
			return  null;
		}else if(frame2 == null){
			System.out.println("2. Frame wurde kein Wert zugewiesen");
			return  null;
		}
		temp1 = frame1.copy();
		temp2 = frame2.copy();
		System.out.print("Z Frame 1: " + temp1.getZ());
		distance = temp1.getZ()-temp2.getZ();
		distance *= 100.0;
		temp = (int)distance;
		distance = (double)temp / 100.0;
		System.out.println("Z-Abstand beträgt "+distance+" mm");
		return distance;
	}

	@Override
	public void moveSaveLin(AbstractFrame destination) {
		// TODO Automatisch generierter Methodenstub
		
	}
	
	/**
	 * Die Methode sucht von der Startposition in 
	 * angegebener Richtung die angegebene Strecke nach einem 
	 * Teil ab und geift dieses, wenn es gefunden wird
	 * @param distace
	 */
	@Override
	public boolean findAndGetXY(double distace, CoordinateAxis direction) {
		// TODO Automatisch generierter Methodenstub
		return false;
	}

	
	private Frame find(double distance, CoordinateAxis direction) {
		ThreadUtil.milliSleep(300);
		double x = 0.0, y = 0.0, z = 0.0;
		IMotionContainer motionCmd;
		ForceCondition normalForce = ForceCondition.createNormalForceCondition(this.tool.getDefaultMotionFrame(), direction, 10.0);
		ForceSensorData data = robot.getExternalForceTorque(tool.getDefaultMotionFrame(), tool.getDefaultMotionFrame());
		Vector force = data.getForce();
		CartesianImpedanceControlMode cartImpCtrlMode = new	CartesianImpedanceControlMode();
		cartImpCtrlMode.parametrize(CartDOF.TRANSL).setStiffness(3000.0);
		cartImpCtrlMode.setNullSpaceStiffnessToDefaultValue();
		switch(direction){
			case X: cartImpCtrlMode.parametrize(CartDOF.X).setStiffness(1000); x = distance; break;
			case Y:	cartImpCtrlMode.parametrize(CartDOF.Y).setStiffness(1000); y = distance; break;
			case Z:	cartImpCtrlMode.parametrize(CartDOF.Z).setStiffness(1000); z = distance; break;
		}
		cartImpCtrlMode.setNullSpaceStiffness(nullStiffness);
		cartImpCtrlMode.parametrize(CartDOF.X).setAdditionalControlForce(-force.getX());
		cartImpCtrlMode.parametrize(CartDOF.Y).setAdditionalControlForce(-force.getY());
		cartImpCtrlMode.parametrize(CartDOF.Z).setAdditionalControlForce(-force.getZ());
		try{
			motionCmd = this.tool.getDefaultMotionFrame().move(linRel(x, y, z)
					.setMode(cartImpCtrlMode)
					.setCartVelocity(50.0)
					.setCartAcceleration(maxCartAcceleration)
					.setCartJerk(maxCartJerk).breakWhen(normalForce)
					);
		}catch(Exception e){
			System.out.println("Es konnte nicht in "+direction.toString()+"-Richtung "+distance+" mm gesucht werden");
			return null;
		}
		data = robot.getExternalForceTorque(tool.getDefaultMotionFrame(), tool.getDefaultMotionFrame());
		force = data.getForce();
		IFiredConditionInfo firedInfo = motionCmd.getFiredBreakConditionInfo();
		if(firedInfo != null){
			return this.robot.getCurrentCartesianPosition(this.tool.getDefaultMotionFrame());
		}
		System.out.println("Es konnte nichts gefunden werden");
		return null;
	}
	
	public Frame myfindZ(double z){
		
		IMotionContainer motionCmd;
		ForceCondition normalForce = ForceCondition.createNormalForceCondition(this.tool.getDefaultMotionFrame(),CoordinateAxis.Z , 10.0);
		ForceSensorData data = robot.getExternalForceTorque(tool.getDefaultMotionFrame(), tool.getDefaultMotionFrame() );
		Vector force = data.getForce(); //Get actual current forces (angle, weight, etc)
		
		CartesianImpedanceControlMode cartImpCtrlMode = new CartesianImpedanceControlMode();
		cartImpCtrlMode.setNullSpaceStiffnessToDefaultValue();
		cartImpCtrlMode.parametrize(CartDOF.X).setStiffness(3000);
		cartImpCtrlMode.parametrize(CartDOF.Y).setStiffness(3000);
		cartImpCtrlMode.parametrize(CartDOF.Z).setStiffness(1000);
		
		cartImpCtrlMode.setNullSpaceStiffness(nullStiffness);
		//Substracting the measured force to set the force to 0. Unstressed forces aren't 0 due to weight and angle etc. of the robot.
		cartImpCtrlMode.parametrize(CartDOF.X).setAdditionalControlForce(-force.getX());
		cartImpCtrlMode.parametrize(CartDOF.Y).setAdditionalControlForce(-force.getY());
		cartImpCtrlMode.parametrize(CartDOF.Z).setAdditionalControlForce(-force.getZ());
		
		//
		try{//only to check if possible ACTUAL COMMAND WHEN THE ROBOT MOVES
			motionCmd = this.tool.getDefaultMotionFrame().move(linRel(0.0, 0.0, z)
					.setMode(cartImpCtrlMode)
					.setCartVelocity(50.0)
					.setCartAcceleration(maxCartAcceleration)
					.setCartJerk(maxCartJerk).breakWhen(normalForce));
					System.out.println("motionCMD fired");
		}catch(Exception e){
			System.out.println("Es konnte nicht in Z-Richtung "+z+" mm gesucht werden");
			return null;
		}
		IFiredConditionInfo firedInfo = motionCmd.getFiredBreakConditionInfo();
		if(firedInfo != null){ //If motionCmd fired return Current Position
			return this.robot.getCurrentCartesianPosition(this.tool.getDefaultMotionFrame());
		}
		System.out.println("Es konnte nichts gefunden werden");
		return null;
	}
	
	
	

	@Override
	public boolean putPartSpline(AbstractFrame destination) {
		Frame temp, temp2;
		temp2 = this.robot.getCurrentCartesianPosition(this.tool.getDefaultMotionFrame());
		temp2.setZ(temp2.getZ() + 20.0);
		temp = destination.copy();
		temp.setZ(temp.getZ() + 20);
		Spline mySpline = new Spline(
				lin(temp2).setOrientationType(SplineOrientationType.Ignore),
				spl(temp).setOrientationType(SplineOrientationType.Ignore),
				lin(destination))
			.setCartVelocity(maxCartVelocity)
			.setCartAcceleration(maxCartAcceleration)
			.setCartJerk(maxCartJerk)
			.setJointVelocityRel(maxJointVelocity)
			.setJointAccelerationRel(maxJointAcceleration)
			.setJointJerkRel(maxJointJerk);
		try{
			this.tool.getDefaultMotionFrame().move(mySpline);
			open();
		}catch(Exception e){
			System.out.println("Splinebewegung war nicht möglich");
			return false;
		}
		return true;
	}

	
	public boolean putStoneInSand(AbstractFrame destination){
		ForceCondition normalForce = ForceCondition.createNormalForceCondition(this.tool.getDefaultMotionFrame(), CoordinateAxis.Z, 8.0);
		ForceSensorData data = robot.getExternalForceTorque(tool.getDefaultMotionFrame(), tool.getDefaultMotionFrame());
		Vector force = data.getForce();
		CartesianImpedanceControlMode cartImpCtrlMode = new	CartesianImpedanceControlMode();
		cartImpCtrlMode.parametrize(CartDOF.Z).setStiffness(1000.0);
		Frame temp, temp2, temp3;
		temp = destination.copy();
		temp.setZ(temp.getZ() + 100);
		temp2 = robot.getCurrentCartesianPosition(this.tool.getDefaultMotionFrame());
		temp2.setZ(temp2.getZ() + 50);
		temp3 = destination.copy();
		temp3.setZ(temp3.getZ() + 50);
		Spline mySpline = new Spline(lin(temp2), spl(temp),lin(temp3))
			.setCartVelocity(maxCartVelocity)
			.setCartAcceleration(maxCartAcceleration)
			.setCartJerk(maxCartJerk)
			.setMode(cartImpCtrlMode);
		try{
			this.tool.getDefaultMotionFrame().move(mySpline);
			open();
			close();
			data = robot.getExternalForceTorque(tool.getDefaultMotionFrame(), tool.getDefaultMotionFrame());
			force = data.getForce();
			cartImpCtrlMode.parametrize(CartDOF.Z).setAdditionalControlForce(-force.getZ());
			normalForce = ForceCondition.createNormalForceCondition(this.tool.getDefaultMotionFrame(), CoordinateAxis.Z, 8.0);
			IMotionContainer PlaceMotion = this.tool.getDefaultMotionFrame().move(lin(destination)
					.setCartAcceleration(maxCartAcceleration)
					.setCartJerk(maxCartJerk)
					.setCartVelocity(30.0)
					.setMode(cartImpCtrlMode)
					.breakWhen(normalForce));
			this.moveZ(-50.0);
			if(PlaceMotion.getFiredBreakConditionInfo() != null){
				return true;
			}
			System.out.println("Kein Teil Abgelegt");
		}catch(Exception e){
			System.out.println("Splinebewegung war nicht möglich");
			return false;
		}
		return false;
		
	}
	@Override
	public boolean getPartSpline(AbstractFrame destination) {
		Frame temp, temp2;
		CartesianImpedanceControlMode cartImpCtrlMode = new	CartesianImpedanceControlMode();
		cartImpCtrlMode.parametrize(CartDOF.TRANSL).setStiffness(cartStiffness);
		cartImpCtrlMode.setNullSpaceStiffness(nullStiffness);
		temp2 = this.robot.getCurrentCartesianPosition(this.tool.getDefaultMotionFrame());
		temp2.setZ(temp2.getZ() + 30.0);
		temp = destination.copy();
		temp.setZ(temp.getZ() + 20);
		Spline mySpline = new Spline(
				lin(temp2).setOrientationType(SplineOrientationType.Ignore),
				spl(temp).setOrientationType(SplineOrientationType.Ignore),
				lin(destination))
			.setCartVelocity(maxCartVelocity)
			.setCartAcceleration(maxCartAcceleration)
			.setCartJerk(maxCartJerk)
			.setMode(cartImpCtrlMode);
		
		try{
			open();
			this.tool.getDefaultMotionFrame().move(mySpline);
		}catch(Exception e){
			System.out.println("Splinebewegung war nicht möglich");
			return false;
		}
		if(close()){
			System.out.println("Kein Teil gegriffen");
			return false;
		}
		return true;
	}

	public boolean transferSpline(AbstractFrame destination, boolean Open) {
		Frame temp1 = this.robot.getCommandedCartesianPosition(this.tool.getDefaultMotionFrame());
		Frame temp2 = destination.copy();
		temp1.setZ(temp1.getZ() + 20.0);
		temp2.setZ(temp2.getZ() + 20.0);
		Frame middle = temp2.copy();
		middle.setX((temp1.getX() + temp2.getX()) / 2.0);
		middle.setY((temp1.getY() + temp2.getY()) / 2.0);
		middle.setZ(middle.getZ() + 70.0);
		Spline mySpline = new Spline(
				lin(temp1).setOrientationType(SplineOrientationType.Ignore),
				spl(middle).setOrientationType(SplineOrientationType.Ignore),
				spl(temp2).setOrientationType(SplineOrientationType.Ignore),
				lin(destination))
			.setCartVelocity(maxCartVelocity)
			.setCartAcceleration(maxCartAcceleration)
			.setCartJerk(maxCartJerk)
			.setJointVelocityRel(maxJointVelocity)
			.setJointAccelerationRel(maxJointAcceleration)
			.setJointJerkRel(maxJointJerk);
		try{
			this.tool.move(mySpline);
			if(Open){
				open();
			}
		}catch(Exception e){
			System.out.println("Splinebewegung war nicht möglich");
			return false;
		}
		return true;
	}
	public boolean transferSpline(AbstractFrame destination){
		
		return transferSpline(destination, true);
	}
	
	public boolean tryToGetStone(double Distance){
		Frame StartFrame = this.robot.getCurrentCartesianPosition(this.tool.getDefaultMotionFrame());
		CartesianImpedanceControlMode cartImpCtrlMode = new	CartesianImpedanceControlMode();
		cartImpCtrlMode.parametrize(CartDOF.TRANSL).setStiffness(cartStiffness*0.5);
		ForceCondition normalForce = ForceCondition.createNormalForceCondition(this.tool.getDefaultMotionFrame(), CoordinateAxis.Z, 8.0);
	
		CartesianSineImpedanceControlMode schwingung = new CartesianSineImpedanceControlMode();
		
		/*
		schwingung.parametrize(CartDOF.Z).setStiffness(cartStiffness);
		schwingung.parametrize(CartDOF.X).setStiffness(100.0);
		schwingung.parametrize(CartDOF.Y).setStiffness(100.0);
		schwingung.parametrize(CartDOF.A)
			.setStiffness(15)
			.setAmplitude(15.0)
			.setFrequency(0.3);
		*/
		schwingung.parametrize(CartDOF.TRANSL).setStiffness(500.0);
		schwingung.parametrize(CartDOF.Z).setStiffness(100.0);
		schwingung.parametrize(CartDOF.Z).setBias(10.0);
		schwingung.parametrize(CartDOF.ROT).setStiffness(300.0);
		schwingung.parametrize(CartDOF.A).setStiffness(100.0);
		schwingung.parametrize(CartDOF.A).setAmplitude(10.0);
		schwingung.parametrize(CartDOF.A).setFrequency(5.0);
		schwingung.setRiseTime(1.0);
		schwingung.setFallTime(1.0);
		
		
		open();
		this.tool.getDefaultMotionFrame().move(linRel(0.0,0.0,Distance)
				.setCartVelocity(maxCartVelocity * 0.3)
				.setCartAcceleration(maxCartAcceleration)
				.setCartJerk(maxCartJerk)
				.setMode(cartImpCtrlMode)
				.breakWhen(normalForce));
		//this.tool.getDefaultMotionFrame().move(linRel(0.0,0.0,-5.0));
		normalForce = ForceCondition.createNormalForceCondition(this.tool.getDefaultMotionFrame(), CoordinateAxis.Z, 20.0);
		cartImpCtrlMode.parametrize(CartDOF.TRANSL).setStiffness(5000.0);
		cartImpCtrlMode.parametrize(CartDOF.Z).setStiffness(1000.0);
		//this.tool.getDefaultMotionFrame().move(linRel(0.0,0.0,100.0).setCartVelocity(10.0).setMode(schwingung).breakWhen(normalForce));
		PositionHold bla = new PositionHold(schwingung, (long) 6.0, TimeUnit.SECONDS);
		tool.move(bla);
		close();
		this.tool.getDefaultMotionFrame().move(lin(StartFrame).setCartVelocity(maxCartVelocity));
		return false;//close();
	}

	@Override
	public boolean moveCirc(AbstractFrame Center, double angel) {
		return moveCirc(Center, angel, 1000.0);
	}

	/**
	 * Die Methode dreht den Greifer um einenen angegeben Winkel um einen Angegebneen Drehpunkt.
	 * Die beweung wird abgebrochen, wenn das angegebene maximale Drehmoment überschritten wird.
	 * @param Drehpunkt - Frame um den der Greifer gedräht werden soll
	 * @param Winkel - Winkel um den der Greifer gedreht werden soll
	 * @param MaxDrehmoment - Maximla zulässiges Drehmoment der Bewegung
	 * @return True - Wenn die Bewegun komplett durchgeführt wurde
	 */
	@Override
	public boolean moveCirc(AbstractFrame Drehpunkt, double Winkel, double MaxDrehmoment) {
		CartesianImpedanceControlMode cartImpCtrlMode = new	CartesianImpedanceControlMode();
		cartImpCtrlMode.parametrize(CartDOF.TRANSL).setStiffness(3000.0);
		cartImpCtrlMode.setNullSpaceStiffnessToDefaultValue();
		cartImpCtrlMode.parametrize(CartDOF.A).setStiffness(100.0);			
		try{
			Transformation transTool = tool.getRootFrame().transformationTo(Drehpunkt);
			if(tool.getFrame("HebelFrame") == null){
				tool.addChildFrame("HebelFrame", transTool);
			}else{
				tool.changeFramePosition(tool.getFrame("HebelFrame"), transTool);
			}
			CartesianTorqueCondition Torque = CartesianTorqueCondition.createTurningTorqueCondition(tool.getFrame("HebelFrame"), CoordinateAxis.Z, MaxDrehmoment);
			IMotionContainer motionCmd = tool.getFrame("HebelFrame").move(linRel(0.0,0.0,0.0,Math.toRadians(Winkel),0.0,0.0)
				.setJointVelocityRel(maxJointVelocity)
				.setJointAccelerationRel(maxJointAcceleration)
				.setJointJerkRel(maxJointJerk)
				.setMode(cartImpCtrlMode)
				.breakWhen(Torque));
			if(motionCmd.getFiredBreakConditionInfo() != null){
				return false;
			}
			return true;
		}catch(Exception e){
			System.err.println("Drehbewegung war nicht möglich: "+e);
			return false;
		}
	}

	
	
	
	/**
	 * Die Methode bringt die Angegebene Achse des Roboters auf die 
	 * angegebene Achsposition
	 * @param Achse - zu drehende Achse (0...6)
	 * @param Winkel - Zielwinkel der Achse
	 * @return true - wenn bewegung erfolgreich 
	 */
	public boolean moveJointPosition(int Achse, double Winkel){
		JointPosition pos = robot.getCurrentJointPosition();
		switch(Achse){
		case 0:
			if(Winkel > 170.0 || Winkel <  -170.0){
				System.err.println("Der Wert "+Winkel+" ° liegt auserhalb des Wertebereichs (-170°...+170°)");
				return false;
			}
			break;
		case 1:
			if(Winkel > 120.0 || Winkel < -120.0){
				System.err.println("Der Wert "+Winkel+" ° liegt auserhalb des Wertebereichs (-120°...+120°)");
				return false;
			}
			break;
		case 2:
			if(Winkel > 170.0 || Winkel < -170.0){
				System.err.println("Der Wert "+Winkel+" ° liegt auserhalb des Wertebereichs (-120°...+120°)");
				return false;
			}
			break;
		case 3:
			if(Winkel > 120.0 || Winkel < -120.0){
				System.err.println("Der Wert "+Winkel+" ° liegt auserhalb des Wertebereichs (-120°...+120°)");
				return false;
			}
			break;
		case 4:
			if(Winkel > 170.0 || Winkel < -170.0){
				System.err.println("Der Wert "+Winkel+" ° liegt auserhalb des Wertebereichs (-120°...+120°)");
				return false;
			}
			break;
		case 5:
			if(Winkel > 120.0 || Winkel < -120.0){
				System.err.println("Der Wert "+Winkel+" ° liegt auserhalb des Wertebereichs (-120°...+120°)");
				return false;
			}
			break;
		case 6:
			if(Winkel > 175.0 || Winkel < -175.0){
				System.err.println("Der Wert "+Winkel+" ° liegt auserhalb des Wertebereichs (-120°...+120°)");
				return false;
			}
			break;
		default:
			System.err.println("Achse "+Achse+" ausserhalb des Wertebereichs (0...6)");
			return false;
		}
		pos.set(Achse, Math.toRadians(Winkel));
		try{
			robot.move(ptp(pos)
					.setJointVelocityRel(maxJointVelocity)
					.setJointAccelerationRel(maxJointAcceleration)
					.setJointJerkRel(maxJointJerk));
			return true;
		}catch(Exception e){
			System.err.println("Bewegung nicht Möglich");
			System.err.println(e);
			return false;
		}
	}
	/**
     * Die Methode bringt die Angegebene Achse des Roboters auf die
     * angegebene Achsposition
     * @param Achse - zu drehende Achse (0...6)
     * @param Winkel - Zielwinkel der Achse
     * @param MaxTorqu - Maximales drehmoment
     * @return     -1 - wenn bewegung nicht möglich
     *              0 - wenn bewegung erfolgreich
     *              1 - wenn Drehmoment überschrittten
     */
    public int moveJointPosition(int Achse, double Winkel, double MaxTorque){
        JointPosition pos = robot.getCurrentJointPosition();
        JointTorqueCondition Torque;
        switch(Achse){
        case 0:
            if(Winkel > 170.0 || Winkel <  -170.0){
                System.err.println("Der Wert "+Winkel+" ° liegt auserhalb des Wertebereichs (-170°...+170°)");
                return -1;
            }
            Torque  =  new JointTorqueCondition(JointEnum.J1, -MaxTorque, MaxTorque);
            break;
        case 1:
            if(Winkel > 120.0 || Winkel < -120.0){
                System.err.println("Der Wert "+Winkel+" ° liegt auserhalb des Wertebereichs (-120°...+120°)");
                return -1;
            }
            Torque  =  new JointTorqueCondition(JointEnum.J2, -MaxTorque, MaxTorque);
            break;
        case 2:
            if(Winkel > 170.0 || Winkel < -170.0){
                System.err.println("Der Wert "+Winkel+" ° liegt auserhalb des Wertebereichs (-120°...+120°)");
                return -1;
            }
            Torque  =  new JointTorqueCondition(JointEnum.J3, -MaxTorque, MaxTorque);
            break;
        case 3:
            if(Winkel > 120.0 || Winkel < -120.0){
                System.err.println("Der Wert "+Winkel+" ° liegt auserhalb des Wertebereichs (-120°...+120°)");
                return -1;
            }
            Torque  =  new JointTorqueCondition(JointEnum.J4, -MaxTorque, MaxTorque);
            break;
        case 4:
            if(Winkel > 170.0 || Winkel < -170.0){
                System.err.println("Der Wert "+Winkel+" ° liegt auserhalb des Wertebereichs (-120°...+120°)");
                return -1;
            }
            Torque  =  new JointTorqueCondition(JointEnum.J5, -MaxTorque, MaxTorque);
            break;
        case 5:
            if(Winkel > 120.0 || Winkel < -120.0){
                System.err.println("Der Wert "+Winkel+" ° liegt auserhalb des Wertebereichs (-120°...+120°)");
                return -1;
            }
            Torque  =  new JointTorqueCondition(JointEnum.J6, -MaxTorque, MaxTorque);
            break;
        case 6:
            if(Winkel > 175.0 || Winkel < -175.0){
                System.err.println("Der Wert "+Winkel+" ° liegt auserhalb des Wertebereichs (-120°...+120°)");
                return -1;
            }
            Torque  =  new JointTorqueCondition(JointEnum.J7, -MaxTorque, MaxTorque);
            break;
        default:
            System.err.println("Achse "+Achse+" ausserhalb des Wertebereichs (0...6)");
            return -1;
        }
        pos.set(Achse, Math.toRadians(Winkel));
        try{
            IMotionContainer Rotate =  robot.move(ptp(pos)
                    .setJointVelocityRel(maxJointVelocity)
                    .setJointAccelerationRel(maxJointAcceleration)
                    .setJointJerkRel(maxJointJerk)
                    .breakWhen(Torque));
            if(Rotate.getFiredBreakConditionInfo() == null){
                return 0;
            }
            return 1;
        }catch(Exception e){
            System.err.println("Bewegung nicht Möglich");
            System.err.println(e);
            return -1;
        }
    }
	
}
