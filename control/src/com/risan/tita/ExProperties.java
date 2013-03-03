package com.risan.tita;


import java.util.*;
import java.io.*;


class ExProperties {
	
	Control control;
	
	final String path = "c:\\control\\user.ini";
	public static void main(String args[]) {
		new ExProperties();
	}
	
	public ExProperties(Control control){
		this.control = control;
		doit();
	}
	
	public ExProperties(){
		
		doit();
	}
	
	public void doit() {
		try{
			System.out.println("start");
			File f = new File(path); 
			Properties p = new Properties();
			if(f.exists()) {
				System.out.println("File open !");
			}else{
				System.out.println("No File");
				
			}

   // ini 파일 읽기
			p.load(new FileInputStream(path));
			System.out.println("load..");
			
			control.titaniumIp = p.getProperty("ip");
			control.tab1Name = p.getProperty("mon1Name");
			control.tab2Name = p.getProperty("mon2Name");
			control.tab1Full = p.getProperty("mon1Full");
			control.tab2Full = p.getProperty("mon2Full");
			
			System.out.println("tab1Name:"+p.getProperty("mon1Name"));
			System.out.println("tab1Name:"+p.getProperty("mon2Name"));
			System.out.println("tab1Full:"+p.getProperty("mon1Full"));
			System.out.println("tab2Full:"+p.getProperty("mon2Full"));
			   
   // Key 값 저장
			//p.setProperty("Key", p.getProperty("DBuser" ));
			//p.list(System.out);
   // ini 파일 쓰기
			//p.store( new FileOutputStream("c:\\java\\user.ini"), "done.");
		}catch (Exception e) {
			System.out.println(e);
		}
	}
}