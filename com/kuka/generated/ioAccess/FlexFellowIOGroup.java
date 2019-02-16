package com.kuka.generated.ioAccess;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.kuka.roboticsAPI.controllerModel.Controller;
import com.kuka.roboticsAPI.ioModel.AbstractIOGroup;
import com.kuka.roboticsAPI.ioModel.IOTypes;

/**
 * Automatically generated class to abstract I/O access to I/O group <b>FlexFellow</b>.<br>
 * <i>Please, do not modify!</i>
 * <p>
 * <b>I/O group description:</b><br>
 * ./.
 */
@Singleton
public class FlexFellowIOGroup extends AbstractIOGroup
{
	/**
	 * Constructor to create an instance of class 'FlexFellow'.<br>
	 * <i>This constructor is automatically generated. Please, do not modify!</i>
	 *
	 * @param controller
	 *            the controller, which has access to the I/O group 'FlexFellow'
	 */
	@Inject
	public FlexFellowIOGroup(Controller controller)
	{
		super(controller, "FlexFellow");

		addDigitalOutput("SignalLightBlue", IOTypes.BOOLEAN, 1);
		addDigitalOutput("SignalLightRed", IOTypes.BOOLEAN, 1);
		addDigitalOutput("SignalLightGreen", IOTypes.BOOLEAN, 1);
		addDigitalOutput("SignalLightYellow", IOTypes.BOOLEAN, 1);
		addDigitalOutput("SignalRinger", IOTypes.BOOLEAN, 1);
		addInput("DoorIsClosed", IOTypes.BOOLEAN, 1);
		addInput("FuseOfAirCondition", IOTypes.BOOLEAN, 1);
		addDigitalOutput("EnableMagnetSideLeftFront", IOTypes.BOOLEAN, 1);
		addDigitalOutput("EnableMagnetSideRightBack", IOTypes.BOOLEAN, 1);
		addDigitalOutput("EnableMagnetBackRight", IOTypes.BOOLEAN, 1);
		addDigitalOutput("EnableMagnetBackLeft", IOTypes.BOOLEAN, 1);
		addDigitalOutput("EnableMagnetSideLeftBack", IOTypes.BOOLEAN, 1);
		addDigitalOutput("OpenGripper", IOTypes.BOOLEAN, 1);
		addDigitalOutput("CloseGripper", IOTypes.BOOLEAN, 1);
		addInput("GripperIsOpen", IOTypes.BOOLEAN, 1);
		addInput("GripperIsClose", IOTypes.BOOLEAN, 1);
	}

	/**
	 * Gets the value of the <b>digital output '<i>SignalLightBlue</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital output 'SignalLightBlue'
	 */
	public boolean getSignalLightBlue()
	{
		return getBooleanIOValue("SignalLightBlue", true);
	}

	/**
	 * Sets the value of the <b>digital output '<i>SignalLightBlue</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @param value
	 *            the value, which has to be written to the digital output 'SignalLightBlue'
	 */
	public void setSignalLightBlue(java.lang.Boolean value)
	{
		setDigitalOutput("SignalLightBlue", value);
	}

	/**
	 * Gets the value of the <b>digital output '<i>SignalLightRed</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital output 'SignalLightRed'
	 */
	public boolean getSignalLightRed()
	{
		return getBooleanIOValue("SignalLightRed", true);
	}

	/**
	 * Sets the value of the <b>digital output '<i>SignalLightRed</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @param value
	 *            the value, which has to be written to the digital output 'SignalLightRed'
	 */
	public void setSignalLightRed(java.lang.Boolean value)
	{
		setDigitalOutput("SignalLightRed", value);
	}

	/**
	 * Gets the value of the <b>digital output '<i>SignalLightGreen</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital output 'SignalLightGreen'
	 */
	public boolean getSignalLightGreen()
	{
		return getBooleanIOValue("SignalLightGreen", true);
	}

	/**
	 * Sets the value of the <b>digital output '<i>SignalLightGreen</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @param value
	 *            the value, which has to be written to the digital output 'SignalLightGreen'
	 */
	public void setSignalLightGreen(java.lang.Boolean value)
	{
		setDigitalOutput("SignalLightGreen", value);
	}

	/**
	 * Gets the value of the <b>digital output '<i>SignalLightYellow</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital output 'SignalLightYellow'
	 */
	public boolean getSignalLightYellow()
	{
		return getBooleanIOValue("SignalLightYellow", true);
	}

	/**
	 * Sets the value of the <b>digital output '<i>SignalLightYellow</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @param value
	 *            the value, which has to be written to the digital output 'SignalLightYellow'
	 */
	public void setSignalLightYellow(java.lang.Boolean value)
	{
		setDigitalOutput("SignalLightYellow", value);
	}

	/**
	 * Gets the value of the <b>digital output '<i>SignalRinger</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital output 'SignalRinger'
	 */
	public boolean getSignalRinger()
	{
		return getBooleanIOValue("SignalRinger", true);
	}

	/**
	 * Sets the value of the <b>digital output '<i>SignalRinger</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @param value
	 *            the value, which has to be written to the digital output 'SignalRinger'
	 */
	public void setSignalRinger(java.lang.Boolean value)
	{
		setDigitalOutput("SignalRinger", value);
	}

	/**
	 * Gets the value of the <b>digital input '<i>DoorIsClosed</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital input
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital input 'DoorIsClosed'
	 */
	public boolean getDoorIsClosed()
	{
		return getBooleanIOValue("DoorIsClosed", false);
	}

	/**
	 * Gets the value of the <b>digital input '<i>FuseOfAirCondition</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital input
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital input 'FuseOfAirCondition'
	 */
	public boolean getFuseOfAirCondition()
	{
		return getBooleanIOValue("FuseOfAirCondition", false);
	}

	/**
	 * Gets the value of the <b>digital output '<i>EnableMagnetSideLeftFront</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital output 'EnableMagnetSideLeftFront'
	 */
	public boolean getEnableMagnetSideLeftFront()
	{
		return getBooleanIOValue("EnableMagnetSideLeftFront", true);
	}

	/**
	 * Sets the value of the <b>digital output '<i>EnableMagnetSideLeftFront</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @param value
	 *            the value, which has to be written to the digital output 'EnableMagnetSideLeftFront'
	 */
	public void setEnableMagnetSideLeftFront(java.lang.Boolean value)
	{
		setDigitalOutput("EnableMagnetSideLeftFront", value);
	}

	/**
	 * Gets the value of the <b>digital output '<i>EnableMagnetSideRightBack</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital output 'EnableMagnetSideRightBack'
	 */
	public boolean getEnableMagnetSideRightBack()
	{
		return getBooleanIOValue("EnableMagnetSideRightBack", true);
	}

	/**
	 * Sets the value of the <b>digital output '<i>EnableMagnetSideRightBack</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @param value
	 *            the value, which has to be written to the digital output 'EnableMagnetSideRightBack'
	 */
	public void setEnableMagnetSideRightBack(java.lang.Boolean value)
	{
		setDigitalOutput("EnableMagnetSideRightBack", value);
	}

	/**
	 * Gets the value of the <b>digital output '<i>EnableMagnetBackRight</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital output 'EnableMagnetBackRight'
	 */
	public boolean getEnableMagnetBackRight()
	{
		return getBooleanIOValue("EnableMagnetBackRight", true);
	}

	/**
	 * Sets the value of the <b>digital output '<i>EnableMagnetBackRight</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @param value
	 *            the value, which has to be written to the digital output 'EnableMagnetBackRight'
	 */
	public void setEnableMagnetBackRight(java.lang.Boolean value)
	{
		setDigitalOutput("EnableMagnetBackRight", value);
	}

	/**
	 * Gets the value of the <b>digital output '<i>EnableMagnetBackLeft</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital output 'EnableMagnetBackLeft'
	 */
	public boolean getEnableMagnetBackLeft()
	{
		return getBooleanIOValue("EnableMagnetBackLeft", true);
	}

	/**
	 * Sets the value of the <b>digital output '<i>EnableMagnetBackLeft</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @param value
	 *            the value, which has to be written to the digital output 'EnableMagnetBackLeft'
	 */
	public void setEnableMagnetBackLeft(java.lang.Boolean value)
	{
		setDigitalOutput("EnableMagnetBackLeft", value);
	}

	/**
	 * Gets the value of the <b>digital output '<i>EnableMagnetSideLeftBack</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital output 'EnableMagnetSideLeftBack'
	 */
	public boolean getEnableMagnetSideLeftBack()
	{
		return getBooleanIOValue("EnableMagnetSideLeftBack", true);
	}

	/**
	 * Sets the value of the <b>digital output '<i>EnableMagnetSideLeftBack</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @param value
	 *            the value, which has to be written to the digital output 'EnableMagnetSideLeftBack'
	 */
	public void setEnableMagnetSideLeftBack(java.lang.Boolean value)
	{
		setDigitalOutput("EnableMagnetSideLeftBack", value);
	}

	/**
	 * Gets the value of the <b>digital output '<i>OpenGripper</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital output 'OpenGripper'
	 */
	public boolean getOpenGripper()
	{
		return getBooleanIOValue("OpenGripper", true);
	}

	/**
	 * Sets the value of the <b>digital output '<i>OpenGripper</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @param value
	 *            the value, which has to be written to the digital output 'OpenGripper'
	 */
	public void setOpenGripper(java.lang.Boolean value)
	{
		setDigitalOutput("OpenGripper", value);
	}

	/**
	 * Gets the value of the <b>digital output '<i>CloseGripper</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital output 'CloseGripper'
	 */
	public boolean getCloseGripper()
	{
		return getBooleanIOValue("CloseGripper", true);
	}

	/**
	 * Sets the value of the <b>digital output '<i>CloseGripper</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @param value
	 *            the value, which has to be written to the digital output 'CloseGripper'
	 */
	public void setCloseGripper(java.lang.Boolean value)
	{
		setDigitalOutput("CloseGripper", value);
	}

	/**
	 * Gets the value of the <b>digital input '<i>GripperIsOpen</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital input
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital input 'GripperIsOpen'
	 */
	public boolean getGripperIsOpen()
	{
		return getBooleanIOValue("GripperIsOpen", false);
	}

	/**
	 * Gets the value of the <b>digital input '<i>GripperIsClose</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital input
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital input 'GripperIsClose'
	 */
	public boolean getGripperIsClose()
	{
		return getBooleanIOValue("GripperIsClose", false);
	}

}
