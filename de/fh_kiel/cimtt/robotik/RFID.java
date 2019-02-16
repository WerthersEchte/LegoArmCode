package de.fh_kiel.cimtt.robotik;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.text.StyledEditorKit.BoldAction;

import org.omg.CORBA.portable.IDLEntity;

import com.sun.jndi.cosnaming.IiopUrl.Address;

import de.re.eeip.cip.datatypes.Priority;
import de.re.eeip.cip.datatypes.RealTimeFormat;

public class RFID {
	final String IPADRESS = "172.31.1.52";
	final int TCPPORT = 44818;
	final int UDPPORT = 44818;
	final int REFRESH_TIME = 100;
	final int BUFFERSIZE = 128;
	final static int CRC_WERKSTUECK = 1;
	final static int CRC_KISTE = 1;
	final static int INTEGER32 = 4;
	final static int INTEGER16 = 2;
	final static int INTEGER8 = 1;
	final static int BIT32 = 4;
	final static int BIT16 = 2;
	final static int MAX_SIZE = 30;
	
	public enum Box{
		STANDARTBOX();
		
		public final int SPEICHERLAYOUT;
		public final int SPEICHERLAYOUT_SIZE;
		public final int GESAMTMASSE;
		public final int GESAMTMASSE_SIZE;
		public final int ANZAHL;
		public final int ANZHAL_SIZE;
		public final int STATUS;
		public final int STATUS_SIZE;
		public final int GESAMTPLATZ;
		public final int GESAMTPLATZ_SIZE;
		public final int WERK;
		public final int WERK_SIZE;
		public final int LAGERPLATZ;
		public final int LAGERPLATZ_SIZE;
		public final int ZEITSTEMPEL;
		public final int ZEITSTEMPEL_SIZE;
		public final int AKTIV;
		public final int AKTIV_SIZE;
		public final int FARBE;
		public final int FARBE_SIZE;
		public final int SERIALNUMBER;
		public final int SERIALNUMBER_SIZE;
		public final int SIZE;
		public final int CRC;
		
		public final WORKPIEC[] PIECES = new WORKPIEC[30];
		
		Box(){
			this.SPEICHERLAYOUT 		= 0x00;
			this.SPEICHERLAYOUT_SIZE 	= INTEGER16 + INTEGER16 + CRC_KISTE;
			this.GESAMTMASSE 			= this.SPEICHERLAYOUT + this.SPEICHERLAYOUT_SIZE;
			this.GESAMTMASSE_SIZE 		= INTEGER32 + CRC_KISTE;
			this.ANZAHL 				= this.GESAMTMASSE + this.GESAMTMASSE_SIZE;
			this.ANZHAL_SIZE 			= INTEGER16 + CRC_KISTE;
			this.STATUS					= this.ANZAHL + this.ANZHAL_SIZE;
			this.STATUS_SIZE			= BIT16 + CRC_KISTE;
			this.GESAMTPLATZ			= this.STATUS + this.STATUS_SIZE;
			this.GESAMTPLATZ_SIZE		= BIT32 + CRC_KISTE;
			this.WERK					= this.GESAMTPLATZ + this.GESAMTPLATZ_SIZE;
			this.WERK_SIZE				= BIT32 + CRC_KISTE;
			this.LAGERPLATZ				= this.WERK + this.WERK_SIZE;
			this.LAGERPLATZ_SIZE		= BIT32 + CRC_KISTE;
			this.ZEITSTEMPEL			= this.LAGERPLATZ + this.LAGERPLATZ_SIZE;
			this.ZEITSTEMPEL_SIZE		= INTEGER32 + INTEGER8 + CRC_KISTE;
			this.AKTIV					= this.ZEITSTEMPEL + this.ZEITSTEMPEL_SIZE;
			this.AKTIV_SIZE				= BIT32 + CRC_KISTE;
			this.FARBE					= this.AKTIV + this.AKTIV_SIZE;
			this.FARBE_SIZE				= BIT16 + CRC_KISTE;
			this.SERIALNUMBER			= this.FARBE + this. FARBE_SIZE;
			this.SERIALNUMBER_SIZE		= INTEGER16 + CRC_KISTE;
			this.SIZE					= 80;
			this.CRC					= CRC_KISTE;
		}
	}
	
	public enum WORKPIEC{	
		WORKPIEC(Box.STANDARTBOX);
		
		
		public int SAP;
		public final int SAP_SIZE;
		public final int WERKSKENNUNG;
		public final int WERKSKENNUNG_SIZE;
		public final int LAGERPLATZ;
		public final int LAGERPLATZ_SIZE;
		public final int X_Y_Z_POS;
		public final int X_Y_Z_POS_SIZE;
		public final int MASSE;
		public final int MASSE_SIZE;
		public final int ANZAHL;
		public final int ANZAHL_SIZE;
		public final int PLATZBEDARF;
		public final int PLATZBEDARF_SIZE;
		public final int STATUS;
		public final int STATUS_SIZE;
		public final int ZEITSTEMPEL;
		public final int ZEITSTEMPEL_SIZE;
		public final int AUFTRAGSNUMMER;
		public final int AUFTRAGSNUMMER_SIZE;
		public final int SIZE;
		public final int CRC;
		
		WORKPIEC(Box box){
			this.SAP				= 0 + box.SIZE;
			this.SAP_SIZE			= 18 + CRC_WERKSTUECK;
			this.WERKSKENNUNG		= this.SAP + this.SAP_SIZE;
			this.WERKSKENNUNG_SIZE	= BIT32 + CRC_WERKSTUECK;
			this.LAGERPLATZ			= this.WERKSKENNUNG + this.WERKSKENNUNG_SIZE;
			this.LAGERPLATZ_SIZE	= BIT32 + CRC_WERKSTUECK;
			this.X_Y_Z_POS			= this.LAGERPLATZ + this.LAGERPLATZ_SIZE;
			this.X_Y_Z_POS_SIZE		= INTEGER8 + INTEGER8 + INTEGER8 + CRC_WERKSTUECK;
			this.MASSE				= this.X_Y_Z_POS + this.X_Y_Z_POS_SIZE;
			this.MASSE_SIZE			= INTEGER32 + CRC_WERKSTUECK;
			this.STATUS				= this.MASSE + this.MASSE_SIZE;
			this.STATUS_SIZE		= BIT16 + CRC_WERKSTUECK;
			this.ANZAHL				= this.STATUS + this.STATUS_SIZE;
			this.ANZAHL_SIZE		= INTEGER16 + CRC_WERKSTUECK;
			this.PLATZBEDARF		= this.ANZAHL + this.ANZAHL_SIZE;
			this.PLATZBEDARF_SIZE	= BIT32 + CRC_WERKSTUECK;
			this.ZEITSTEMPEL		= this.PLATZBEDARF + this.PLATZBEDARF_SIZE;
			this.ZEITSTEMPEL_SIZE	= INTEGER32 + CRC_WERKSTUECK;
			this.AUFTRAGSNUMMER 	= this.ZEITSTEMPEL + this.ZEITSTEMPEL_SIZE;
			this.AUFTRAGSNUMMER_SIZE= BIT32 + CRC_WERKSTUECK;
			this.SIZE				= 64;
			this.CRC				= CRC_KISTE;
			
			box.PIECES[0] 				= this;			
		}		
	}
	

	private static final int poly = 0x0D5;
	
	//	BB		HF		TO		MT		AF		AE		AA		CP
	//	0x80	0x40	0x20	0x10	0x08	0x04	0x02	0x01
	private final int BB = 0x80;
	private final int HF = 0x40;
	private final int TO = 0x20;
	private final int MT = 0x10;
	private final int AF = 0x08;
	private final int AE = 0x04;
	private final int AA = 0x02;
	private final int CP = 0x01;
	
	//			TI		KA						GR				AV
	//			0x40	0x20					0x04			0x01
	private final int TI = 0x40;
	private final int KA = 0x20;
	private final int GR = 0x04;
	private final int AV = 0x01;
	
	private final int COMMAND_READ 		= 0x01;
	private final int COMMAND_WRITE 	= 0x02;
	
	private byte CurrentState;
	private Timer TransmitTimer = new Timer();
	private int state = 0;
	private long timeout = 0;
	private boolean TO_OLD = false;
	private boolean signal = false;
	private byte[] buffer = new byte[128];
	private byte[] bufferIn = new byte[128];
	private int lengthOfWrite = 0;
	private int lengthOfRead = 0;
	
	
	
	private final int STATE_IDLE 		= 0x01;
	private final int STATE_WAIT		= 0x02;
	private final int STATE_READ 		= 0x04;
	private final int STATE_READ_READY	= 0x08;
	private final int STATE_WAIT_WRITE	= 0x10;
	private final int STATE_WRITE		= 0x20;
	private final int STATE_FIRSTBOOT	= 0x40;
	private final int STATE_RESET		= 0x80;
	
	private de.re.eeip.EEIPClient eeipClient = new de.re.eeip.EEIPClient();
	public RFID(){
		opentPort(IPADRESS, TCPPORT);
	}	
	public RFID(String ipAddress, int tcpPort){
		opentPort(ipAddress, tcpPort);
	}
	
	public byte getCRC8(byte[] bytes, int offset, int length){
		int crc = 0;
		for(int i = 0; i < length; i++){
			crc ^= bytes[i + offset];
			for (int j = 0; j < 8; j++) {
				if ((crc & 0x80) != 0) {
					crc = ((crc << 1) ^ poly);
				} else {
					crc <<= 1;
				}
			}
			crc &= 0xFF;
		}
		return (byte)(crc & 0xFF);
	}
	
	public boolean check8(byte[] bytes, int offset, int length){
		if(getCRC8(bytes, offset, length) == bytes[offset + length]){
			return true;
		}
		System.err.println("CRC-Fehler");
		return false;
	}
	
	private void opentPort(String ipAddress, int tcpPort){
		try {
			eeipClient.RegisterSession(ipAddress,tcpPort);
			eeipClient.setO_T_InstanceID(101);       //Output Assembly 64hex
	        eeipClient.setO_T_Length(BUFFERSIZE);
	        eeipClient.setO_T_RealTimeFormat(RealTimeFormat.Header32Bit);
	        eeipClient.setO_T_ownerRedundant(false);
	        eeipClient.setO_T_priority(Priority.Scheduled);
	        eeipClient.setO_T_variableLength(false);
	        eeipClient.setO_T_connectionType(de.re.eeip.cip.datatypes.ConnectionType.Point_to_Point);
	        eeipClient.setIpAddress(ipAddress);
	        eeipClient.setTcpPort(tcpPort);
			//eeipClient.setRequestedPacketRate_O_T(10);
			//eeipClient.setRequestedPacketRate_T_O(10);
			eeipClient.setT_O_InstanceID(100);       //Output Assembly 65hex
	        eeipClient.setT_O_Length(BUFFERSIZE);
	        eeipClient.setT_O_RealTimeFormat(RealTimeFormat.Modeless);
	        eeipClient.setT_O_ownerRedundant(false);
	        eeipClient.setT_O_priority(Priority.Scheduled);
	        eeipClient.setT_O_variableLength(false);
	        eeipClient.setT_O_connectionType(de.re.eeip.cip.datatypes.ConnectionType.Point_to_Point);
			eeipClient.ForwardOpen();
			CurrentState = 0;
			System.out.println("Denken Sie drann, am Ende des Programs die Close-Methode aufzurufen");
			state = STATE_FIRSTBOOT;
		} catch (IOException e) {
			// TODO Automatisch generierter Erfassungsblock
			e.printStackTrace();
		}
		
		
		TransmitTimer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				ReadCurrentState();
					
				if(timeout > 0){
					timeout--;
				}				
				switch(state){
					case STATE_IDLE:
						TO_OLD = getTO();
						signal = false;
					break;
					case STATE_FIRSTBOOT:
						if(isReady()){
							setReset();
						}
					break;
					case STATE_RESET:
						System.out.println("Init RFID...");
						eeipClient.O_T_IOData[0] |= GR;
						eeipClient.O_T_IOData[BUFFERSIZE - 1] |= GR;
						while(isReady());
						eeipClient.O_T_IOData[0] &= ~GR;
						eeipClient.O_T_IOData[BUFFERSIZE - 1] &= ~GR;						
						while(!isReady());
						System.out.println("RFID Ready!");
						TO_OLD=getTO();
						signal = false;
						state = STATE_IDLE;
					break;
					//	Daten sollen AUsgelesen werden
					case STATE_WAIT:
						if(!isJobStarted() && !isJobEnd()){
							state = STATE_IDLE;
							signal = false;
						}
					break;
					case STATE_READ:
						if(isJobEnd()){
							for(int i = 0; i < lengthOfRead; i++){
								buffer[i] = (byte)eeipClient.T_O_IOData[i+1];
							}
							setJob(false);
							state = STATE_WAIT;							
						}else if(isJobError()){
							String ErrMsg = "Read Error: ";
							switch(eeipClient.T_O_IOData[1]){
							case 0x02:
								ErrMsg += "Not possible to read the data carrier. ";
								break;
							case 0x04:
								ErrMsg +="Cannot write to the data carrier. ";
								break;
							case 0x05:
								ErrMsg += "Data carrier was removed from the R/W head's range during writing.";
								break;
							case 0x07:
								ErrMsg += "No or invalid command designator with set AV bit or the number of bytes is 00hex";
								break;
							case 0x09:
								ErrMsg += "R/W head cable break or no R/W head connected";
								break;
							case 0x0D:
								ErrMsg += "Communication to the R/W head disrupted.";
								break;
							case 0x0E:
								ErrMsg += "CRC for the read data and CRC for the data carrier do not agree. ";
								break;
							case 0x0F:
								ErrMsg +="1st and 2nd bit string are unequal. The 2nd bit string must be used. ";
								break;
							case 0x20:
								ErrMsg +="Address assignment of the read/write job is outside the memory range of the data carrier.";
								break;
							case 0x21:
								ErrMsg += "This function is not possible for this data carrier.";
								break;
							default:
								ErrMsg +="unknown";
								break;
							}
							System.err.println(ErrMsg);
							//System.err.printf("Read: Error 0x%02X", eeipClient.T_O_IOData[1]);
							
							setJob(false);
							state = STATE_WAIT;	
						}
					break;
					case STATE_WRITE:
						if(isJobStarted() && !signal){
							for(int i = 0; i < lengthOfWrite; i++){
								eeipClient.O_T_IOData[i+1] = buffer[i];
							}
							ToggleTI();
							signal = true;
						}else if(isJobEnd()){
							setJob(false);
							state = STATE_WAIT;
						}else if(isJobError()){
							String ErrMsg = "Write Error: ";
							switch(eeipClient.T_O_IOData[1]){
							case 0x02:
								ErrMsg += "Not possible to read the data carrier. ";
								break;
							case 0x04:
								ErrMsg +="Cannot write to the data carrier. ";
								break;
							case 0x05:
								ErrMsg += "Data carrier was removed from the R/W head's range during writing.";
								break;
							case 0x07:
								ErrMsg += "No or invalid command designator with set AV bit or the number of bytes is 00hex";
								break;
							case 0x09:
								ErrMsg += "R/W head cable break or no R/W head connected";
								break;
							case 0x0D:
								ErrMsg += "Communication to the R/W head disrupted.";
								break;
							case 0x0E:
								ErrMsg += "CRC for the read data and CRC for the data carrier do not agree. ";
								break;
							case 0x0F:
								ErrMsg +="1st and 2nd bit string are unequal. The 2nd bit string must be used. ";
								break;
							case 0x20:
								ErrMsg +="Address assignment of the read/write job is outside the memory range of the data carrier.";
								break;
							case 0x21:
								ErrMsg += "This function is not possible for this data carrier.";
								break;
							default:
								ErrMsg +="unknown";
								break;
							}
							System.err.println(ErrMsg);
							setJob(false);
							state = STATE_WAIT;	
						}
					break;
					default:
					break;
				}
			}
		}, 500, REFRESH_TIME);
	}
	
	
	public void WaitTillDone(){
		while(state != STATE_IDLE);
	}
	
	public boolean Write(int Address, byte Byte){
		return Write(Address,Byte, -1);
	}
	
	public boolean Write(int Address, byte Byte, int Timeout){
		byte[] temp = new byte[1];
		temp[0] = Byte;
		return Write(Address, 1, temp, Timeout);
	}
	/**
	 * Write a number of Bytes and wait to finish (!NO TIMEOUT!)
	 * @param Startaddress		Address of the first Byte
	 * @param NoOfBytes			number of Bytes	
	 * @param Buffer			Bytes to write
	 * @return					True if successful else false
	 */
	public boolean Write(int Startaddress, int NoOfBytes, byte[] Buffer){
		return Write(Startaddress, NoOfBytes, Buffer, -1);
	}
	
	/**
	 * Write a number of Bytes
	 * @param Startaddress		Address of the first Byte
	 * @param NoOfBytes			number of Bytes	
	 * @param Buffer			Bytes to write
	 * @param Timeout			Timeout in seconds for the hole operation
	 * @return					True if successful else false
	 */
	public boolean Write(int Startaddress, int NoOfBytes, byte[] Buffer, int Timeout, int index){
		return Write(Startaddress + index * WORKPIEC.WORKPIEC.SIZE, NoOfBytes, Buffer, Timeout);
	}
	
	public boolean Write(int Startaddress, int NoOfBytes, byte[] Buffer, int Timeout){
		this.timeout = Timeout * 1000 / REFRESH_TIME;
		while(this.timeout != 0 && state != STATE_IDLE);
		while(this.timeout != 0 && !isTagPresent());
		if(isTagPresent() && this.timeout != 0){
			this.lengthOfWrite = NoOfBytes;
			for(int i = 1; i < BUFFERSIZE - 2; i++){
				eeipClient.O_T_IOData[i] = 0x00;
			}
			for(int i = 0; i < Buffer.length; i++)
				this.buffer[i] = Buffer[i];
			byte[] adressbyte = ByteBuffer.allocate(Integer.SIZE/8).putInt(Startaddress).array();
			
			TO_OLD=getTO();
			eeipClient.O_T_IOData[0x01] = COMMAND_WRITE;
			eeipClient.O_T_IOData[0x02] = adressbyte[3];
			eeipClient.O_T_IOData[0x03] = adressbyte[2];
			eeipClient.O_T_IOData[0x04] = (byte) NoOfBytes;
			eeipClient.O_T_IOData[0x05] = 0x00;
			
			//System.out.format("0x%x 0x%x 0x%x 0x%x", adressbyte[0],adressbyte[1], adressbyte[2],adressbyte[3]);
		
			setJob(true);
			this.state = STATE_WRITE;
			while(this.timeout != 0 && (this.state == STATE_WRITE|| this.state == STATE_WAIT));
			if(this.timeout != 0){
				return true;
			}
		}else{
			System.err.println("Write: No Tag Present");
			return false;
		}		
		System.err.println("Write: Timeout");
		return false;
	}
	
	/**
	 * Read Bytes
	 * 
	 * @param Startaddress		Address of first byte
	 * @param NoOfBytes			number of Bytes
	 * @param Timeout			Timeout in seconds for the hole operation
	 * @return					Array of Bytes
	 */
	private byte[] Read(int Startaddress, int NoOfBytes, int Timeout){
		this.timeout = Timeout * 1000;
		this.timeout /= REFRESH_TIME;
		while(this.timeout != 0 && state != STATE_IDLE);
		while(this.timeout != 0 && !isTagPresent());
		if(isTagPresent() && this.timeout!= 0){
			this.lengthOfRead = NoOfBytes;
			for(int i = 1; i < BUFFERSIZE - 2; i++){
				eeipClient.O_T_IOData[i] = 0x00;
			}
			TO_OLD=getTO();
			byte[] adressbyte = ByteBuffer.allocate(Integer.SIZE/8).putInt(Startaddress).array();
			eeipClient.O_T_IOData[0x01] = COMMAND_READ;
			eeipClient.O_T_IOData[0x02] = adressbyte[3];
			eeipClient.O_T_IOData[0x03] = adressbyte[2];
			eeipClient.O_T_IOData[0x04] = (byte) NoOfBytes;
			eeipClient.O_T_IOData[0x05] = 0x00;
				
			
			setJob(true);
			this.state = STATE_READ;
			while(this.timeout != 0 && this.state == STATE_READ);
			if(this.timeout != 0){
				return this.buffer;
			}
		}else{
			System.err.println("Read: No Tag Present");
			return null;
		}		
		System.err.println("Read: Timeout");
		return null;
	}
	
	public byte[] ReadByte(int Address, int count, int Timeout, int index){
		return ReadByte(Address + WORKPIEC.WORKPIEC.SIZE * index, count, Timeout);
	}
	
	public byte[] ReadByte(int Address, int count, int Timeout){
		byte[] temp = Read(Address, count, Timeout);
		return temp;
	}
	
	public byte ReadByte(int Address, int Timeout){
		byte[] temp = new byte[1];
		temp = Read(Address, 1, Timeout);
		return temp[0];
	}
	
	public byte ReadByte(int Address){
		return ReadByte(Address, -1);
	}

	
	/**
	 * Print the current Status of the RW-Head
	 */
	public void getCurrentState(){
		String temp;
		temp = "Ready = "+isReady();
		temp +="\nHeadError = "+isError();
		temp +="\nToogleBit = "+getTO();
		temp +="\nMultiTag = "+isMultipleTag();
		temp +="\nJobError = "+isJobError();
		temp +="\nJobEnd = "+isJobEnd();
		temp +="\nJobStart = "+isJobStarted();
		temp +="\nTag Present = "+isTagPresent();
		System.out.println(temp);
	}
	
	private void ReadCurrentState(){
		CurrentState = eeipClient.T_O_IOData[0x7F];
	}
	
	private boolean ToggleTI(){
		if(getTI()){
			eeipClient.O_T_IOData[0x00] &= ~TI;
			eeipClient.O_T_IOData[BUFFERSIZE - 1] &= ~TI;
			return false;
		}
		eeipClient.O_T_IOData[0x00] |= TI;
		eeipClient.O_T_IOData[BUFFERSIZE - 1] |= TI;
		return true;
	}
	
	private void resetTI(){
		eeipClient.O_T_IOData[0x00] &= ~TI;
		eeipClient.O_T_IOData[BUFFERSIZE - 1] &= ~TI;	
	}
	private boolean getTI(){
		if((eeipClient.O_T_IOData[0] & TI)>0){
			return true;
		}
		return false;
	}
	
	private void setHeadON_OFF(boolean On_Off){
		if(On_Off == true){
			eeipClient.O_T_IOData[0] |= KA;
			eeipClient.O_T_IOData[BUFFERSIZE - 1] |= KA;
		}else{
			eeipClient.O_T_IOData[0] &= ~KA;
			eeipClient.O_T_IOData[BUFFERSIZE - 1] &= ~KA;
		}
	}
	
	private void setReset(){
		timeout = 50;
		state = STATE_RESET;
	}
	
	private void setJob(boolean Job){
		if(Job){
			eeipClient.O_T_IOData[0] |= AV;
			eeipClient.O_T_IOData[BUFFERSIZE - 1] |= AV;
		}else{
			eeipClient.O_T_IOData[0] &= ~AV;
			eeipClient.O_T_IOData[BUFFERSIZE - 1] &= ~AV;
		}
	}
	
	private boolean isReady(){
		ReadCurrentState();
		if((CurrentState & BB) > 0){
			return true;
		}
		return false;
	}
	
	private boolean isError(){
		ReadCurrentState();
		if((CurrentState & HF) > 0){
			return true;
		}
		return false;
	}
	
	private boolean getTO(){
		ReadCurrentState();
		if((CurrentState & TO) > 0){
			return true;
		}
		return false;
	}
	
	private boolean isMultipleTag(){
		ReadCurrentState();
		if((CurrentState & MT) > 0){
			return true;
		}
		return false;
	}
	
	private boolean isJobError(){
		ReadCurrentState();
		if((CurrentState & AF) > 0){
			return true;
		}
		return false;
	}
	
	private boolean isJobEnd(){
		ReadCurrentState();
		if((CurrentState & AE) > 0){
			return true;
		}
		return false;
	}
	
	private boolean isJobStarted(){
		ReadCurrentState();
		if((CurrentState & AA) > 0){
			return true;
		}
		return false;
	}
	
	private boolean isTagPresent(){
		ReadCurrentState();
		if((CurrentState & CP) > 0){
			return true;
		}
		return false;
	}
	
	public void Show(){
		String temp;
		temp = "ToogleBit = "+(eeipClient.O_T_IOData[0] & TI);
		temp +="\nHeadOnOff = "+(eeipClient.O_T_IOData[0] & KA);
		temp +="\nBaseState = "+(eeipClient.O_T_IOData[0] & GR);
		temp +="\nJob = "+(eeipClient.O_T_IOData[0] & AV);
		System.out.println(temp);
	}
	
	public byte[] ReadString(int Startaddress, int count, int Timeout){
		byte[] bytes = Read(Startaddress, count, Timeout);
		if(bytes == null){
			return null;
		}
		return bytes; 
	}
	
	public byte[] ReadString(int Startaddress, int count){
		return ReadString(Startaddress, count, -1);
	}
	
	/**
	 * Read an Integer from the Startaddress !without Timeout!
	 * @param Startaddress	(RFID.INTEGER_01 ... RFID.INTEGER_20)
	 * @return				Integer
	 */
	public int ReadInt(int Startaddress){
		return ReadInt(Startaddress, -1);
	}
	
	/**
	 * Read an Integer from the Startaddress !without Timeout!
	 * @param Startaddress	(RFID.INTEGER_01 ... RFID.INTEGER_20)
	 * @param Timeout		timeout in seconds
	 * @return				Integer
	 */
	public int ReadInt(int Startaddress, int Timeout){
		byte[] bytes = Read(Startaddress, Integer.SIZE/8, Timeout);
		if(bytes == null){
			return -1;
		}
		return ByteBuffer.wrap(bytes).getInt();
	}
	
	public double ReadDouble(int Startaddress){
		return ReadDouble(Startaddress, -1);
	}
	
	public double ReadDouble(int Startaddress, int Timeout){
		byte[] bytes = Read(Startaddress, Double.SIZE/8, Timeout);
		if(bytes == null){
			return -1;
		}
		return ByteBuffer.wrap(bytes).getDouble();
	}
	
	
	public boolean Write(int Startaddress, int Number){
		return Write(Startaddress, Number, -1);	
	}
	
	public boolean Write(int Startaddress, int Number, int Timeout){
		byte[] bytes = ByteBuffer.allocate(Integer.SIZE/8).putInt(Number).array();
		return Write(Startaddress, bytes.length, bytes, Timeout);
	}
	
	public boolean Write(int Startaddress, double Number){
		return Write(Startaddress, Number, -1);
	}
	
	public boolean Write(int Startaddress, double Number, int Timeout){
		byte[] bytes = ByteBuffer.allocate(Double.SIZE / 8).putDouble(Number).array();
		return Write(Startaddress, bytes.length, bytes, Timeout);
	}
	
	public void close(){
		try {
			eeipClient.ForwardClose();
			eeipClient.UnRegisterSession();
		} catch (IOException e) {
			// TODO Automatisch generierter Erfassungsblock
			e.printStackTrace();
		}
		TransmitTimer.cancel();
	}
	
	
}
