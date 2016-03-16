package distribully.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import distribully.model.DistribullyModel;

public class InviteThread extends Thread{
	private String address;
	private int port;
	private boolean started;
	DistribullyModel model;
	public InviteThread(String targetAddress, int targetPort, DistribullyModel model){
		address = targetAddress;
		port = targetPort;
		started = false;
		this.model = model;
		this.start();
	}
	
	public void setStarted(boolean started){
		this.started = started;
	}
	
	public void run() {
		Socket s = null;
		DataOutputStream out;
		DataInputStream in;
		try {
			s = new Socket(address, port);
			in = new DataInputStream(s.getInputStream());
			out = new DataOutputStream(s.getOutputStream());
			out.writeUTF(model.getNickname());
			String data = in.readUTF();
			System.out.println("From "+ address+": " + data);
			if(data.equals("Accepted")){ //TODO: gamestate check?
				//TODO: setStatus joined
				while(!started){} //TODO: timeout?
				out.writeUTF("QueueName");
				String ok = in.readUTF();
				if(!ok.equals("ok")){//TODO: moet dit
					//ALLES IS KAPOT!LL!L!LL!L!LL!L!!
				}
			}else{
				//TODO: setStatus rejected
			}
		} catch (UnknownHostException e) {
			System.err.println(e);
		} catch (ConnectException e){ //Receiver has no open socket
			//TODO: set status rejected
		} catch (IOException e) {
			System.err.println(e);
		} finally {
			if (s != null)
				try {
					s.close();
				} catch (IOException e) {
					System.err.println(e);
				}
		}
	}
}
