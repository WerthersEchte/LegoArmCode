package application.core;

import com.kuka.roboticsAPI.uiModel.userKeys.IUserKey;
import com.kuka.roboticsAPI.uiModel.userKeys.IUserKeyBar;
import com.kuka.roboticsAPI.uiModel.userKeys.IUserKeyListener;
import com.kuka.roboticsAPI.uiModel.userKeys.UserKeyAlignment;
import com.kuka.roboticsAPI.uiModel.userKeys.UserKeyEvent;
import com.kuka.roboticsAPI.uiModel.userKeys.UserKeyLED;
import com.kuka.roboticsAPI.uiModel.userKeys.UserKeyLEDSize;

import static application.core.Logging.log;

public class GuiKeys {
	
	private static IUserKeyBar vMovementbar;
	
	public static void createGuiKeys(){
		vMovementbar = Devices.getUI().createUserKeyBar("Bewegung");
		
		IUserKey vStopMovementKey = vMovementbar.addUserKey(0, new IUserKeyListener() {
			
			@Override
			public void onKeyEvent(IUserKey key, UserKeyEvent event) {
				if (event == UserKeyEvent.KeyDown) {
					log("Cut");
					Devices.getIOs().setSignalLightRed(true);
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Devices.getIOs().setSignalLightRed(false);
					Movement.stopCurentMove();
				}
			}
			
		}, false);
		vStopMovementKey.setText(UserKeyAlignment.Middle, "Stop M");
		vStopMovementKey.setCriticalText("Kills Movement");
		
		IUserKey vStopFreemoveKey = vMovementbar.addUserKey(1, new IUserKeyListener() {
			
			@Override
			public void onKeyEvent(IUserKey key, UserKeyEvent event) {
				log("Grind");
				Movement.freeMovementStop();			
			}
			
		}, false);
		vStopFreemoveKey.setText(UserKeyAlignment.Middle, "Stop F");
		vStopFreemoveKey.setCriticalText("Kills Free Movement");
		
		vMovementbar.publish();
		
	}

}
