package de.fh_kiel.cimtt.robotik;

import java.nio.ByteBuffer;

import javax.swing.text.StyledEditorKit.BoldAction;

import com.kuka.roboticsAPI.geometricModel.math.XyzAbcTransformation;
import com.sun.swing.internal.plaf.basic.resources.basic;
import com.sun.xml.internal.ws.client.Stub;
import com.sun.xml.internal.ws.util.ByteArrayBuffer;

import de.fh_kiel.cimtt.robotik.RFID.Box;
import de.fh_kiel.cimtt.robotik.RFID.WORKPIEC;

public class Werkstueck {
	public String SAP;
	public int aSAP;
	public String Werk;
	public int aWerk;
	public String Lager;
	public int aLager;
	public RFID readerUnit;
	public byte X_Pos;
	public byte Y_Pos;
	public byte Z_Pos;
	public int aXYZ_pos;
	public int Masse;
	public int aMasse;
	public short Anzahl;
	public int aAnzahl;
	public int Platzbedarf;
	public int aPlatzbedarf;
	public short Status;
	public int aStatus;
	public int Zeitstempel;
	public int aZeitstempel;
	public int Auftragsnummer;
	public int aAuftragsnummer;
	public WORKPIEC stueck;
	public int Index;
	byte[] temp = new byte[128];
	private static final int STD_TIMEOUT = 10;
	
	
	public Werkstueck(WORKPIEC piec, int index, RFID rfid){
		stueck = piec;
		Index = index; 
		readerUnit = rfid;
	}
	
	public boolean writeAll(){
		byte[] tmp = new byte[stueck.SIZE];
		byte[] ding;
		ding = this.SAP.getBytes();
		int n = 0;
		int position = 0;
		for(int i = 0; i < stueck.SAP_SIZE - stueck.CRC; i++){
			if(i < ding.length)
				tmp[n] = ding[i];
			else
				tmp[n] = 0;
			n++;
		}
		tmp[n] = readerUnit.getCRC8(tmp, position, stueck.SAP_SIZE - stueck.CRC);
		n++;
		position = n;
		
		ding = Werk.getBytes();
		for(int i = 0; i < stueck.WERKSKENNUNG_SIZE - stueck.CRC; i++){
			tmp[n] = ding[i];
			n++;
		}
		tmp[n] = readerUnit.getCRC8(tmp, position, stueck.WERKSKENNUNG_SIZE - stueck.CRC);
		n++;
		position = n;
		
		ding = this.Lager.getBytes();
		for(int i = 0; i < stueck.LAGERPLATZ_SIZE - stueck.CRC; i++){
			tmp[n] = ding[i];
			n++;
		}
		tmp[n] = readerUnit.getCRC8(tmp, position, stueck.LAGERPLATZ_SIZE - stueck.CRC);
		n++;
		position = n;
		
		tmp[n] = X_Pos;
		n++;
		tmp[n] = Y_Pos;
		n++;
		tmp[n] = Z_Pos;
		n++;
		tmp[n] = readerUnit.getCRC8(tmp, position, stueck.X_Y_Z_POS_SIZE - stueck.CRC);
		n++;
		position = n;
		
		ding = ByteBuffer.allocate(4).putInt(this.Masse).array();
		for(int i= 0; i< 4; i++){
			tmp[n] = ding[i];
			n++;
		}
		tmp[n] = readerUnit.getCRC8(tmp, position, stueck.MASSE_SIZE - stueck.CRC);
		n++;
		position = n;
		
		ding = ByteBuffer.allocate(2).putShort(this.Status).array();
		for(int i= 0; i< 2; i++){
			tmp[n] = ding[i];
			n++;
		}
		tmp[n] = readerUnit.getCRC8(tmp, position, stueck.STATUS_SIZE - stueck.CRC);
		n++;
		position = n;
		
		ding = ByteBuffer.allocate(2).putShort(this.Anzahl).array();
		for(int i= 0; i< 2; i++){
			tmp[n] = ding[i];
			n++;
		}
		tmp[n] = readerUnit.getCRC8(tmp, position, stueck.ANZAHL_SIZE - stueck.CRC);
		n++;
		position = n;
		
		
		ding = ByteBuffer.allocate(4).putInt(this.Platzbedarf).array();
		for(int i= 0; i< 4; i++){
			tmp[n] = ding[i];
			n++;
		}
		tmp[n] = readerUnit.getCRC8(tmp, position, stueck.PLATZBEDARF_SIZE - stueck.CRC);
		n++;
		position = n;
		
		
		ding = ByteBuffer.allocate(4).putInt(this.Zeitstempel).array();
		for(int i= 0; i< 4; i++){
			tmp[n] = ding[i];
			n++;
		}
		tmp[n] = readerUnit.getCRC8(tmp, position, stueck.ZEITSTEMPEL_SIZE - stueck.CRC);
		n++;
		position = n;
		
		ding = ByteBuffer.allocate(4).putInt(this.Auftragsnummer).array();
		for(int i= 0; i< 4; i++){
			tmp[n] = ding[i];
			n++;
		}
		tmp[n] = readerUnit.getCRC8(tmp, position, stueck.AUFTRAGSNUMMER_SIZE - stueck.CRC);
		n++;
		position = n;
		
		return readerUnit.Write(stueck.SAP, stueck.SIZE, tmp, STD_TIMEOUT, Index);
	}
/*
	public static boolean EraseAll(Kiste box){
		for(int i = 0; i < box.Inhalt.size(); i++){
			box.Inhalt.get(box.Inhalt.size() - 1).Erase(box.Inhalt.get(box.Inhalt.size() - 1), box);
		}
		return true;
	}

	public boolean Erase(Kiste box){
		byte[] tmp = new byte[stueck.SIZE];
		for(int i = 0; i < tmp.length; i++){
			tmp[i] = 0;
		}
		if(readerUnit.Write(stueck.SAP, stueck.SIZE, tmp, STD_TIMEOUT, Index)){
			box.setAktiv(Index, false);
			box.Inhalt.remove(box.Inhalt.indexOf(this));
			return true;
		}
		return false;
	}
	public static boolean Erase(Werkstueck wp, Kiste box){
		byte[] tmp = new byte[wp.stueck.SIZE];
		for(int i = 0; i < tmp.length; i++){
			tmp[i] = 0;
		}
		if(wp.readerUnit.Write(wp.stueck.SAP, wp.stueck.SIZE, tmp, STD_TIMEOUT, wp.Index)){
			box.setAktiv(wp.Index, false);
			box.Inhalt.remove(box.Inhalt.indexOf(wp));
		}
		return false;
	}*/
	
	public boolean setAnzahl(int anzahl){
		return setAnzahl((short) anzahl);
	}
	
	public boolean setAnzahl(short anzahl){
		if(anzahl < 0)
			return false;
		this.Anzahl = anzahl;
		return setAnzahl();
	}
	
	public boolean setAnzahl(){
		byte[] tmp = ByteBuffer.allocate(2).putShort(this.Anzahl).array();
		for(int i = 0; i < stueck.ANZAHL_SIZE - stueck.CRC; i++)
			temp[i] = tmp[i];
		temp[stueck.ANZAHL_SIZE - stueck.CRC] = readerUnit.getCRC8(temp, 0, stueck.ANZAHL_SIZE - stueck.CRC);
		return readerUnit.Write(stueck.ANZAHL, stueck.ANZAHL_SIZE, temp, STD_TIMEOUT, Index);
	}
	
	public boolean setAuftragsnummer(int auftragsnummer){
		if(auftragsnummer > 0){
			this.Auftragsnummer = auftragsnummer;
			return setAuftragsnummer();
		}
		return false;
	}
	
	public boolean setAuftragsnummer(){
		byte[] tmp = ByteBuffer.allocate(stueck.AUFTRAGSNUMMER_SIZE - stueck.CRC).putInt(this.Auftragsnummer).array();
		for(int i = 0; i < stueck.AUFTRAGSNUMMER_SIZE - stueck.CRC; i++)
			temp[i] = tmp[i];
		temp[stueck.AUFTRAGSNUMMER_SIZE - stueck.CRC] = readerUnit.getCRC8(temp, 0, stueck.AUFTRAGSNUMMER_SIZE - stueck.CRC);
		return readerUnit.Write(stueck.AUFTRAGSNUMMER, stueck.AUFTRAGSNUMMER_SIZE, temp, STD_TIMEOUT, Index);
	}
	
	public boolean setZeitstempel(int zeitstempel){
		if(zeitstempel > 0){
			this.Zeitstempel = zeitstempel;
			return setZeitstempel();
		}
		return false;
	}
	public boolean setZeitstempel(){
		byte[] tmp = ByteBuffer.allocate(stueck.ZEITSTEMPEL_SIZE - stueck.CRC).putInt(this.Zeitstempel).array();
		for(int i = 0; i < stueck.ZEITSTEMPEL_SIZE - stueck.CRC; i++)
			temp[i] = tmp[i];
		temp[stueck.ZEITSTEMPEL_SIZE - stueck.CRC] = readerUnit.getCRC8(temp, 0, stueck.ZEITSTEMPEL_SIZE - stueck.CRC);
		return readerUnit.Write(stueck.ZEITSTEMPEL, stueck.ZEITSTEMPEL_SIZE, temp, STD_TIMEOUT, Index);
	}
	
	public boolean setStatus(int status){
		return setStatus((short)status);
	}
	
	public boolean setStatus(short status){
		this.Status = status;
		return setStatus();
	}
	
	public boolean setStatus(){
		byte[] tmp = ByteBuffer.allocate(stueck.STATUS_SIZE - stueck.CRC).putShort(this.Status).array();
		for(int i = 0; i < stueck.STATUS_SIZE - stueck.CRC; i++)
			temp[i] = tmp[i];
		temp[stueck.STATUS_SIZE - stueck.CRC] = readerUnit.getCRC8(temp, 0, stueck.STATUS_SIZE - stueck.CRC);
		return readerUnit.Write(stueck.STATUS, stueck.STATUS_SIZE, temp, STD_TIMEOUT, Index);
	}
	
	public boolean setPlatzbedarf(int platzbedarf){
		this.Platzbedarf = platzbedarf;
		return setPlatzbedarf();
	}
	
	public boolean setPlatzbedarf(){
		byte[] tmp = new byte[stueck.PLATZBEDARF_SIZE];
		tmp = ByteBuffer.allocate(stueck.PLATZBEDARF_SIZE - stueck.CRC).putInt(this.Platzbedarf).array();
		for(int i = 0; i < stueck.PLATZBEDARF_SIZE - stueck.CRC; i++)
			temp[i] = tmp[i];
		temp[stueck.PLATZBEDARF_SIZE - stueck.CRC] = readerUnit.getCRC8(temp, 0, stueck.LAGERPLATZ_SIZE - stueck.CRC);
		return readerUnit.Write(stueck.PLATZBEDARF, stueck.PLATZBEDARF_SIZE, temp, STD_TIMEOUT, Index);
	}
	
	
	public boolean setMasse(int masse){
		if(masse < 0)
			return false;
		this.Masse = masse;
		return setMasse();
	}
	
	public boolean setMasse(){
		byte[] tmp = ByteBuffer.allocate(4).putInt(this.Masse).array();
		for(int i = 0; i < stueck.MASSE_SIZE - stueck.CRC; i++){
			temp[i] = tmp[i];
		}
		temp[stueck.MASSE_SIZE - stueck.CRC] = readerUnit.getCRC8(temp, 0, stueck.MASSE_SIZE - stueck.CRC);
		return readerUnit.Write(stueck.MASSE, stueck.MASSE_SIZE, temp, STD_TIMEOUT, Index);
	}
	
	public boolean setXYZ(int xPos, int yPos, int zPos){
		return setXYZ((byte)xPos, (byte)yPos, (byte)zPos);
	}
	
	public boolean setXYZ(byte xPos, byte yPos, byte zPos){
		if(xPos >= 0)
			this.X_Pos = xPos;
		if(yPos >= 0)
			this.Y_Pos = yPos;
		if(zPos >= 0)
			this.Z_Pos = zPos;
		return setXYZ();
	}
	
	public boolean setXYZ(){
		byte[] tmp = new byte[stueck.X_Y_Z_POS_SIZE];
		tmp[0] = this.X_Pos;
		tmp[1] = this.Y_Pos;
		tmp[2] = this.Z_Pos;
		tmp[stueck.X_Y_Z_POS_SIZE - stueck.CRC] = readerUnit.getCRC8(tmp, 0, stueck.X_Y_Z_POS_SIZE - stueck.CRC);
		return readerUnit.Write(stueck.X_Y_Z_POS, stueck.X_Y_Z_POS_SIZE, tmp, STD_TIMEOUT, Index);		
	}
	
	public boolean setLager(String lager){
		if(lager.length() > 4){
			return false;
		}
		this.Lager = lager;
		return setLager();
	}
	
	public boolean setLager(){
		byte[] tmp = new byte[stueck.LAGERPLATZ_SIZE];
		tmp = this.Lager.getBytes();
		for(int i = 0; i < stueck.LAGERPLATZ_SIZE - stueck.CRC; i++)
			temp[i] = tmp[i];
		temp[stueck.LAGERPLATZ_SIZE - stueck.CRC] = readerUnit.getCRC8(temp, 0, stueck.LAGERPLATZ_SIZE - stueck.CRC);
		return readerUnit.Write(stueck.LAGERPLATZ, stueck.LAGERPLATZ_SIZE, temp, STD_TIMEOUT, Index);
	}
	
	public boolean setWerk(String werk){
		if(werk.length() > 4){
			return false;
		}
		this.Werk = werk;
		return setWerk();
	}
	
	public boolean setWerk(){
		byte[] tmp = new byte[stueck.WERKSKENNUNG_SIZE];
		tmp = this.Werk.getBytes();
		for(int i = 0; i < stueck.WERKSKENNUNG_SIZE - stueck.CRC; i++)
			temp[i] = tmp[i];
		temp[stueck.WERKSKENNUNG_SIZE - stueck.CRC] = readerUnit.getCRC8(temp, 0, stueck.WERKSKENNUNG_SIZE - stueck.CRC);
		return readerUnit.Write(stueck.WERKSKENNUNG, stueck.WERKSKENNUNG_SIZE, temp, STD_TIMEOUT, Index);
	}
	
	
	public boolean setSAP(String sap){
		if(sap.length() > 20){
			return false;
		}
		this.SAP = sap;
		return setSAP();
	}
	
	public boolean setSAP(){
		byte[] tmp = new byte[stueck.SAP_SIZE];
		for(int i = 0; i < stueck.SAP_SIZE - stueck.CRC; i++){
			if(i < SAP.length())
				temp[i] = tmp[i];
			else
				temp[i] = 0;
		}
		temp[stueck.SAP_SIZE - stueck.CRC] = readerUnit.getCRC8(temp, 0, stueck.SAP_SIZE - stueck.CRC);
		return readerUnit.Write(stueck.SAP, stueck.SAP_SIZE, temp, STD_TIMEOUT, Index);
	}
	
	
	public boolean getAll(){
		byte[] tmp = readerUnit.ReadByte(stueck.SAP, stueck.SIZE, STD_TIMEOUT, Index);
		if(temp != null){
			getSAP(tmp, stueck.SAP - stueck.SAP);
			getWerk(tmp, stueck.WERKSKENNUNG - stueck.SAP);
			getLager(tmp, stueck.LAGERPLATZ - stueck.SAP);
			getXYZ(tmp, stueck.X_Y_Z_POS - stueck.SAP);
			getMasse(tmp, stueck.MASSE - stueck.SAP);
			getPlatzbedarf(tmp, stueck.PLATZBEDARF - stueck.SAP);
			getStatus(tmp, stueck.STATUS - stueck.SAP);
			getAnzahl(tmp, stueck.ANZAHL - stueck.SAP);
			getZeitstempel(tmp, stueck.ZEITSTEMPEL - stueck.SAP);
			getAuftragsnummer(tmp, stueck.AUFTRAGSNUMMER - stueck.SAP);
			return true;
		}
		return false;
	}
	
	public boolean getAuftragsnummer(){
		byte[] tmp = readerUnit.ReadByte(stueck.AUFTRAGSNUMMER, stueck.AUFTRAGSNUMMER_SIZE, STD_TIMEOUT, Index);
		return getAuftragsnummer(tmp);
	}
	
	public boolean getAuftragsnummer(byte[] bytes){
		return getAuftragsnummer(bytes, 0);
	}
	
	public boolean getAuftragsnummer(byte[] bytes, int offset){
		if(bytes != null){
			if(readerUnit.check8(bytes, offset, stueck.AUFTRAGSNUMMER_SIZE - stueck.CRC)){
				this.Auftragsnummer = ByteBuffer.wrap(bytes, offset, 4).getInt();
				return true;
			}
		}
		System.err.println("Auftragsnummer "+Index+" konnte nicht gelesen werden");
		return false;
	}
	
	public boolean getZeitstempel(){
		byte[] tmp = readerUnit.ReadByte(stueck.ZEITSTEMPEL, stueck.ZEITSTEMPEL_SIZE, STD_TIMEOUT, Index);
		return getZeitstempel(tmp);
	}
	
	public boolean getZeitstempel(byte[] bytes){
		return getZeitstempel(bytes, 0);
	}
	
	public boolean getZeitstempel(byte[] bytes, int offset){
		if(bytes != null){
			if(readerUnit.check8(bytes, offset, stueck.ZEITSTEMPEL_SIZE - stueck.CRC)){
				this.Zeitstempel = ByteBuffer.wrap(bytes, offset, 4).getInt();
				return true;
			}
		}
		System.err.println("Zeitstempel "+Index+" konnte nicht gelesen werden");
		return false;
	}
	
	public boolean getAnzahl(){
		byte[] tmp = readerUnit.ReadByte(stueck.ANZAHL, stueck.ANZAHL_SIZE, STD_TIMEOUT, Index);
		return getAnzahl(tmp);
	}
	
	public boolean getAnzahl(byte[] bytes){
		return getAnzahl(bytes, 0);
	}
	
	public boolean getAnzahl(byte[] bytes, int offset){
		if(bytes != null){
			if(readerUnit.check8(bytes, offset, stueck.ANZAHL_SIZE - stueck.CRC)){
				this.Anzahl = ByteBuffer.wrap(bytes, offset, 2).getShort();
				return true;
			}
		}
		System.err.println("Anzahl "+Index+" konnte nicht gelesen werden");
		return false;
	}
	
	public boolean getStatus(){
		byte[] tmp = readerUnit.ReadByte(stueck.STATUS, stueck.STATUS_SIZE, STD_TIMEOUT, Index);
		return getStatus(tmp);
	}
	
	public boolean getStatus(byte[] bytes){
		return getStatus(bytes, 0);
	}
	
	public boolean getStatus(byte[] bytes, int offset){
		if(bytes != null){
			if(readerUnit.check8(bytes, offset, stueck.STATUS_SIZE - stueck.CRC)){
				this.Status = ByteBuffer.wrap(bytes, offset, 2).getShort();
				return true;
			}
		}
		System.err.println("Status "+Index+" konnte nicht gelesen werden");
		return false;
	}
	
	public boolean getPlatzbedarf(){
		byte[] tmp = readerUnit.ReadByte(stueck.PLATZBEDARF, stueck.PLATZBEDARF_SIZE, STD_TIMEOUT, Index);
		return getPlatzbedarf(tmp);
	}
	
	public boolean getPlatzbedarf(byte[] bytes){
		return getPlatzbedarf(bytes, 0);
	}
	
	public boolean getPlatzbedarf(byte[] bytes, int offset){
		if(bytes != null){
			if(readerUnit.check8(bytes, offset, stueck.PLATZBEDARF_SIZE - stueck.CRC)){
				this.Platzbedarf =ByteBuffer.wrap(bytes, offset, 4).getInt();
				return true;
			}
		}
		System.err.println("Platzbedarf "+Index+" konnte nicht gelesen werden");
		return false;
	}
	
	public boolean getMasse(){
		byte[] tmp = readerUnit.ReadByte(stueck.MASSE, stueck.MASSE_SIZE, STD_TIMEOUT, Index);
		return getMasse(tmp);
	}
	
	public boolean getMasse(byte[] bytes){
		return getMasse(bytes, 0);
	}
	
	public boolean getMasse(byte[] bytes, int offset){
		if(bytes != null){
			if(readerUnit.check8(bytes, offset, stueck.MASSE_SIZE - stueck.CRC)){
				this.Masse = ByteBuffer.wrap(bytes, offset, 4).getInt();
				return true;
			}
		}
		System.err.println("Masse "+Index+" konnte nicht gelesen werden");
		return false;
	}
	
	public boolean getXYZ(){
		byte[] tmp = readerUnit.ReadByte(stueck.X_Y_Z_POS, stueck.X_Y_Z_POS_SIZE, STD_TIMEOUT, Index);
		return getXYZ(tmp);
	}
	
	public boolean getXYZ(byte[] bytes){
		return getXYZ(bytes, 0);
	}
	
	public boolean getXYZ(byte[] bytes, int offset){
		if(bytes != null){
			if(readerUnit.check8(bytes, offset, stueck.X_Y_Z_POS_SIZE - stueck.CRC)){
				this.X_Pos = bytes[0 + offset];
				this.Y_Pos = bytes[1 + offset];
				this.Z_Pos = bytes[2 + offset];
				return true;
			}
		}
		System.err.println("Platzbedarf "+Index+" konnte nicht gelesen werden");
		return false;
	}
	
	public boolean getLager(){
		byte[] tmp = readerUnit.ReadByte(stueck.LAGERPLATZ, stueck.LAGERPLATZ_SIZE, STD_TIMEOUT, Index);
		return getLager(tmp);
	}
	
	public boolean getLager(byte[] bytes){
		return getLager(bytes, 0);
	}
	
	public boolean getLager(byte[] bytes, int offset){
		if(bytes != null){
			byte[] tmp = new byte[stueck.LAGERPLATZ_SIZE];
			for(int i = 0; i < stueck.LAGERPLATZ_SIZE; i++){
				tmp[i] = bytes[i + offset];
			}
			if(readerUnit.check8(tmp, 0, stueck.LAGERPLATZ_SIZE - stueck.CRC)){
				this.Lager = tmp.toString();
				return true;
			}
		}
		System.err.println("Lagerplatz "+Index+" konnte nicht gelesen werden");
		return false;
	}
	
	public boolean getWerk(){
		byte[] tmp = readerUnit.ReadByte(stueck.WERKSKENNUNG, stueck.WERKSKENNUNG_SIZE, STD_TIMEOUT, Index);
		return getWerk(tmp);
	}
	
	public boolean getWerk(byte[] bytes){
		return getWerk(bytes, 0);
	}
	
	public boolean getWerk(byte[] bytes, int offset){
		if(bytes != null){
			byte[] tmp = new byte[stueck.WERKSKENNUNG_SIZE];
			for(int i = 0; i < stueck.WERKSKENNUNG_SIZE; i++){
				tmp[i] = bytes[i + offset];
			}
			if(readerUnit.check8(tmp, 0, stueck.WERKSKENNUNG_SIZE - stueck.CRC)){
				this.Werk = tmp.toString();
				return true;
			}
		}
		System.err.println("Werkskennung "+Index+" konnte nicht gelesen werden");
		return false;
	}
	
	
	public boolean getSAP(){
		byte[] tmp = readerUnit.ReadByte(stueck.SAP, stueck.SAP_SIZE, STD_TIMEOUT, Index);
		return getSAP(tmp);
	}
	
	public boolean getSAP(byte[] bytes){
		return getSAP(bytes, 0);
	}
	
	public boolean getSAP(byte[] bytes, int offset){
		if(bytes != null){
			byte[] tmp = new byte[stueck.SAP_SIZE];
			for(int i = 0; i < stueck.SAP_SIZE; i++){
				tmp[i] = bytes[i + offset];
			}
			if(readerUnit.check8(tmp, 0, stueck.SAP_SIZE - stueck.CRC)){
				this.SAP = tmp.toString();
				return true;
			}
		}
		System.err.println("SAP-Nummer "+Index+" konnte nicht gelesen werden");
		return false;
	}
	
}
