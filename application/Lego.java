package application;

import static application.core.Devices.getControll;
import static application.core.Devices.getLbr;
import static application.core.Devices.getTool;
import static application.core.Gripper.isGripperOpen;
import static application.core.Gripper.openGripper;
import static application.core.Gripper.closeGripper;
import static application.core.Movement.linearMoveToPoint;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.lin;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.ptp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static application.tools.Points.getOrigin;
import application.tools.Measure;
import application.tools.Points;

import application.core.Devices;
import application.core.GuiKeys;
import application.core.Logging;
import application.core.Movement;

import com.kuka.roboticsAPI.applicationModel.RoboticsAPIApplication;
import com.kuka.roboticsAPI.deviceModel.LBR;
import com.kuka.roboticsAPI.geometricModel.CartDOF;
import com.kuka.roboticsAPI.geometricModel.Frame;
import com.kuka.roboticsAPI.geometricModel.Tool;
import com.kuka.roboticsAPI.motionModel.IMotionContainer;
import com.kuka.roboticsAPI.motionModel.PositionHold;
import com.kuka.roboticsAPI.motionModel.controlModeModel.CartesianImpedanceControlMode;
import com.kuka.roboticsAPI.uiModel.ApplicationDialogType;

import de.fh_kiel.cimtt.robotik.EGripper;

public class Lego extends RoboticsAPIApplication {

	public void initialize() {
		Devices.setControll(getController("KUKA_Sunrise_Cabinet_1"));
		
		Devices.setLbr((LBR) getDevice(getControll(), "LBR_iiwa_7_R800_1"));
		
		Devices.setTool((Tool) getApplicationData().createFromTemplate("EGripper"));
		Devices.getTool().attachTo(getLbr().getFlange());
		
		Devices.setGripper(new EGripper(getControll(), getLbr(), getTool()));
		
		Logging.setLogger(getLogger());
		Devices.setUI(getApplicationUI());
		Devices.setApp(getApplicationData());
		
		GuiKeys.createGuiKeys();
		
		Points.loadPoints();
	}
	
	public void gripperTest(){
		int direction = 0;
		do{
			direction = getApplicationUI().displayModalDialog(
				ApplicationDialogType.QUESTION,
				"Gripper",
				"Auf", "Zu", "Aufhören");
		
			switch (direction) {
				case 0:
					openGripper();
					break;
				case 1:
					closeGripper();
					break;
				case 2:
					break;
			}
		}while(direction != 2);
		
	}
	
	public void pickupCar(){
		if(getOrigin() == null){
			getLogger().info("Please calibrate");
			getApplicationUI().displayModalDialog(ApplicationDialogType.INFORMATION, "Please Calibrate", "OK");
			return;
		}
		// 113mm x-, 115mm y-
		Frame vOverBench = getOrigin().copy();
		vOverBench.setY(vOverBench.getY()-115);
		vOverBench.setX(vOverBench.getX()-112);
		vOverBench.setAlphaRad(vOverBench.getAlphaRad() + Math.toRadians(90));
		
		linearMoveToPoint(vOverBench, 150);
		openGripper();
		
		CartesianImpedanceControlMode cartImpCtrlMode = new CartesianImpedanceControlMode();
		cartImpCtrlMode.parametrize(CartDOF.Z).setDamping(0.1);
		cartImpCtrlMode.parametrize(CartDOF.Z).setStiffness(1.0);
		
		Frame vBench = vOverBench.copy();
		vBench.setZ(vBench.getZ()-100);
		
		linearMoveToPoint(vBench,30,cartImpCtrlMode);
		closeGripper();
		
		linearMoveToPoint(vOverBench, 150);
		
	}
	
	public void parkCar(){
		if(getOrigin() == null){
			getLogger().info("Please calibrate");
			getApplicationUI().displayModalDialog(ApplicationDialogType.INFORMATION, "Please Calibrate", "OK");
			return;
		}
		if(isGripperOpen()){
			getLogger().info("No car gripped");
			getApplicationUI().displayModalDialog(ApplicationDialogType.INFORMATION, "No Car held!", "OK");
			return;
		}
		
		int direction = 0;

		Frame vOverParkPosition = getOrigin().copy();
		vOverParkPosition.setY(vOverParkPosition.getY()-175);
		vOverParkPosition.setX(vOverParkPosition.getX()-30);
		
		direction = getApplicationUI().displayModalDialog(
				ApplicationDialogType.QUESTION,
				"Parkposition",
				"Zurück", "1", "2");
		
		switch (direction) {
			case 0:
				return;
			case 1:
				break;
			case 2:
				break;
		}

		Frame vParkPosition = vOverParkPosition.copy();
		vParkPosition.setZ(vParkPosition.getZ()-100);
		
		linearMoveToPoint(vOverParkPosition, 150);
		
		CartesianImpedanceControlMode cartImpCtrlMode = new CartesianImpedanceControlMode();
		cartImpCtrlMode.parametrize(CartDOF.Z).setDamping(0.1);
		cartImpCtrlMode.parametrize(CartDOF.Z).setStiffness(1.0);
		

		linearMoveToPoint(vParkPosition,30,cartImpCtrlMode);
		openGripper();
		
		linearMoveToPoint(vOverParkPosition, 150);
		
	}
	
	public void run() {		
		int direction = 0;
		do{
			List<String> vOptions = new ArrayList<String>();
			vOptions.add("Aufhören");
			vOptions.add("Kalibrieren");
			if(getOrigin() != null){
				vOptions.add("Messen");
				vOptions.add("Set Points");
			}
			
			direction = getApplicationUI().displayModalDialog(
				ApplicationDialogType.QUESTION,
				"Was soll gemacht werden",
				vOptions.toArray(new String[vOptions.size()]) );
		
			switch (direction) {
				case 0:
					break;
				case 1:
					Points.calibrate();
					break;
				case 2:
					Measure.measure();
					break;
				case 3:
					Points.setPoints();
					break;
				case 4:
					parkCar();
					break;
				case 5:
					linearMoveToPoint(getOrigin(), 150);
					break;
			}
		}while(direction != 0);
		
	}

	public static void main(String[] args) {
		Lego app = new Lego();
		app.runApplication();
	}
}
