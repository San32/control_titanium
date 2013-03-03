package com.risan.tita;

import java.io.IOException;


//외부 프로그램 실행 하기

//Runtime 클래스의 exec() 메소드를 통해 외부 프로그램을 실행 시킬 수 있으며 Process 클래스의 waitFor() 메소드를 사용하면 실행시킨 프로그램이 종료 될 때까지 기다릴 수 있습니다.

//아래의 예제를 참고 하세요~

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

