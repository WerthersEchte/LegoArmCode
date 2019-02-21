package application.core;

import com.kuka.roboticsAPI.uiModel.userKeys.IUserKey;
import com.kuka.roboticsAPI.uiModel.userKeys.IUserKeyBar;
import com.kuka.roboticsAPI.uiModel.userKeys.IUserKeyListener;
import com.kuka.roboticsAPI.uiModel.userKeys.UserKeyAlignment;
import com.kuka.roboticsAPI.uiModel.userKeys.UserKeyEvent;

public class GuiKeys {
	
	public static void createGuiKeys(){
		IUserKeyBar vMovementbar = Devices.getUI().createUserKeyBar("Bewegung");
		IUserKey vStopMovementKey = vMovementbar.addUserKey(0, new IUserKeyListener() {
			
			@Override
			public void onKeyEvent(IUserKey key, UserKeyEvent event) {
				Movement.stopCurentMove();				
			}
		}, true);
		vStopMovementKey.setText(UserKeyAlignment.Middle, "Stop M");
		
		IUserKey vStopFreemoveKey = vMovementbar.addUserKey(1, new IUserKeyListener() {
			
			@Override
			public void onKeyEvent(IUserKey key, UserKeyEvent event) {
				Movement.freeMovementStop();			
			}
		}, true);
		vStopFreemoveKey.setText(UserKeyAlignment.Middle, "Stop F");
	}

}
