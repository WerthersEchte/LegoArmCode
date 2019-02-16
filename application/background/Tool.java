package application.background;

import java.util.ArrayList;
import java.util.List;

import com.kuka.roboticsAPI.geometricModel.ObjectFrame;

public class Tool {
	
	String mName;
	
	String mDefaultMotionFrame;
	List<Frame> mFrames = new ArrayList<Frame>();

	public Tool(String aName, String aDefaultMotionFrame,ObjectFrame aRootFrame) {
		mName = aName;
		mDefaultMotionFrame = aDefaultMotionFrame;
		
		List<ObjectFrame> vFramesToCheck  = new ArrayList<ObjectFrame>(aRootFrame.getChildrenSnapshot());
		while( !vFramesToCheck.isEmpty() ){
			ObjectFrame vCurrentFrame = vFramesToCheck.remove(0);
			mFrames.add(new Frame(vCurrentFrame));
			vFramesToCheck.addAll(vCurrentFrame.getChildrenSnapshot());
		}
	}
}
