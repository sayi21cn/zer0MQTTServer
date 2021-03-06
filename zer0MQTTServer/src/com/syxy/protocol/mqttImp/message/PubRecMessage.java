package com.syxy.protocol.mqttImp.message;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.syxy.protocol.mqttImp.Type;
import com.syxy.protocol.mqttImp.message.Message.HeaderMessage;
import com.syxy.server.ClientSession;

public class PubRecMessage extends Message {

	private static int PUBREC_SIZE = 2;
	
	public PubRecMessage(){
		super(Type.PUBREC);
	}
	
	public PubRecMessage(int packageID){
		super(Type.PUBREC);
		this.setPackgeID(packageID);
	}
	
	public PubRecMessage(HeaderMessage headerMessage){
		super(headerMessage);
	}
	
	@Override
	public byte[] encode() throws IOException {
		return this.encodePackageID();
	}

	@Override
	public Message decode(ByteBuffer byteBuffer, int messageLength)
			throws IOException {
		PubRecMessage pubRecMessage = new PubRecMessage();
		pubRecMessage.setPackgeID(this.decodePackageID(byteBuffer));
		pubRecMessage.setHeaderMessage(this.getHeaderMessage());
		
		return pubRecMessage;
	}

	@Override
	public void handlerMessage(ClientSession client) {
		// TODO Auto-generated method stub

	}

	@Override
	public int messageLength(Message msg) {
		// TODO Auto-generated method stub
		return PUBREC_SIZE;
	}

	@Override
	public boolean isMessageIdRequired() {
		// TODO Auto-generated method stub
		return true;
	}
}
