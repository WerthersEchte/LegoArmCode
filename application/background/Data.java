package application.background;

import java.util.ArrayList;
import java.util.List;

import com.jsoniter.output.JsonStream;
import com.kuka.roboticsAPI.deviceModel.LBR;
import com.kuka.roboticsAPI.geometricModel.ObjectFrame;
import com.kuka.roboticsAPI.persistenceModel.XmlApplicationDataSource;

public class Data {

	double mX=0.0, mY=0.0, mZ=0.0, mAlphaRad=0.0, mBetaRad=0.0, mGammaRad=0.0;
	double[] mCommandedJointPosition = null;
	double[] mCurrentJointPosition = null;
	List<Frame> mFrames = new ArrayList<Frame>();
	List<Tool> mTools = new ArrayList<Tool>();
	
	List<DefaultMarkerPosition> mDefaultMarkerPositions = new ArrayList<DefaultMarkerPosition>();
	
	public Data(){
		for( DefaultMarkerPositions vMarker : DefaultMarkerPositions.values() ){
			mDefaultMarkerPositions.add(new DefaultMarkerPosition(vMarker.name().substring(1), new float[]{vMarker.getXDefault(), vMarker.getYDefault(), vMarker.getZDefault(), vMarker.getAlphaDefault(), vMarker.getBetaDefault(), vMarker.getGammaDefault()}) );
		}
	}
	
	public Data setData( LBR aLBR, List<com.kuka.roboticsAPI.geometricModel.Tool> aTools, XmlApplicationDataSource aDataSource ){

		mX = aLBR.getFlange().getX();
		mY = aLBR.getFlange().getY();
		mZ = aLBR.getFlange().getZ();
		mAlphaRad = aLBR.getFlange().getAlphaRad();
		mBetaRad = aLBR.getFlange().getBetaRad();
		mGammaRad = aLBR.getFlange().getGammaRad();
		
		mCommandedJointPosition = aLBR.getCommandedJointPosition().getInternalArray();
		mCurrentJointPosition = aLBR.getCurrentJointPosition().getInternalArray();
		
		mFrames.clear();
		mTools.clear();
		if( aDataSource != null ){
			for(ObjectFrame vFrame : aDataSource.loadAllFrames()){
				mFrames.add(new Frame(vFrame));
				
				List<ObjectFrame> vFramesToCheck  = new ArrayList<ObjectFrame>(vFrame.getChildrenSnapshot());
				while( !vFramesToCheck.isEmpty() ){
					ObjectFrame vCurrentFrame = vFramesToCheck.remove(0);
					mFrames.add(new Frame(vCurrentFrame));
					vFramesToCheck.addAll(vCurrentFrame.getChildrenSnapshot());
				}
			}
			
			for(com.kuka.roboticsAPI.geometricModel.Tool vTool : aTools){
				mTools.add(new Tool(vTool.getName(), vTool.getDefaultMotionFrame().getName(), vTool.getRootFrame()));
			}
		}
		
		return this;
	}

	@Override
	public String toString() {
		return JsonStream.serialize(this);
	}
}
