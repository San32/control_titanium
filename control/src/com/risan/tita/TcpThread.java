package com.risan.tita;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Administrator
 */
public class TcpThread extends Thread{    
    
    private Socket sock = null;                   
    private BufferedReader br = null;    
    private PrintWriter pw = null;   
    Control control;
    private String host;
    private int port;
    public boolean connected = false;
    
    

    
    public TcpThread(Control control){                    
        this.control = control;
    }                 
    
    private void disp(String str){
        control.disp(str);
    }
    
    private void dom(String str){
    	control.domProcess(str);
    }
    
    private void test(String str){
    	/*
    	String a,b;
    	int i;
    	i = str.length();
    	a = str.substring(i, i+1);
    	if(a == null) disp("a = null");
    	else disp("a ="+a);
    	*/
    	str = str+"\0";
    	disp(str);
    	disp("str length = " + str.length());
    }
    
    
    public boolean connect(String host, int port){
        
        this.host = host;
        this.port = port;
        sock = null;
        pw = null;
        br = null;
        boolean ok = false;
        try {
            sock = new Socket(host,port);
            disp("立加 IP : "+sock.getInetAddress());
            disp("立加 Port : "+sock.getPort());
            pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
            br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            ok = true;
            connected = true;
        } catch (IOException ex) {
            disp("Error message : "+ex);
            connected = false;
        }
        return ok;
    }
    
    public boolean disconnect(){
        boolean ok = false;
        if(sock!=null) sock=null;
        if(pw!=null) pw=null;
        if(br!=null) br=null;
        
        return ok;
        
    }
    
    public void sendMsg(String str){
        //String len= Util.convertHexToString(Util.byteArrayToHex(Util.intTo4ByteArray(str.length())));
        //str=len+str;
        //String n = null;
        control.disp("S:"+str);
        pw.print(str+"\0");
        pw.flush();
    }
    
    public void sendMsgTita(String str){
        str =  "<?xml version=\"1.0\" encoding=\"utf-8\"?>"+str;
        control.disp("S:"+str);
        pw.print( str+"\0");
        pw.flush();
    }
    
    public void run(){
                       
        int len=0;
        int ii=0;
        int count=0;
        String buff="";
        String temp;
        
         
        try{
            while((ii = br.read()) != -1){
                if(ii!=0){
                    buff = buff+(char)ii;
                    //System.out.print(ii);
                }else{
                    //System.out.println(buff);
                	//buff = buff.substring(0, buff.length());
                	
                    disp("R:"+buff);
                    //disp("length = "+buff.length());
                    dom(buff.substring(0, buff.length()));
                    //test(buff);
                    buff = "";
                    
                }
                
            }
                      
        }catch(Exception ex){   
            disp("Error : "+ ex);
        }finally{                  
               try{         
                     if(br != null)      
                            br.close();
               }catch(Exception ex){ 
                   disp("Error : "+ ex);
               }           
               try{         
                     if(sock != null)    
                            sock.close();
               }catch(Exception ex){
                    disp("Error : "+ ex);
                }           
        }            
        
}
}
