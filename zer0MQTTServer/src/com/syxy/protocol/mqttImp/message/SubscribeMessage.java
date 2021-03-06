package com.syxy.protocol.mqttImp.message;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.syxy.protocol.mqttImp.QoS;
import com.syxy.protocol.mqttImp.Type;
import com.syxy.protocol.mqttImp.message.Message.HeaderMessage;
import com.syxy.server.ClientSession;
import com.syxy.util.BufferPool;
import com.syxy.util.StringTool;

public class SubscribeMessage extends Message {
	
	private List<String> topicFilter = new ArrayList<String>();
	private List<QoS> requestQos = new ArrayList<QoS>();
	
	public SubscribeMessage(){
		super(Type.SUBSCRIBE);
	}
	
	public SubscribeMessage(HeaderMessage headerMessage){
		super(headerMessage);
	}
	
	@Override
	public byte[] encode() throws IOException {
		throw new UnsupportedOperationException("SUBSCRIBE无需编码，该类型仅能从客户端发送服务端");
	}

	@Override
	public Message decode(ByteBuffer byteBuffer, int messageLength)
			throws IOException {
		InputStream in = new ByteArrayInputStream(byteBuffer.array());
		DataInputStream dataInputStream = new DataInputStream(in);
		
		SubscribeMessage subscribeMessage = new SubscribeMessage();
		subscribeMessage.setPackgeID(dataInputStream.readUnsignedShort());
		
		int pos = 2;//可变头为2字节，该变量用来计算是否还有需要读取的数据
		while (pos < messageLength) {
			subscribeMessage.getTopicFilter().add(dataInputStream.readUTF());
			pos += StringTool.stringToByte(subscribeMessage.getTopicFilter().get(subscribeMessage.getTopicFilter().size() - 1)).length;
			subscribeMessage.getRequestQos().add(QoS.valueOf(dataInputStream.read() & 0x03));//0x03=00000011
			pos++;
		}
	
		subscribeMessage.setHeaderMessage(this.getHeaderMessage());
		
		byteBuffer.position(messageLength);
		BufferPool.removeReadedData(byteBuffer);
		
		return subscribeMessage;
	}

	@Override
	public void handlerMessage(ClientSession client) {
		// TODO Auto-generated method stub

	}

	@Override
	public int messageLength(Message msg) {
		// TODO Auto-generated method stub
		int length = 2; // message id length
		for (String topicfilter : topicFilter) {
			length += StringTool.stringToByte(topicfilter).length;//topicFilter
			length += 1; // requestQoS
		}
		return length;
	}

	@Override
	public boolean isMessageIdRequired() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public List<String> getTopicFilter() {
		return topicFilter;
	}

	public void setTopicFilter(List<String> topicFilter) {
		this.topicFilter = topicFilter;
	}

	public List<QoS> getRequestQos() {
		return requestQos;
	}

	public void setRequestQos(List<QoS> requestQos) {
		this.requestQos = requestQos;
	}
	
}
