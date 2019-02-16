package application.background;

import com.kuka.roboticsAPI.geometricModel.ObjectFrame;

public class Frame {

	String mID, mName, mParent = "";
	
	double mX, mY, mZ, mAlphaRad, mBetaRad, mGammaRad;

	public Frame(
			String aID,
			String aName, 
			double aX, double aY, double aZ,
			double aAlphaRad, double aBetaRad, double aGammaRad,
			ObjectFrame aParent ) {
		mID = aID;
		mName = aName;
		mX = aX;
		mY = aY;
		mZ = aZ;
		mAlphaRad = aAlphaRad;
		mBetaRad = aBetaRad;
		mGammaRad = aGammaRad;
		if(aParent != null){
			mParent = aParent.getId();
		}
	}

	public Frame(ObjectFrame aFrame) {
		if(aFrame == null){
			return;
		}
		mID = aFrame.getId();
		mName = aFrame.getName();
		mX = aFrame.getX();
		mY = aFrame.getY();
		mZ = aFrame.getZ();
		mAlphaRad = aFrame.getAlphaRad();
		mBetaRad = aFrame.getBetaRad();
		mGammaRad = aFrame.getGammaRad();
		if(aFrame.getParent() != null){
			mParent = aFrame.getParent().getId();
		}
		
	}

}
