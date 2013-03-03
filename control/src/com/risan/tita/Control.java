package com.risan.tita;

import java.awt.EventQueue;
import java.awt.TextArea;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JTextField;
import javax.swing.border.TitledBorder;



public class Control {

	public JFrame frame;
	
	
	TcpThread tcp = null;
	ExProperties ini = null;

	XmlDom dom;
	
	//public  String up = "";  //상선 uuid 저장
	//public  String down= ""; //하선 uuid 저장
	
	public String tab1uuid = "";
	public String tab2uuid = "";
	public String tab1Name = "";
	public String tab2Name = "";
	public String tab1Full = "";
	public String tab2Full = "";
	
	// <LayoutName livetab=”1” livescreenstatus=”1” uuid=”UUID”>LayoutName</LayoutName>
	public String livetab ="";
	public String livescreenstatus ="";
	public String live_uuid ="";
	public String live_LayoutName ="";
	
	
	//현재 설정의 full 상태를 체크해서 저장하는 곳  xml을 모두 처리한다.
	public boolean mon1full = false;
	public boolean mon2full = false;
	
	
	
	final String ind[] = {
			"VIEW1X1", "VIEW2X2", "VIEW3X3", "VIEW1_5L","VIEW4X4", "VIEW1_7L", "VIEW1_12", "VIEW1_12L", "VIEW2_8", "VIEW5X5", 
			"VIEW1_16",	"VIEW1_16L", "VIEW1_9", "VIEW6X6", "VIEW1_20", "VIEW1_32", "VIEW1_20L", "VIEW1_27L", "VIEW2_18", "VIEW7X7", 
			"VIEW1_24", "VIEW1_24L", "VIEW1_33L", "VIEW1_40", "VIEW8X8", "VIEW1_28", "VIEW1_28L", "VIEW1_39L", "VIEW1_48", "VIEW1_48L",
			"VIEW2_32", "WVIEW3X3", "WVIEW1_2L", "WVIEW4X3", "WVIEW1_6L", "WVIEW1_3L", "WVIEW2_4", "WVIEW5X4", "WVIEW1_8L", "WVIEW1_4L", 
			"WVIEW6X4", "WVIEW6X5", "WVIEW1_5L", "WVIEW2_12"};
	
	String titaniumIp ;
	final int titaniumPort = 1091;
	final String xmlHead = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
	static Runtime r = Runtime.getRuntime();
	
	
	// xml 정리
	final String SetMon1FullScreenOn = "<SetLiveFullScreenReq> <FullScreen monitoridx=\'1\' livetab=\'1\'>On</FullScreen> </SetLiveFullScreenReq>";
	final String SetMon1FullScreenOff = "<SetLiveFullScreenReq> <FullScreen monitoridx=\'1\' livetab=\'1\'>Off</FullScreen> </SetLiveFullScreenReq>";
	final String SetMon2FullScreenOn = "<SetLiveFullScreenReq> <FullScreen monitoridx=\'2\' livetab=\'2\'>On</FullScreen> </SetLiveFullScreenReq>";
	final String SetMon2FullScreenOff = "<SetLiveFullScreenReq> <FullScreen monitoridx=\'2\' livetab=\'2\'>Off</FullScreen> </SetLiveFullScreenReq>";
	
	
	final String GetMonitorInfoReq = "<GetMonitorInfoReq></GetMonitorInfoReq>";
	final String GetMonitorCountReq = "<GetMonitorCountReq></GetMonitorCountReq>";
	final String GetAllCameraListReq = "<GetAllCameraListReq></GetAllCameraListReq>";
	final String GetLayoutReq_mon1 = "<GetLayoutReq><MonitorIndex>1</MonitorIndex></GetLayourReq>";
	final String GetLayoutReq_mon2 = "<GetLayoutReq><MonitorIndex>2</MonitorIndex></GetLayourReq>";
	final String GetLayoutListReq =  "<GetLayoutListReq></GetLayoutListReq>";
	

	private JTextArea ta;
	
	
	//SetCommand
	//해당 Monitor의 Layout을 변경한다.
	private void SetLayoutReq(String mon, String ind){
		tcp.sendMsg(xmlHead+"<SetLayoutReq><MonitorIndex>"+mon+"</MonitorIndex><Layout>"+ind+"</Layout></SetLayoutReq>");
	}
	
	//현재 Layout screen index에 Camera를 할당한다.
	//여러개를 한번에 설정할 수 있으나 여기서는 한번에 하나만 한다.
	private void SetChannelAssignReq(String mon, String ch, String uuid){
		tcp.sendMsg(xmlHead+"<SetChannelAssignReq><List><MonitorIndex>"+mon+"</MonitorIndex><UUID ch=\""+ch+"\">"+uuid+"</UUID></List></SetChannelAssignReq>");
	}
	
	//현재 Layout screen index에 Camera를 해제한다.
	private void SetChannelCloseReq(String monidx, String uuid){
		tcp.sendMsg(xmlHead+"<SetChannelCloseReq><UUID monitoridx=\""+monidx+"\">"+uuid+"</UUID></SetChannelCloseReq>");
	}
	
	//Pop-up창을 띄워 해당 카메라의 영상을 볼 수 있다.
	private void SetOpenPopUpCameraReq(String left, String top, String width, String height, String uuid){
		tcp.sendMsg(xmlHead+"<SetOpenPopUpCameraReq><UUID pos=\""+left+","+top+","+width+","+height+"\">"+uuid+"</UUID></SetOpenPopUpCameraReq>");
	}
	
	//Pop-up된 모든 영상을 닫는다.
	private void SetClosePopUpCameraReq(){
		tcp.sendMsg(xmlHead+"<SetClosePopUpCameraReq> </SetClosePopUpCameraReq>");
	}
	
	//해당 Monitor에 Live Tab FULL SCREEN 지정 및 해제를 한다.
	private void SetLiveFullScreenReq(String mon, String tab, String onoff){
		tcp.sendMsg(xmlHead+"<SetLiveFullScreenReq><FullScreen monitoridx=\""+mon+"\" livetab=\""+tab+"\">"+onoff+"</FullScreen></SetLiveFullScreenReq>");
	}
	
	//Live Screen에 Layout List를 지정한다.
	private void SetLayoutListArrangeReq(String tab, String uuid){
		tcp.sendMsg(xmlHead+"<SetLayoutListArrangeReq><UUID livetab=\""+tab+"\">"+uuid+"</UUID></SetLayoutListArrangeReq>");
	}

	private void GetLiveScreenLayoutInfoReq(){
		tcp.sendMsgTita("<GetLiveScreenLayoutInfoReq></GetLiveScreenLayoutInfoReq>");
	}
	
	/**
	 * 수신 메시지 처리
	 */
	
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Control window = new Control();
					window.frame.setVisible(true);
					//autoTimer();
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});
		
		
	}

	/**
	 * Create the application.
	 */
	public Control() {
		
		initialize();
		//this.setVisible(true);
	}
	

	public void clear(){
		tab1uuid = "";  //1번 화면 uuid 저장
		tab2uuid= ""; //2번 화면 uuid 저장
		livetab ="";
		livescreenstatus ="";
		live_uuid ="";
		live_LayoutName ="";
		mon1full = false;
		mon2full = false;
	}
	public void start(){
		

		iniStart();
		connect();
		while(true){
			tcp.sendMsgTita(GetLayoutListReq);
			this.sleep(1000);
			if((tab1uuid != "")&&(tab2uuid != "")){
				break;
				
			}
		}
			
		
			
		GetLiveScreenLayoutInfoReq();
		if(mon1full == false){
			SetLayoutListArrangeReq("1",tab1uuid);
			this.sleep(1000);
			SetLiveFullScreenReq("1","1","On");
		}
		if(mon2full == false){
			SetLayoutListArrangeReq("2",tab2uuid);
			this.sleep(1000);
			SetLiveFullScreenReq("2","2","On");
		}
		
		

	}
	
	public void iniStart(){
		ini = null;
		ini = new ExProperties(this);
		
		ini.doit();
		
	}
	
	public void setTita(){
		
		
		
		tcp.sendMsgTita(GetLayoutListReq);
		this.sleep(1000);
		
		
		SetLayoutListArrangeReq("1",tab1uuid);
		this.sleep(1000);
		SetLayoutListArrangeReq("2",tab2uuid);
		this.sleep(1000);
		
		SetLiveFullScreenReq("1","1","On");
		this.sleep(1000);
		SetLiveFullScreenReq("2","2","On");
		this.sleep(1000);
		
		
	}
	
	public static void runTitanum() {
        //실행시에 인자를 줄려면 exec(String[]) 형태로 호출
        Process nc;
		try {
			System.out.println("start");
			nc = r.exec("C:\\Program Files (x86)\\NCTitanium\\MonitoringService\\NCTitanium.exe");

		} catch (SecurityException | IOException  e) {
			// TODO Auto-generated catch block
			System.out.println("Error");
			e.printStackTrace();
		}
	}
	
	/**
	 * connect
	 */
	private void connect(){
		tcp = null;
		tcp = new TcpThread(this);
		
		while(true){
		
			if(tcp.connect(titaniumIp, titaniumPort)){
				break;
			}
				runTitanum();
				System.out.println("re connect");
				this.sleep(1000);
		}
		tcp.start();
		System.out.println("connect");
	}
	
	

	
	public void sleep(int time){
	    try {
	      Thread.sleep(time);
	    } catch (InterruptedException e) { }
	}
	
	public void domProcess(String xml){
		dom = null;
		dom = new XmlDom(this);
		disp("xml:"+xml);
		dom.xmlPaser(xml);
		
		//disp("up:"+up);
		//disp("down:"+down);
		
		
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1025, 627);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(12, 415, 985, 163);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 10, 961, 143);
		panel_1.add(scrollPane);
		
		ta = new JTextArea();
		scrollPane.setViewportView(ta);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBorder(new TitledBorder(null, "Layout List", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(12, 10, 449, 395);
		frame.getContentPane().add(panel);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(6, 17, 437, 368);
		panel.add(scrollPane_1);
		
		JPanel panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel_2.setBorder(new TitledBorder(null, "모니터1", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(489, 39, 137, 130);
		frame.getContentPane().add(panel_2);
		
		JTextArea textArea1 = new JTextArea();
		textArea1.setBounds(12, 23, 113, 97);
		panel_2.add(textArea1);
		
		JPanel panel_3 = new JPanel();
		panel_3.setLayout(null);
		panel_3.setBorder(new TitledBorder(null, "모니터2", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_3.setBounds(645, 37, 137, 130);
		frame.getContentPane().add(panel_3);
		
		JTextArea textArea_2 = new JTextArea();
		textArea_2.setBounds(12, 23, 113, 97);
		panel_3.add(textArea_2);
		
		JPanel panel_4 = new JPanel();
		panel_4.setLayout(null);
		panel_4.setBorder(new TitledBorder(null, "모니터3", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_4.setBounds(794, 37, 137, 130);
		frame.getContentPane().add(panel_4);
		
		JTextArea textArea_3 = new JTextArea();
		textArea_3.setBounds(12, 23, 113, 97);
		panel_4.add(textArea_3);
		
		JButton btnConnect = new JButton("connect");
		btnConnect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				start();
			}
		});
		btnConnect.setBounds(489, 202, 97, 23);
		frame.getContentPane().add(btnConnect);
		
		JButton btnGetLayout = new JButton("getLayout");
		btnGetLayout.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				tcp.sendMsgTita(GetLayoutListReq);
			}
		});
		btnGetLayout.setBounds(489, 235, 97, 23);
		frame.getContentPane().add(btnGetLayout);
		
		JButton btnLayoutarrange = new JButton("layoutArrange");
		btnLayoutarrange.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				SetLayoutListArrangeReq("1",tab1uuid);
				SetLayoutListArrangeReq("2",tab2uuid);
			}
		});
		btnLayoutarrange.setBounds(489, 268, 176, 23);
		frame.getContentPane().add(btnLayoutarrange);
		
		JButton btnMfull = new JButton("m1Full");
		btnMfull.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				SetLiveFullScreenReq("1","1","On");
				SetLiveFullScreenReq("2","2","On");
				//tcp.sendMsg(SetMon1FullScreenOn);
			}
		});
		btnMfull.setBounds(489, 301, 97, 23);
		frame.getContentPane().add(btnMfull);
		
		JButton btnGetmon = new JButton("getMon");
		btnGetmon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				tcp.sendMsgTita(GetMonitorInfoReq);
			}
		});
		btnGetmon.setBounds(645, 202, 97, 23);
		frame.getContentPane().add(btnGetmon);
		
		JButton btnTimer = new JButton("timer");
		btnTimer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}
		});
		btnTimer.setBounds(764, 202, 97, 23);
		frame.getContentPane().add(btnTimer);
		
		JButton btnGet = new JButton("get");
		btnGet.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				GetLiveScreenLayoutInfoReq();
			}
		});
		btnGet.setBounds(764, 235, 97, 23);
		frame.getContentPane().add(btnGet);
	}
	
	/**
	 * 디스플레이
	 */
	public void disp(String msg){
		ta.append(msg+"\n");
	}
}


