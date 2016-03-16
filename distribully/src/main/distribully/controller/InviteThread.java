package distribully.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class InviteThread extends Thread{
	private String address;
	private int port;
	private boolean started;
	public InviteThread(String targetAddress, int targetPort){
		address = targetAddress;
		port = targetPort;
		started = false;
		this.start();
	}
	
	public void setStarted(boolean started){
		this.started = started;
	}
	
	public void run(String args[]) {
		Socket s = null;
		DataOutputStream out;
		DataInputStream in;
		try { //TODO: als geen connectie kan maken -> rejected
			s = new Socket(address, port);
			in = new DataInputStream(s.getInputStream());
			out = new DataOutputStream(s.getOutputStream());
			out.writeUTF("HostName"); //TODO: hostname
			String data = in.readUTF();
			System.out.println("From server: " + data);
			if(data.equals("yes")){ //TODO: gamestate check?
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
