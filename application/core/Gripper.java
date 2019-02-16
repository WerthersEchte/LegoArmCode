package application.core;

import static application.core.Devices.getGripper;

public class Gripper {
	
	private static boolean mGripperOpen = false;
	public static boolean isGripperOpen(){
		return mGripperOpen;
	}
	
	public static void openGripper(){
		getGripper().open();
		mGripperOpen = true;
	}
	
	public static void closeGripper(){
		getGripper().close();
		mGripperOpen = false;
	}

}
