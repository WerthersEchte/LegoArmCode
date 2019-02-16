package application.core;

import com.kuka.roboticsAPI.applicationModel.IApplicationData;
import com.kuka.roboticsAPI.controllerModel.Controller;
import com.kuka.roboticsAPI.deviceModel.LBR;
import com.kuka.roboticsAPI.geometricModel.Tool;
import com.kuka.roboticsAPI.uiModel.IApplicationUI;

import de.fh_kiel.cimtt.robotik.EGripper;

public class Devices {
	private static Controller Controller;
	private static LBR Lbr;
	private static Tool Tool;
	private static EGripper Gripper;
	
	private static IApplicationUI UI;
	private static IApplicationData App;
	
	public static Controller getControll() {
		return Controller;
	}
	public static void setControll(Controller controller) {
		Controller = controller;
	}
	public static LBR getLbr() {
		return Lbr;
	}
	public static void setLbr(LBR lbr) {
		Lbr = lbr;
	}
	public static Tool getTool() {
		return Tool;
	}
	public static void setTool(Tool tool) {
		Tool = tool;
	}
	public static EGripper getGripper() {
		return Gripper;
	}
	public static void setGripper(EGripper gripper) {
		Gripper = gripper;
	}
	public static void setUI(IApplicationUI applicationUI) {
		UI = applicationUI;
	}
	public static IApplicationUI getUI() {
		return UI;
	}
	public static void setApp(IApplicationData applicationData) {
		App = applicationData;
	}
	public static IApplicationData getApp() {
		return App;
	}
	

}
