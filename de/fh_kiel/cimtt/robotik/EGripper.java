package de.fh_kiel.cimtt.robotik;



import java.util.Timer;
import java.io.IOException;
import java.util.Properties;

import com.kuka.common.ThreadUtil;
import com.kuka.generated.ioAccess.*;
import com.kuka.roboticsAPI.conditionModel.ForceCondition;
import com.kuka.roboticsAPI.controllerModel.Controller;
import com.kuka.roboticsAPI.deviceModel.LBR;
import com.kuka.roboticsAPI.geometricModel.AbstractFrame;
import com.kuka.roboticsAPI.geometricModel.Tool;
import com.kuka.roboticsAPI.geometricModel.math.CoordinateAxis;
import com.kuka.roboticsAPI.geometricModel.math.Vector;
import com.kuka.roboticsAPI.sensorModel.ForceSensorData;

public class EGripper extends Gripper{
	FlexFellowIOGroup digitalOut;
	Tool toolEgripper;
	LBR robotEgripper;
	/**
	 * 
	 * @param controller Verwendeter Controller
	 * @param robot	Verwendetet Roboter
	 * @param tool Verwendeter Greifer
	 */
	public EGripper(Controller controller, LBR robot, Tool tool){
		super(controller, robot, tool);

	    	

	    // Hier aufbauen der GUI mit den Einstellungen 
	    // in der Property-Datei
		
		digitalOut = new FlexFellowIOGroup(controller);
		toolEgripper = tool;
		robotEgripper = robot;
	}
	
	/**
	 * @return true wenn Greifer vollständig geöffnet
	 * @return false wenn nicht
	 */
	public boolean close(){
		digitalOut.setCloseGripper(true);
		digitalOut.setOpenGripper(false);
		for(int i = 0; i < 10; i++){
			if(digitalOut.getGripperIsClose()){
				return true;
			}
			ThreadUtil.milliSleep(100);
		}
		return false;
	}
	
	/**
	 * @return true wenn Greifer vollständig geschlossen
	 * @return false wenn nicht
	 */
	public boolean open(){
		digitalOut.setCloseGripper(false);
		digitalOut.setOpenGripper(true);
		for(int i = 0; i < 10; i++){
			if(digitalOut.getGripperIsOpen()){
				digitalOut.setOpenGripper(false);
				return true;
			}
			ThreadUtil.milliSleep(100);
		}
		digitalOut.setOpenGripper(false);
		return false;
	}
	

	/*
	 * Task: Fill glass with one liquid
	 * - gets ingredient Frame and amount of liquid 
	 * - stops when current weight 
	 * */
	public boolean fillGlass(AbstractFrame ingredient, int amount) {
		//Move to Bottle
		this.moveNear(ingredient);
		 
		//Move up
		this.findZ(10); //Move until contact
		//TODO: Configure Sensibility and check distance
		this.moveZ(10); //Move up to fill
		/*	- implement timer
		 * TODO: 
		 * 	- Wait for fluid to fill 
		 * 	- Measure weight of filling and go on
		 * 	- Feature: Wait for xx seconds, if weight isn't changing go up and down again 
		*/
		Timer timer = new Timer();
		
		
		//TODO: Move down
		this.moveZ(-10); //Measure Distance
		
		
		return false;
	}
	
	
}
