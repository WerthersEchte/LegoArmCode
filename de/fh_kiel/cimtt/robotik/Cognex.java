package de.fh_kiel.cimtt.robotik;

import static com.kuka.roboticsAPI.motionModel.BasicMotions.linRel;

import java.net.InetAddress;
import java.net.UnknownHostException;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.ModbusIOException;
import net.wimpi.modbus.ModbusSlaveException;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.net.TCPMasterConnection;

import com.kuka.common.ThreadUtil;
import com.kuka.roboticsAPI.conditionModel.ForceCondition;
import com.kuka.roboticsAPI.deviceModel.LBR;
import com.kuka.roboticsAPI.geometricModel.CartDOF;
import com.kuka.roboticsAPI.geometricModel.Frame;
import com.kuka.roboticsAPI.geometricModel.Tool;
import com.kuka.roboticsAPI.geometricModel.math.CoordinateAxis;
import com.kuka.roboticsAPI.geometricModel.math.Transformation;
import com.kuka.roboticsAPI.geometricModel.math.Vector;
import com.kuka.roboticsAPI.motionModel.controlModeModel.CartesianImpedanceControlMode;
import com.kuka.roboticsAPI.sensorModel.ForceSensorData;

public class Cognex {
	private InetAddress address;
	private TCPMasterConnection connection;
	private ReadMultipleRegistersRequest request;
	private ModbusTCPTransaction trans;
	private ReadMultipleRegistersResponse response;
	private int CameraPort;
	private Transformation transformation;
	private Frame Temp;
	private LBR lbr;
	
	public static final int READ_ROTATION_PATTERN = 30010;
	public static final int READ_X_PATTERN = 30011;
	public static final int READ_Y_PATTERN = 30012;
	public static final int READ_X_STONE = 30013;
	public static final int READ_Y_STONE = 30014;
	public static final int READ_STONE_COUNT = 30015;
	public static final int READ_STONE_COLOR = 30016;
	public static final int READ_NEED_SHAKE = 30017;
	public static final int READ_STONE_KONTRAST = 30020;
	
	private static double maxCartVelocity = 300.0;
	private static double maxCartAcceleration = 500.0;
	private static double maxCartJerk = 5000.0;
	
	private static double maxJointVelocity = 0.15;
	private static double maxJointAcceleration = 0.2;
	private static double maxJointJerk = 0.02;
	
	private static double cartStiffness = 2000.0;
	private static double nullStiffness = 0.5;
	
	public Cognex(){
		try {
			address = InetAddress.getByName("172.31.1.5");
		} catch (UnknownHostException e) {
			
		}
		CameraPort = Modbus.DEFAULT_PORT;
		establishConnexion();
	}
	
	public Cognex(String addr){
		try {
			address = InetAddress.getByName(addr);
		} catch (UnknownHostException e) {
			System.err.println(addr+" ist keine Gültige Adresse!");
		}
		CameraPort = Modbus.DEFAULT_PORT;
		establishConnexion();
	}
	
	public Cognex(String addr, int port){
		try {
			address = InetAddress.getByName(addr);
		} catch (UnknownHostException e) {
			System.err.println(addr+" ist keine Gültige Adresse!");
		}
		CameraPort = port;
		establishConnexion();
	}
	

	public Frame getPosition(double X, double Y){//We use the X and Y from the camera frame
		Frame Output = this.Temp.copy();
		Output.setX(X);
		Output.setY(Y);
		Output.setZ(0);
		Output.setAlphaRad(0);
		Output.setBetaRad(0);
		Output.setGammaRad(0);
		if(transformation == null){
			return null;
		}
		Output.transform(this.lbr.getRootFrame(), transformation);
		Output.setGammaRad(3.141);
		return Output;
	}
	
	public boolean getTransformation(Tool tool, LBR LBR, Frame FRAME){
		this.lbr = LBR;
		this.Temp = FRAME;
		int Reg[] = new int[5];
		double Alpha = 0.0;
		Transformation transTool;
		Transformation transRefPoint;
		
		CartesianImpedanceControlMode cartImpCtrlMode = new	CartesianImpedanceControlMode();
		cartImpCtrlMode.parametrize(CartDOF.Z).setStiffness(1000.0);
		cartImpCtrlMode.parametrize(CartDOF.TRANSL).setDamping(1.0);
		cartImpCtrlMode.parametrize(CartDOF.ROT).setStiffness(200.0);
		//cartImpCtrlMode.setNullSpaceStiffnessToDefaultValue();
		ForceCondition normalForce = ForceCondition.createNormalForceCondition(tool.getDefaultMotionFrame(), CoordinateAxis.Z, 10.0);
		lbr.move(linRel(0.0, 0.0, 200.0).setMode(cartImpCtrlMode).setCartVelocity(20.0).breakWhen(normalForce));
		
		ThreadUtil.milliSleep(1500);
		Reg = getMultiRegister(READ_ROTATION_PATTERN, 5);
		if(Reg[1] == 0 || Reg[2] == 0){
			System.err.println("Muster nicht Vollständig im Bild");
			return false;
		}
		Alpha = (double)Reg[0];
		if(Alpha > 32768){
			Alpha -= 65536.0;
		}
		tool.getFrame("Greifer/SteinGreifer/Tastfinger").move(linRel(0,0,20.0,-Math.toRadians(Alpha),0,0).setCartVelocity(maxCartVelocity).setCartAcceleration(maxCartAcceleration).setCartJerk(maxCartJerk));
		lbr.move(linRel(0.0, 0.0, 200.0).setMode(cartImpCtrlMode).setCartVelocity(20.0).breakWhen(normalForce));
		ThreadUtil.milliSleep(1000);
		Reg = getMultiRegister(READ_X_PATTERN, 2);
		transTool = tool.getFrame("Greifer/SteinGreifer/Tastfinger").transformationFromWorld();
		transRefPoint = transTool;
		transRefPoint = transRefPoint.withTranslation(-Reg[0],-Reg[1],-5.0);
		transRefPoint = transRefPoint.withRotationRad(0,0,0);
		transformation = transTool.compose(transRefPoint);
		return true;
	}
	
	private void makeRequest(int ref, int count) {
		//3. Prepare the request 
	     request = new ReadMultipleRegistersRequest(ref, count);

	     //4. Prepare the transaction
	     trans = new ModbusTCPTransaction(connection);
	     trans.setRequest(request);
	     
	     try {
			trans.execute();
	     }catch(ModbusIOException e){
	    	 System.err.println("ModbusIOException = "+e.getMessage());
	    	 connection.close();
	    	 ThreadUtil.milliSleep(200);
	    	 establishConnexion();
	    	 makeRequest(ref, count);
	    	 return;
	     }catch(ModbusSlaveException e){
	    	 System.err.println("ModbusSlaveException = "+e.getMessage());
	    	 return;
	     }catch(ModbusException e){
	    	System.err.println("ModbusException = "+e.getMessage());
			return;
	     }catch(Exception e){
	    	 System.err.println("Exception = "+e.getMessage());
	    	 establishConnexion();
	    	 makeRequest(ref, count);
	    	 return;
	     }
	     response = (ReadMultipleRegistersResponse) trans.getResponse();
	}
	
	public Integer getCameraPosition(int ref){
		int rep;
		makeRequest(ref ,1);
		if(response == null){
			rep = 123;
		}else{
			rep = response.getRegisterValue(0);
		}
	     
	    return rep;
	}
	
	public int getOneRegister(int ref){
		makeRequest(ref, 1);
		return response.getRegisterValue(0);
	}
	
	public int[] getMultiRegister(int ref, int Length){
		int Register[] = new int[Length];
		makeRequest(ref, Length);
		for (int i = 0; i < Length; i++){
			Register[i] = response.getRegisterValue(i);
		}
		return Register;
	}
	
	public String getString(int ref, int Length){
		String rep;
		makeRequest(ref ,Length);
		/*if(res == null){
			rep = "Nothing";
		}else{
			rep = res.getHexMessage();
		}*/
		for(int i = 0; i < Length;i++){
			//getLogger().info("msg = "+res.getRegisterValue(i));
		}
		rep = "Nothing";
	     
	    return rep;
	}

	
	private void establishConnexion(){
		//1. Setup the parameters
		if(connection == null){
			try {
				//getLogger().info("Parameter Setzen");
				/*int idx = CameraDress.indexOf(':');
			    if(idx > 0) {
			    	CameraPort = Integer.parseInt(astr.substring(idx+1));	           
			        astr = astr.substring(0,idx);
			    }
			    address = InetAddress.getByName(astr);*/
			    //addr = InetAddress.getByName("172.31.1.5");
//			    getLogger().info("Adresse setzen "+addr);
			    connection = new TCPMasterConnection(address);
			}catch(Exception ex) {
//				getLogger().info("Fehler "+ex);
				//System.err.println("ex = "+ex.getMessage());
				establishConnexion();
				return;
			         //ex.printStackTrace();
			         //System.exit(1);
			}
		}
		if(!connection.isConnected()){
		//2. Open the connection
		//con = new TCPMasterConnection(addr);
//			getLogger().info("Verbindung nicht offen");
			connection.setPort(CameraPort);
			//System.out.println("Timeout vor ändernung = "+con.getTimeout());
			connection.setTimeout(250);
//			System.out.println("Timeout nach ändernung = "+con.getTimeout());
			try {
//				getLogger().info("Versuchen zu Öffnen..");
				connection.connect();
			}catch(ModbusIOException e){
//				getLogger().info("Fehler 2"+e);
			   	System.err.println("e = "+e.getMessage());
				establishConnexion();
			}catch (Exception e) {
//				getLogger().info("Fehler 3"+e);
				System.err.println("e = "+e.getMessage());
				
				
				//establishConnexion(astr);
			}
		}
	}
}
