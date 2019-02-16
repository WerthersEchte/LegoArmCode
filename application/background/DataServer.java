package application.background;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class DataServer{
	
	public void sendData( Data aData ){
		try{
			sendDatagramm(aData.toString());
		}catch( Exception vAll ){
			vAll.printStackTrace();
		}
	}

    protected boolean mSocketInitialized = false;
	private static int MAX_DATAGRAM_LENGTH = 65507;
	protected DatagramPacket mDataPaket;
	protected DatagramSocket mToServerSocket = null;
	
	public DataServer( InetAddress aReceiverAddress, int aReceiverPort ) throws IOException
	{		

		initialiseDatagram( aReceiverAddress, aReceiverPort );
		
		mToServerSocket = new DatagramSocket();
		mToServerSocket.setBroadcast(true);
		
		mSocketInitialized = true;
		
	}

    private void initialiseDatagram( InetAddress aReceiverAddress, int aReceiverPort ) {

		try {
			mDataPaket = new DatagramPacket( new byte[MAX_DATAGRAM_LENGTH], MAX_DATAGRAM_LENGTH );
			mDataPaket.setAddress( aReceiverAddress );
			mDataPaket.setPort( aReceiverPort );
		} catch ( Exception e) {
			e.printStackTrace();
		}
		
    }

	private void sendDatagramm( String aData ) throws IOException
	{
		
		mDataPaket.setData( aData.getBytes() );
		mToServerSocket.send( mDataPaket );

	}

	private void closeConnection()
	{
		if ( mToServerSocket != null ) {
		    mToServerSocket.disconnect();
			mToServerSocket.close();
			mToServerSocket = null;
		}
		
		mSocketInitialized = false;
		
	}
    
    public boolean isConnected() {
        
        return mSocketInitialized;
        
    }
    
    public String toString(){
        if ( mToServerSocket != null ) {
            
            return mToServerSocket.getLocalAddress().toString() + ":" + mToServerSocket.getLocalPort() + " zu " + mToServerSocket.getInetAddress().toString() + ":" + mToServerSocket.getPort();
            
        } else {
            
            return "Es besteht keine Serververbindung";
            
        }
        
    }

}
