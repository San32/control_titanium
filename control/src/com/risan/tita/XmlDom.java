package com.risan.tita;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringReader; 
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory; 
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource; 
/* * Simple demo of JDOM */

public final class XmlDom{
	Control main;
	DocumentBuilderFactory t_dbf = null;
    DocumentBuilder t_db = null;
    Document t_doc = null;
    NodeList t_nodes = null;
    Node t_node = null;
    Element t_element = null;
    InputSource t_is = new InputSource();
    
	public XmlDom(Control main){                    
        this.main = main;
    } 
	
	public void xmlPaser(String xml){
		String layoutindex, uuid, name;
		String temp, temp1, temp2, temp3, temp4;
        try{
        	t_dbf = DocumentBuilderFactory.newInstance();
        	t_db = t_dbf.newDocumentBuilder();
        	t_is = new InputSource();
        	t_is.setCharacterStream(new StringReader(xml));
        	t_doc = t_db.parse(t_is);
        	
        	
        	
        	
        	
        	
        	if(t_doc.hasChildNodes()){
        		disp("t_doc has Child:"+t_doc.hasChildNodes());
        		
        		while(true){
        			t_node = t_doc.getFirstChild();
        			temp = t_node.getNodeName();
        			if(temp.equals("GetLayoutListRes")){
        				disp("this node : GetLayoutListRes");
        				t_nodes = t_doc.getElementsByTagName("LayoutName");
        	        	
        	        	for (int i = 0, t_len = t_nodes.getLength(); i < t_len; i ++)
        	        	{
        	        		t_element = (Element)t_nodes.item(i);
        	        		layoutindex = t_element.getAttribute("layoutindex");
        	        		uuid = t_element.getAttribute("uuid");
        	        		name = t_element.getTextContent();
        	        		System.out.println(name +"  "+ uuid+"  "+ layoutindex);
        	        		
        	        		if(main.tab1Name.equals(name)){
        	        			System.out.println("tab1Name:"+main.tab1Name);
        	        			System.out.println("Name:"+name);
        	        			
        	        			main.tab1uuid = uuid;
        	        			disp("tab1uuid:"+uuid);
        	        		}
        	        		if(main.tab2Name.equals(name)){
        	        			System.out.println("tab2Name:"+main.tab1Name);
        	        			System.out.println("Name:"+name);
        	        			
        	        			main.tab2uuid = uuid;
        	        			disp("tab2uuid:"+uuid);
        	        		}
        	        	}
        			}else if(temp.equals("GetLiveScreenLayoutInfoRes")){
        				disp("this node : GetLiveScreenLayoutInfoRes");
        				t_nodes = t_doc.getElementsByTagName("LayoutName");
        	        	for (int i = 0, t_len = t_nodes.getLength(); i < t_len; i ++)
        	        	{
        	        		t_element = (Element)t_nodes.item(i);
        	        		main.livetab = t_element.getAttribute("livetab");
        	        		main.livescreenstatus= t_element.getAttribute("livescreenstatus");
        	        		main.live_uuid = t_element.getAttribute("uuid");
        	        		main.live_LayoutName = t_element.getTextContent();
        	        		if((main.livetab.equals("1")) && (main.livescreenstatus.equals("1")) && (main.live_LayoutName.equals(main.tab1Name))){
        	        			main.mon1full = true;
        	        		}else main.mon1full = false;
        	        		if((main.livetab.equals("2")) && (main.livescreenstatus.equals("2")) && (main.live_LayoutName.equals(main.tab2Name))){
        	        			main.mon2full = true;
        	        		}else main.mon2full = false;
        	        		
        	        		
        	        	}
        			}
        			t_node = t_doc.getNextSibling();
        			if (t_node == null){
        				disp("next node null");
        				break;
        			}
        					
        			
        		}
        	}
        	
        	
        	/*
        	
        	t_node = t_doc.getFirstChild();
        	disp("nodeName:"+t_node.getNodeName());
        	disp("LayoutName");
        	disp("hasChileNode:"+t_node.hasChildNodes());
        	
        	disp("getNodeName:"+t_node.getNodeName());
        	disp("getLocalName:"+t_node.getLocalName());
        	disp("getNodeValue:"+t_node.getNodeValue());
        	
        	
        	
        	t_nodes = t_doc.getElementsByTagName("LayoutName");
        	
        	for (int i = 0, t_len = t_nodes.getLength(); i < t_len; i ++)
        	{
        		t_element = (Element)t_nodes.item(i);
        		layoutindex = t_element.getAttribute("layoutindex");
        		uuid = t_element.getAttribute("uuid");
        		name = t_element.getTextContent();
        		//disp(name +"  "+ uuid+"  "+ layoutindex);
        		
        		if(name.equals("up")){
        			main.up = uuid;
        			disp("up:"+uuid);
        		}else if(name.equals("down")){
        			main.down = uuid;
        		    disp("down:"+uuid);
        		}
        	}
        	
        	t_nodes = t_doc.getElementsByTagName("LayoutName");
        	for (int i = 0, t_len = t_nodes.getLength(); i < t_len; i ++)
        	{
        		t_element = (Element)t_nodes.item(i);
        		disp("livetab:"+ t_element.getAttribute("livetab"));
        		disp("livescreenstatus:"+ t_element.getAttribute("¡± livescreenstatus"));
        		disp("UUID:"+ t_element.getAttribute("uuid"));
        		disp("layoutName:"+ t_element.getTextContent());
        		
        	}
        	*/
        }catch (Exception e){
        	e.printStackTrace();
        }
	}
	
	private void disp(String str){
        main.disp(str);
    }
} 

