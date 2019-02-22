package application.tools;

import static application.core.Devices.getLbr;
import static application.core.Gripper.closeGripper;
import static application.core.Gripper.openGripper;
import static application.core.Movement.linearMoveToPoint;
import static application.tools.Points.getOrigin;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.ptp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import application.core.Movement;

import static application.core.Logging.log;
import static application.core.Devices.getUI;
import static application.core.Devices.getApp;

import com.kuka.roboticsAPI.geometricModel.Frame;
import com.kuka.roboticsAPI.uiModel.ApplicationDialogType;

public class Points {
	
	public enum Point{
		Origin,
		Assembly,
		Parkingspot1, 
		Parkingspot2, 
		Parkingspot3, 
		Parkingspot4, 
		Parkingspot5,
		Export1,
		Export2,
		Export3,
		Export4,
		Import1,
		Import2,
		Import3,
		Import4,
		Wheel1, 
		Wheel2, 
		Body,
		Window,
		Roof,
		Door,
		Trunk;
		
		private Frame mPosition = new Frame(0,0,0,0,0,0);

		public Frame getPosition() {
			return mPosition;
		}

		public void setPosition(Frame aPosition) {
			mPosition = aPosition;
		}
		
	}

	public static Frame getOrigin() {
		return Point.Origin.getPosition();
	}

	
	public static String parseFrame(Frame aFrame){
		Frame vFrameToSave = aFrame.copy();
		if(aFrame != Point.Origin.getPosition()){
			vFrameToSave.setX(vFrameToSave.getX()-Point.Origin.getPosition().getX());
			vFrameToSave.setY(vFrameToSave.getY()-Point.Origin.getPosition().getY());
			vFrameToSave.setZ(vFrameToSave.getZ()-Point.Origin.getPosition().getZ());
			
			double vWinkel = Point.Origin.getPosition().getAlphaRad();

			double vNewX = vFrameToSave.getY() * Math.sin(vWinkel) + vFrameToSave.getX() * Math.cos(vWinkel);
			double vNewY = vFrameToSave.getY() * Math.cos(vWinkel) - vFrameToSave.getX() * Math.sin(vWinkel);
			
			vFrameToSave.setX(vNewX);
			vFrameToSave.setY(vNewY);
			
			vFrameToSave.setAlphaRad(vFrameToSave.getAlphaRad()-Point.Origin.getPosition().getAlphaRad());
			vFrameToSave.setBetaRad(vFrameToSave.getBetaRad()-Point.Origin.getPosition().getBetaRad());
			vFrameToSave.setGammaRad(vFrameToSave.getGammaRad()-Point.Origin.getPosition().getGammaRad());			
		}
		log("Point saved", vFrameToSave.toString());
		return vFrameToSave.getX() + "|" + vFrameToSave.getY() + "|" + vFrameToSave.getZ() + "|" + vFrameToSave.getAlphaRad() + "|" + vFrameToSave.getBetaRad() + "|" + vFrameToSave.getGammaRad();
	}
	
	public static void loadPoints(){
		for(Point vPoint: Point.values()){
			
			if( getApp().tryGetProcessData(vPoint.name()) != null && !getApp().getProcessData(vPoint.name()).getValue().toString().isEmpty()){
				log("Points", vPoint.name() + ": " + getApp().getProcessData(vPoint.name()).getValue());
				
				String[] vValuesOfFrame = getApp().getProcessData(vPoint.name()).getValue().toString().split("\\|");
				log("Points", vPoint.name() + ": " + Arrays.toString(vValuesOfFrame));
				
				if(vPoint == Point.Origin){
					vPoint.setPosition(new Frame(0,0,0,0,0,0));
				}
				
				double vWinkel = Point.Origin.getPosition().getAlphaRad();
				
				double vOldX = Double.parseDouble(vValuesOfFrame[0]);
				double vOldY = Double.parseDouble(vValuesOfFrame[1]);
								
				vPoint.setPosition(new Frame(
						vOldX * Math.cos(vWinkel) - vOldY * Math.sin(vWinkel) + getOrigin().getX(),
						vOldX * Math.sin(vWinkel) + vOldY * Math.cos(vWinkel) + getOrigin().getY(),  
						Double.parseDouble(vValuesOfFrame[2]) + getOrigin().getZ(),  
						Double.parseDouble(vValuesOfFrame[3]) + getOrigin().getAlphaRad(),  
						Double.parseDouble(vValuesOfFrame[4]) + getOrigin().getBetaRad(),  
						Double.parseDouble(vValuesOfFrame[5]) + getOrigin().getGammaRad()));
				
				log("Points", vPoint.name() + ": " + vPoint.getPosition());
			}
		}
	}
	
	public static void calibrate(){
		log("Kalibrieren", "Move to ReadyToCalibrate");
		linearMoveToPoint(getApp().getFrame("/ReadyToCalibrate"), 100);
		closeGripper();

		log("Kalibrieren", "Holding position in impedance control mode");

		Movement.freeMovementStart();
		getUI().displayModalDialog(ApplicationDialogType.INFORMATION, "Press ok to set origin.", "OK");
				
		Point.Origin.setPosition(getLbr().getCurrentCartesianPosition(getLbr().getFlange()));
		getOrigin().setZ(getOrigin().getZ()+100);
		
		Movement.freeMovementStop();
		
		Frame vCalibrationSlot = getOrigin().copy();
		vCalibrationSlot.setZ(vCalibrationSlot.getZ()-100);

		linearMoveToPoint(getOrigin(), 150);
		linearMoveToPoint(vCalibrationSlot, 100);
		linearMoveToPoint(getOrigin(), 150);
		
		getApp().getProcessData(Point.Origin.name()).setValue(parseFrame(getOrigin()));
		loadPoints();
	}
	
	private static Point vCurrentPoint = null;
	
	public static void setPoints(){
		
		if(vCurrentPoint == null){
			selectPoint();
		}
		
		log("Points", "Set Point" + vCurrentPoint.name() );

		Movement.freeMovementStart();
		
		int direction = 0;
		do{
			direction = getUI().displayModalDialog(
				ApplicationDialogType.QUESTION,
				"Punkt " + vCurrentPoint.name() + " setzen",
				 "Zurück", "Gripper auf", "Gripper zu", "Punkt setzen", "Punkt auswählen", "Punkt anfahren");
		
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
					vCurrentPoint.setPosition(getLbr().getCurrentCartesianPosition(getLbr().getFlange()));
					getApp().getProcessData(vCurrentPoint.name()).setValue(parseFrame(vCurrentPoint.getPosition()));
					break;
				case 4:
					selectPoint();
					break;
				case 5:
					Movement.freeMovementStop();
					linearMoveToPoint(vCurrentPoint.getPosition(), 150);
					Movement.freeMovementStart();
					break;
			}
		} while(direction != 0);
		
		Movement.freeMovementStop();
	}

	private static void selectPoint() {
		List<String> vNames = new ArrayList<String>();
		
		for( Point vPoint : Point.values()){
			vNames.add(vPoint.name());
		}
		
		int direction = 0, page = 0;
		do{

			List<String> vPage = new ArrayList<String>();
			vPage.add("Zurück");
			vPage.add("Vor");
			vPage.addAll(vNames.subList(page, Math.min(page+5, vNames.size())));
			
			direction = getUI().displayModalDialog(
					ApplicationDialogType.QUESTION,
					"Punkt auswählen", vPage.toArray(new String[0])
					);
		
			switch (direction) {
				case 0:
					page -= 5;
					page = Math.max(0, page);
					break;
				case 1:
					page += 5;
					page = Math.min(vNames.size(), page);
					break;
			}
			
		} while(direction <= 1);
		
		vCurrentPoint = Point.values()[direction-2+page];	
		
	}
	
	
	
	

}
