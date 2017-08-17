package redhawk.driver.port.impl;

import redhawk.driver.bulkio.Packet;
import redhawk.driver.port.PortListener;
import redhawk.driver.port.RedhawkPort;

public class GenericPortListener extends PortListener<Object[]>{
	protected Boolean receivedData = false;

	private Integer messagesReceived = 0;

	private RedhawkPort portToSendTo;

	private Boolean sendForward;
	
	private Packet<Object[]> packet; 

	public GenericPortListener() {
		sendForward = false;
	}

	public GenericPortListener(RedhawkPort port) {
		portToSendTo = port;
	}

	@Override
	public void onReceive(Packet<Object[]> packet) {
		messagesReceived++;
		receivedData = true;
		this.packet = packet;
		System.out.println("Received data " + messagesReceived);
	}

	public Boolean getReceivedData() {
		return receivedData;
	}

	public Integer getMessagesReceived() {
		return messagesReceived;
	}
	
	public Packet getPacket() {
		return this.packet;
	}
}
