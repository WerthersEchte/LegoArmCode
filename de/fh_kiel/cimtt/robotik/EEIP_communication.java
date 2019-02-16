package de.fh_kiel.cimtt.robotik;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import de.re.eeip.cip.datatypes.ConnectionType;
import de.re.eeip.cip.datatypes.Priority;
import de.re.eeip.cip.datatypes.RealTimeFormat;
import de.re.eeip.cip.exception.CIPException;

public class EEIP_communication {
	static de.re.eeip.EEIPClient eeipClient = new de.re.eeip.EEIPClient();
	static byte[] Message = new byte[128];
	static byte[] Messagerec = new byte[128];
	static byte[] WriteData = new byte[128];
	static byte[] config = new byte[4];
	static int state=0,state2=0,k=0;
	static Timer wait1;
	
	
	
	static boolean status_write=false, status_read=false, connected=false, timer=false, timerflag1=false,timerflag2=false,timerflag3=false,timerflag4=false;
	static int Status_Byte,TO_Bit,TO_Bit_old,AE_Bit,AA_Bit;
	static byte[] digitalInputs=null,test=null;
	public EEIP_communication() {
		
	}

	
	public static byte[] read() throws IOException, InterruptedException {
		switch(state2) {
		case 0:
			
			if(timerflag1==false) {
				timerflag2=false;
				timerflag3=false;
				timer=false;
				timerflag1=true;
				Messagerec[0]=0;
			}
			if(timer==false) {
			// Inhalt vor Timer wait1 wird nur einmal ausgeführt	
			timer=true;
			
			
			// Find Ethernet Devices
//			java.util.List<CipIdentityItem> cipIdentityItem = eeipClient.ListIdentity();
//	        for (de.re.eeip.encapsulation.CipIdentityItem item : cipIdentityItem)
//	        {
//	            System.out.println("Ethernet/IP Device Found:");
//	            System.out.println(item.ProductName1);
//	            System.out.println("IP-Address: " + de.re.eeip.encapsulation.CipIdentityItem.getIPAddress(item.SocketAddress.SIN_Address));
//	            System.out.println("Port: " + item.SocketAddress.SIN_port);
//	            System.out.println("Vendor ID: " + item.VendorID1);
//	            System.out.println("Product-code: " + item.ProductCode1);
//	            System.out.println("Type-Code: " + item.ItemTypeCode);
//	            System.out.println("Serial Number: " + item.SerialNumber1);
//	            System.out.println();
//	        }
			
	        // Session öffnen
            eeipClient.RegisterSession("192.168.0.5",44818);

	      //******************************************************************************************			
// 			anderer Ansatz: unkommentiert wird hier nur ein connection Fehler ausgegeben	
//******************************************************************************************
			eeipClient.setO_T_InstanceID(101);       //Output Assembly 64hex
            eeipClient.setO_T_Length(128);
            eeipClient.setO_T_RealTimeFormat(RealTimeFormat.Header32Bit);
            eeipClient.setO_T_ownerRedundant(false);
            eeipClient.setO_T_priority(Priority.Scheduled);
            eeipClient.setO_T_variableLength(false);
            eeipClient.setO_T_connectionType(ConnectionType.Point_to_Point);
            eeipClient.setIpAddress("192.168.0.5");
            eeipClient.setTcpPort(44818);
			//eeipClient.setRequestedPacketRate_O_T(10);
			//eeipClient.setRequestedPacketRate_T_O(10);
			eeipClient.setT_O_InstanceID(100);       //Output Assembly 65hex
            eeipClient.setT_O_Length(128);
            eeipClient.setT_O_RealTimeFormat(RealTimeFormat.Modeless);
            eeipClient.setT_O_ownerRedundant(false);
            eeipClient.setT_O_priority(Priority.Scheduled);
            eeipClient.setT_O_variableLength(false);
            eeipClient.setT_O_connectionType(ConnectionType.Point_to_Point);

				eeipClient.ForwardOpen();

            Thread.sleep(1000);
    		// Eingangspuffer auslesen
//    		System.out.printf("Eingangs-Puffer: ");
//    		for(int i=0; i<Messagerec.length; i++)	System.out.printf("0x%02X ", eeipClient.T_O_IOData[i]);
//    		System.out.printf("\n\n");
//    		
//    		System.out.printf("Ausgangs-Puffer: ");
//    		for(int i=0; i<Message.length; i++)	System.out.printf("0x%02X ", eeipClient.O_T_IOData[i]);
//    		System.out.printf("\n\n");

//******************************************************************************************

			System.out.println("read case 0");		
			
			k=0;
			// Eingangspuffer auslesen
//			System.out.printf("Eingangs-Puffer: ");
//			for(int i=0; i<Messagerec.length; i++)	System.out.printf("0x%02X ", eeipClient.T_O_IOData[i]);
//			System.out.printf("\n\n");
			
			// TO-Bit Status abfragen ( damit man später auf togglen überprüfen kann )
			Status_Byte= eeipClient.T_O_IOData[0x7F];
			TO_Bit_old=(Status_Byte&(0x20))>>5;
			
//			for(int i=1;i<0x7F;i++) Message[i] = 0x00;
	//
//			Message[0x00]|=0x01;	// Bitleiste 1
//			Message[0x7F]|=0x01;	// Bitleiste 2 - Ausgangspuffer letztes Byte ist das 128te, also Index 127=0x7F
//			
//	        eeipClient.O_T_IOData[0x01] = (byte)Message[0x01];
//	        eeipClient.O_T_IOData[0x02] = (byte)Message[0x02];
//	        eeipClient.O_T_IOData[0x03] = (byte)Message[0x03];
//	        eeipClient.O_T_IOData[0x04] = (byte)Message[0x04];
//	        eeipClient.O_T_IOData[0x05] = (byte)Message[0x05];
//	        eeipClient.O_T_IOData[0x00] = (byte)Message[0x00];
//	        eeipClient.O_T_IOData[0x7F] = (byte)Message[0x7F];
//	        Thread.sleep(500);
	        		
			// Steuer-Message
			for(int i=1;i<0x7F;i++) Message[i] = 0x00;
			Message[0x01]=0x01;			// Befehlsbyte
			Message[0x02]=0x14;			// Startbyte-LOW
			Message[0x03]=0x00;			// Startbyte-HIGH
			Message[0x04]=0x1E;			// ByteAnzahl-LOW
			Message[0x05]=0x00;			// ByteAnzahl-HIGH
			Message[0x00]|=0x01;	// Bitleiste 1
			Message[0x7F]|=0x01;	// Bitleiste 2 - Ausgangspuffer letztes Byte ist das 128te, also Index 127=0x7F
			
	        eeipClient.O_T_IOData[0x01] = (byte)Message[0x01];
	        eeipClient.O_T_IOData[0x02] = (byte)Message[0x02];
	        eeipClient.O_T_IOData[0x03] = (byte)Message[0x03];
	        eeipClient.O_T_IOData[0x04] = (byte)Message[0x04];
	        eeipClient.O_T_IOData[0x05] = (byte)Message[0x05];
	        eeipClient.O_T_IOData[0x00] = (byte)Message[0x00];
	        eeipClient.O_T_IOData[0x7F] = (byte)Message[0x7F];
	        Thread.sleep(500);
			
			//Ausgangspuffer lesen (überprüfen)
//			System.out.printf("Ausgangs-Puffer: ");
//			for(int i=0; i<Message.length; i++)	System.out.printf("0x%02X ", eeipClient.O_T_IOData[i]);
//			System.out.printf("\n\n");
			
			// Timer für zyklisches Abfragen des Eingangspuffers
			wait1 = new Timer();
			wait1.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {
					
//					System.out.printf("Eingangs-Puffer: ");
//					for(int i=0; i<Messagerec.length; i++)	System.out.printf("0x%02X ", eeipClient.T_O_IOData[i]);
//					System.out.printf("\n\n");
//					
//					System.out.printf("Ausgangs-Puffer: ");
//					for(int i=0; i<Message.length; i++)	System.out.printf("0x%02X ", eeipClient.O_T_IOData[i]);
//					System.out.printf("\n\n");
//					System.out.println(TO_Bit_old);
//					System.out.printf("\n\n");
					Status_Byte=eeipClient.T_O_IOData[0x7F];
					TO_Bit=(Status_Byte&(0x20))>>5;
					AA_Bit=(Status_Byte&(0x02))>>1;
					AE_Bit=(Status_Byte&(0x04))>>2;
			
					if((TO_Bit_old!=TO_Bit)&&(AA_Bit==1)&&(AE_Bit==1)) {			
						state2=1;
						timerflag2=false;
						this.cancel();
					}
					
					
				}
			}, 0, 100);
			}
			break;
			
		case 1:
			
//			digitalInputs = eeipClient.getAssemblyObject().getInstance(100);
//			Messagerec[0x00]=digitalInputs[0];
//			Messagerec[0x01]=digitalInputs[1];
//			Messagerec[0x02]=digitalInputs[2];
//			Messagerec[0x03]=digitalInputs[3];
//			Messagerec[0x04]=digitalInputs[4];
//			Messagerec[0x05]=digitalInputs[5];
//			Messagerec[0x06]=digitalInputs[6];
//			Messagerec[0x07]=digitalInputs[7];
//			Messagerec[0x08]=digitalInputs[8];
//			Messagerec[0x09]=digitalInputs[9];
//			Messagerec[0x0A]=digitalInputs[10];
//			Messagerec[0x0B]=digitalInputs[11];
//			Messagerec[0x0C]=digitalInputs[12];
//			Messagerec[0x0D]=digitalInputs[13];
//			System.out.println(Messagerec);
//			Message[0x00] ^= (1<<6);
//			Message[0x0F] ^= (1<<6);
//			eeipClient.getAssemblyObject().setInstance(101, Message);
			if(timerflag2==false) {
				timer=false;
				timerflag2=true;
			}
			if(timer==false) {
				timer=true;
				System.out.println("read case 1");
				
				for(int i=1;i<31;i++)Messagerec[i]=eeipClient.T_O_IOData[i];

	            eeipClient.O_T_IOData[0x00] = (byte)0;
	            eeipClient.O_T_IOData[0x7F] = (byte)0;
	            Thread.sleep(500);
		
				
			wait1 = new Timer();
			wait1.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {
//					System.out.printf("Eingangs-Puffer: ");
//					for(int i=0; i<Messagerec.length; i++)	System.out.printf("0x%02X ", eeipClient.T_O_IOData[i]);
//					System.out.printf("\n\n");
//					
//					System.out.printf("Ausgangs-Puffer: ");
//					for(int i=0; i<Message.length; i++)	System.out.printf("0x%02X ", eeipClient.O_T_IOData[i]);
//					System.out.printf("\n\n");

					Status_Byte=eeipClient.T_O_IOData[0x7F];
					TO_Bit=(Status_Byte&(0x20))>>5;
					AA_Bit=(Status_Byte&(0x02))>>1;
					AE_Bit=(Status_Byte&(0x04))>>2;
			
					if((AA_Bit==0)&&(AE_Bit==0)) {
						state2=2;
						timerflag2=false;
						this.cancel();
					}
					
				}
			}, 0, 100);
			}
			break;
			
		case 2:
			
			if(timerflag3==false) {
				timer=false;
				timerflag3=true;
			}
			if(timer==false) {
				timer=true;
				System.out.println("read case 2");
				
				System.out.printf("Gelesene Daten: ");
				for(int i=1; i<31; i++)	System.out.printf("0x%02X ", Messagerec[i]);
				System.out.printf("\n\n");
				
			wait1 = new Timer();
			wait1.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {
//					System.out.printf("Eingangs-Puffer: ");
//					for(int i=0; i<Messagerec.length; i++)	System.out.printf("0x%02X ", eeipClient.T_O_IOData[i]);
//					System.out.printf("\n\n");
//					
//					System.out.printf("Ausgangs-Puffer: ");
//					for(int i=0; i<Message.length; i++)	System.out.printf("0x%02X ", eeipClient.O_T_IOData[i]);
//					System.out.printf("\n\n");
					
					k++;
					
					if(k>=3) {
						
							try {
								eeipClient.ForwardClose();
							}  catch (IOException e) {
								e.printStackTrace();
							}

						timerflag1=false;
						Messagerec[0]=1;
						state2=3;
						k=0;
						this.cancel();
					}
				}
			}, 0, 100);
			}
			break;
		case 3:
			state2=0;
			break;
		}
		
			
		return Messagerec;
}
	
	
	public static boolean write(byte[] WriteData) throws IOException, InterruptedException {
		
		switch(state) {
		
		case 0:
					if(timerflag1==false) {
						timerflag2=false;
						timerflag3=false;
						status_write=false;
			timer=false;
			timerflag1=true;
		}
		if(timer==false) {
		// Inhalt vor Timer wait1 wird nur einmal ausgeführt	
		timer=true;
		
		
		// Find Ethernet Devices
//		java.util.List<CipIdentityItem> cipIdentityItem = eeipClient.ListIdentity();
//        for (de.re.eeip.encapsulation.CipIdentityItem item : cipIdentityItem)
//        {
//            System.out.println("Ethernet/IP Device Found:");
//            System.out.println(item.ProductName1);
//            System.out.println("IP-Address: " + de.re.eeip.encapsulation.CipIdentityItem.getIPAddress(item.SocketAddress.SIN_Address));
//            System.out.println("Port: " + item.SocketAddress.SIN_port);
//            System.out.println("Vendor ID: " + item.VendorID1);
//            System.out.println("Product-code: " + item.ProductCode1);
//            System.out.println("Type-Code: " + item.ItemTypeCode);
//            System.out.println("Serial Number: " + item.SerialNumber1);
//            System.out.println();
//        }
		
        // Session öffnen
        eeipClient.RegisterSession("192.168.0.5",44818);
        
      //******************************************************************************************			
//			anderer Ansatz: unkommentiert wird hier nur ein connection Fehler ausgegeben	
//******************************************************************************************
		eeipClient.setO_T_InstanceID(101);       //Output Assembly 64hex
        eeipClient.setO_T_Length(128);
        eeipClient.setO_T_RealTimeFormat(RealTimeFormat.Header32Bit);
        eeipClient.setO_T_ownerRedundant(false);
        eeipClient.setO_T_priority(Priority.Scheduled);
        eeipClient.setO_T_variableLength(false);
        eeipClient.setO_T_connectionType(ConnectionType.Point_to_Point);
        eeipClient.setIpAddress("192.168.0.5");
        eeipClient.setTcpPort(44818);
		//eeipClient.setRequestedPacketRate_O_T(10);
		//eeipClient.setRequestedPacketRate_T_O(10);
		eeipClient.setT_O_InstanceID(100);       //Output Assembly 65hex
        eeipClient.setT_O_Length(128);
        eeipClient.setT_O_RealTimeFormat(RealTimeFormat.Modeless);
        eeipClient.setT_O_ownerRedundant(false);
        eeipClient.setT_O_priority(Priority.Scheduled);
        eeipClient.setT_O_variableLength(false);
        eeipClient.setT_O_connectionType(ConnectionType.Point_to_Point);
        

		eeipClient.ForwardOpen();

        Thread.sleep(500);
		// Eingangspuffer auslesen
//		System.out.printf("Eingangs-Puffer: ");
//		for(int i=0; i<Messagerec.length; i++)	System.out.printf("0x%02X ", eeipClient.T_O_IOData[i]);
//		System.out.printf("\n\n");
//		
//		System.out.printf("Ausgangs-Puffer: ");
//		for(int i=0; i<Message.length; i++)	System.out.printf("0x%02X ", eeipClient.O_T_IOData[i]);
//		System.out.printf("\n\n");

//******************************************************************************************

		System.out.println("write case 0");		
		k=0;
		// Eingangspuffer auslesen
//		System.out.printf("Eingangs-Puffer: ");
//		for(int i=0; i<Messagerec.length; i++)	System.out.printf("0x%02X ", eeipClient.T_O_IOData[i]);
//		System.out.printf("\n\n");
		
		// TO-Bit Status abfragen ( damit man später auf togglen überprüfen kann )
		Status_Byte= eeipClient.T_O_IOData[0x7F];
		TO_Bit_old=(Status_Byte&(0x20))>>5;
		
//		for(int i=1;i<0x7F;i++) Message[i] = 0x00;
//
//		Message[0x00]|=0x01;	// Bitleiste 1
//		Message[0x7F]|=0x01;	// Bitleiste 2 - Ausgangspuffer letztes Byte ist das 128te, also Index 127=0x7F
//		
//        eeipClient.O_T_IOData[0x01] = (byte)Message[0x01];
//        eeipClient.O_T_IOData[0x02] = (byte)Message[0x02];
//        eeipClient.O_T_IOData[0x03] = (byte)Message[0x03];
//        eeipClient.O_T_IOData[0x04] = (byte)Message[0x04];
//        eeipClient.O_T_IOData[0x05] = (byte)Message[0x05];
//        eeipClient.O_T_IOData[0x00] = (byte)Message[0x00];
//        eeipClient.O_T_IOData[0x7F] = (byte)Message[0x7F];
//        Thread.sleep(500);
        		
		// Steuer-Message
		for(int i=1;i<0x7F;i++) Message[i] = 0x00;
		Message[0x01]=0x02;			// Befehlsbyte
		Message[0x02]=0x14;			// Startbyte-LOW
		Message[0x03]=0x00;			// Startbyte-HIGH
		Message[0x04]=0x1E;			// ByteAnzahl-LOW
		Message[0x05]=0x00;			// ByteAnzahl-HIGH
		Message[0x00]|=0x01;	// Bitleiste 1
		Message[0x7F]|=0x01;	// Bitleiste 2 - Ausgangspuffer letztes Byte ist das 128te, also Index 127=0x7F
		
        eeipClient.O_T_IOData[0x01] = (byte)Message[0x01];
        eeipClient.O_T_IOData[0x02] = (byte)Message[0x02];
        eeipClient.O_T_IOData[0x03] = (byte)Message[0x03];
        eeipClient.O_T_IOData[0x04] = (byte)Message[0x04];
        eeipClient.O_T_IOData[0x05] = (byte)Message[0x05];
        eeipClient.O_T_IOData[0x00] = (byte)Message[0x00];
        eeipClient.O_T_IOData[0x7F] = (byte)Message[0x7F];
        Thread.sleep(500);
		
		//Ausgangspuffer lesen (überprüfen)
//		System.out.printf("Ausgangs-Puffer: ");
//		for(int i=0; i<Message.length; i++)	System.out.printf("0x%02X ", eeipClient.O_T_IOData[i]);
//		System.out.printf("\n\n");
		
		wait1 = new Timer();
		wait1.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
//				System.out.printf("Eingangs-Puffer: ");
//				for(int i=0; i<Messagerec.length; i++)	System.out.printf("0x%02X ", eeipClient.T_O_IOData[i]);
//				System.out.printf("\n\n");
//				
//				System.out.printf("Ausgangs-Puffer: ");
//				for(int i=0; i<Message.length; i++)	System.out.printf("0x%02X ", eeipClient.O_T_IOData[i]);
//				System.out.printf("\n\n");

				Status_Byte=eeipClient.T_O_IOData[0x7F];
				TO_Bit=(Status_Byte&(0x20))>>5;
				AA_Bit=(Status_Byte&(0x02))>>1;
		
				if((TO_Bit_old!=TO_Bit)&&(AA_Bit==1)) {
					TO_Bit_old=TO_Bit;
					state=1;
					timerflag2=false;
					this.cancel();
				}
				
			}
		}, 0, 100);
		}
		break;
		
		
		case 1:
			
			
			if(timerflag2==false) {
				timer=false;
				timerflag2=true;
			}
			if(timer==false) {
				timer=true;
				System.out.println("write case 1");	
				
				// Steuer-Message
				for(int i=0;i<30;i++)Message[i+1]=(byte)WriteData[i];
				Message[0x00]^= (1<<6);
				Message[0x7F]^= (1<<6);
				
				for(int i=0;i<0x80;i++)eeipClient.O_T_IOData[i] = (byte)Message[i];
				System.out.printf("Geschriebene Daten: ");
				for(int i=1; i<31; i++)	System.out.printf("0x%02X ", Message[i]);
				System.out.printf("\n\n");
				
		        Thread.sleep(500);

			wait1 = new Timer();
			wait1.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {
//					System.out.printf("Eingangs-Puffer: ");
//					for(int i=0; i<Messagerec.length; i++)	System.out.printf("0x%02X ", eeipClient.T_O_IOData[i]);
//					System.out.printf("\n\n");
//					
//					System.out.printf("Ausgangs-Puffer: ");
//					for(int i=0; i<Message.length; i++)	System.out.printf("0x%02X ", eeipClient.O_T_IOData[i]);
//					System.out.printf("\n\n");
					
					Status_Byte=eeipClient.T_O_IOData[0x7F];
					TO_Bit=(Status_Byte&(0x20))>>5;
					AE_Bit=(Status_Byte&(0x04))>>2;
					AA_Bit=(Status_Byte&(0x02))>>1;
					
					if(AE_Bit==1) {
						state=2;
						timerflag3=false;
						
						this.cancel();
					}
					
				}
			}, 0, 100);
			}
			break;
			
		case 2:
			
			
			if(timerflag3==false) {
				timer=false;
				timerflag3=true;
			}
			if(timer==false) {
				timer=true;
			System.out.println("write case 2");
			
			Message[0x00]&= ~(0x01);
			Message[0x7F]&= ~(0x01);
	        eeipClient.O_T_IOData[0x00] = (byte)Message[0x00];
	        eeipClient.O_T_IOData[0x7F] = (byte)Message[0x7F];
			
			wait1 = new Timer();
			wait1.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {
//					System.out.printf("Eingangs-Puffer: ");
//					for(int i=0; i<Messagerec.length; i++)	System.out.printf("0x%02X ", eeipClient.T_O_IOData[i]);
//					System.out.printf("\n\n");
//					
//					System.out.printf("Ausgangs-Puffer: ");
//					for(int i=0; i<Message.length; i++)	System.out.printf("0x%02X ", eeipClient.O_T_IOData[i]);
//					System.out.printf("\n\n");
					k++;
					
					if(k>=10) {
						
							try {
								eeipClient.ForwardClose();
							} catch (IOException e) {
								e.printStackTrace();
							}
	
						timerflag1=false;
						status_write=true;
						state=3;
						k=0;
						this.cancel();
					}
					
				}
			}, 0, 100);
			}
		break;
		case 3:
			state=0;
			break;
		}
		
		return status_write;
	}
}
