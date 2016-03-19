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
	DistribullyModel model;
	public InviteThread(String targetAddress, int targetPort, DistribullyModel model){
		address = targetAddress;
		port = targetPort;
		this.model = model;
		this.start();
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
