package com.risan.tita;

import java.io.IOException;


//�ܺ� ���α׷� ���� �ϱ�

//Runtime Ŭ������ exec() �޼ҵ带 ���� �ܺ� ���α׷��� ���� ��ų �� ������ Process Ŭ������ waitFor() �޼ҵ带 ����ϸ� �����Ų ���α׷��� ���� �� ������ ��ٸ� �� �ֽ��ϴ�.

//�Ʒ��� ������ ���� �ϼ���~

public class Execute {

	Runtime r = Runtime.getRuntime();
    Process nc;
    Control control;
    
       public static void main(String[] args) throws Exception {
               new Execute();
       }
       
       
       public Execute() throws Exception{
    	   control = new Control();
    	   //control.frame.setVisible(true);
    	   
    	   
    	   while(true){
               
				nc = r.exec("C:\\Program Files (x86)\\NCTitanium\\MonitoringService\\NCTitanium.exe");
				this.sleep(2000);
				control.start();
				nc.waitFor();
				control.clear();
				
            
            System.out.println("The End......restart");
    	   }
       }
       
   	public void sleep(int time){
	    try {
	      Thread.sleep(time);
	    } catch (InterruptedException e) { }
	}
	
 
}

