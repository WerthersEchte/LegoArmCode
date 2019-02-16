package application.background;


import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.kuka.roboticsAPI.applicationModel.tasks.CycleBehavior;
import com.kuka.roboticsAPI.applicationModel.tasks.RoboticsAPICyclicBackgroundTask;
import com.kuka.roboticsAPI.controllerModel.Controller;
import com.kuka.roboticsAPI.deviceModel.LBR;
import com.kuka.roboticsAPI.persistenceModel.IPersistenceEngine;
import com.kuka.roboticsAPI.persistenceModel.XmlApplicationDataSource;
import com.kuka.roboticsAPI.persistenceModel.templateModel.TemplateElement;
import com.kuka.roboticsAPI.persistenceModel.templateModel.ToolTemplate;

public class InformationSpreadingTask extends RoboticsAPICyclicBackgroundTask {
	private Controller mKUKA_Sunrise_Cabinet_1;
	private LBR mLBR_IIWA_7_R800_1;
	
	DataServer mDataServer;

	public void initialize() {
		mKUKA_Sunrise_Cabinet_1 = getController("KUKA_Sunrise_Cabinet_1");
		mLBR_IIWA_7_R800_1 = (LBR) getDevice(mKUKA_Sunrise_Cabinet_1, "LBR_iiwa_7_R800_1");
		
		try {
			mDataServer = new DataServer( InetAddress.getByName("172.31.1.240"), 30000 );
		} catch ( Exception vAll ) {
			vAll.printStackTrace();
		}
		if( mDataServer != null){
			initializeCyclic(3000, 25, TimeUnit.MILLISECONDS, CycleBehavior.BestEffort);
		}
	}
	
	Data mData = new Data();
	private List<com.kuka.roboticsAPI.geometricModel.Tool> mTools = new ArrayList<com.kuka.roboticsAPI.geometricModel.Tool>();
	
	@Override
	protected void runCyclic() {

		final IPersistenceEngine engine = this.getContext().getEngine(IPersistenceEngine.class);
		final XmlApplicationDataSource defaultDataSource = (XmlApplicationDataSource) engine.getDefaultDataSource();
		
		if( mTools.isEmpty() ){
			for(TemplateElement vPotentialTool : defaultDataSource.loadAllTemplates()){
				if( vPotentialTool instanceof ToolTemplate){
					com.kuka.roboticsAPI.geometricModel.Tool vTool = getApplicationData().createFromTemplate(vPotentialTool.getName());
					mTools.add(vTool);
				}
			}
		}
		
		if( mDataServer != null && mDataServer.isConnected() ){
			mDataServer.sendData(mData.setData(mLBR_IIWA_7_R800_1, mTools, defaultDataSource));
		}
		
	}
}