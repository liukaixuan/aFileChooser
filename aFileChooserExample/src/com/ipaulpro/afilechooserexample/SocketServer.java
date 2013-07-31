package com.ipaulpro.afilechooserexample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;

import android.util.Log;

/**
 * 
 * 
 * 
 * @author liukaixuan@baidu.com
 */
public class SocketServer {
	
	public static final int port = 1177 ;
	
	private boolean running = true ;
	
	ServerSocket ss ;
	
	public void shutdown(){
		running = false ;
		if(ss != null){
			synchronized (ss) {
				ss.notifyAll() ;
			}
		}
	}
	
	public void openListener() throws Exception{
		ss = ServerSocketFactory.getDefault().createServerSocket(port) ;
		ss.setSoTimeout(5000) ;
		
		new Thread(){

			@Override
			public void run() {
				while(running){
					Socket s;
					try {
						s = ss.accept();
						
						if(s != null){
							getRequset(s) ;
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
		}.start() ;
		
	}
	
	private void getRequset(Socket client) {  
	    try {  
	        InputStream in = client.getInputStream();  
	        InputStreamReader reader = new InputStreamReader(in);  
	        BufferedReader bd = new BufferedReader(reader);//ÈýÖØ·â×°  
	        String t = null;  
	  
	        while ((t = bd.readLine()) != null) {  
	        	String fileLine = t ;
	        	Log.e("test_ss", "server recv:" + t) ;
	        	
	        	break ;
	        }
	        
	        OutputStream os = client.getOutputStream() ;
	        StringBuilder sb = new StringBuilder() ;
	        String lineFeed = "\n\r" ;
	        
	        sb.append("HTTP/1.0 200 OK").append(lineFeed)
	          .append("Server: Sisters' Network").append(lineFeed) ;
	        
	        String body = "hello world~~" ;
	        
	        sb.append("Content-Length:").append(body.length()).append(lineFeed) ;
	        sb.append(lineFeed).append(body) ;
	        
	        os.write(sb.toString().getBytes()) ;
	        os.flush() ;

	        client.close() ;
        	Log.e("test_ss", "server response:" + sb.toString()) ;
	        
	    } catch (Exception ef) {  
	    	Log.e("test_ss", "server ef:" + ef.getMessage(), ef) ;
	    }  
	}  

}
