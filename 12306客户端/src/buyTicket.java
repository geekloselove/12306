import java.awt.Button;
import java.awt.Choice;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.sql.Date;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

import mobilePhone.tool;


public class buyTicket extends Frame implements Runnable{
	
	private static final long serialVersionUID = 1L;
	
	private Label name;
	private Button exit;
	
	private Button bookTicket;
	private Label bookTrain;
	private Choice bttn;
	
	private Button returnTicket;
	private Label returnTrain;
	private Choice rttn;
	private Label buyTickTime;
	private Choice year;
	private Label ylabel;
	private Choice month;
	private Label mlabel;
	private Choice day;
	private Label dlabel;
	
	private Button query;
	private Button returnAllTicket;
	
	private Label service;
	private Button send;
	private TextField chat;
	private TextArea showChat;
	
	private Label blank;
	private Label blank1;
	private Label blank2;
	
	private Calendar c = Calendar.getInstance();
	
	private String user;
	
	private int ticketNum =0;//�洢һ���û��Ķ�Ʊ�����
	private Hashtable<String, String> subtm = tempContainer.getTicketInfo();//�洢һ���û��Ķ�Ʊ��Ϣ����������
	
	private Socket s;
	private String tn;//��Ʊ����
	private Date time;//��Ʊʱ��
	private String key; 
	private String value; 
	private String leftNum;//ʣ��Ʊ��
	
	public buyTicket(Socket s){
		this.s = s;
	}
	
	public buyTicket() {
		
	}

	public void run(){
		
		name = new Label();
		exit =new Button("�˳�");
		
		blank = new Label("������������������Ʊ����������������");
		bookTicket =new Button("��Ʊ");
		bookTrain = new Label("��Ʊ����  ");
		bttn =new Choice();
		
		blank1 = new Label("������������������Ʊ����������������");
		returnTicket =new Button("��Ʊ ");
		returnTrain = new Label("��Ʊ����  ");
		rttn =new Choice();
		buyTickTime = new Label("��Ʊʱ��  ");
		year=new Choice();
		ylabel=new Label("��");
		month=new Choice();
		mlabel=new Label("��");
		day=new Choice();
		dlabel=new Label("��");
		
		blank2 = new Label("������������������ѯ��ȫ�ˡ���������������");
		query =new Button("��ѯ");
		returnAllTicket = new Button("ȫ��");
		
		
		send =new Button("����");
		chat =new TextField(20);
		showChat =new TextArea(8,25);
		service =new Label("������������������ѯ�ͷ����ڡ���������������");		
		
		year.add("");
		year.add(c.get(Calendar.YEAR)+"");
		if(hasTwelve())
			year.add(c.get(Calendar.YEAR)+1+"");
		
		month.add("");
		for(int i=0;i<3;i++)
			month.add(getProperMonth(c.get(Calendar.MONTH)+i+1));
		
		day.add("");
		for(int i=1;i<32;i++)
			if(i<10)
				day.add("0"+i);
			else
				day.add(""+i);
		
		
		this.add(name);
		this.add(exit);
		
		String[] init = tool.read(s).split(":");
		//�����û���
		user=init[0];
		name.setText("�𾴵�"+user+",���ã�");
		//��������
		String[] strtn =init[1].substring(1, init[1].length()-1).split(", ");
		//��ӳ���
		bttn.add("");
		rttn.add("");
		for(int i=0;i<strtn.length;i++){
			bttn.add(strtn[i]);
			rttn.add(strtn[i]);
		}
		//д
		tool.write(s,"�ͻ��˳�ʼ��˳�����"); 
//		System.out.println("�ͻ��˳�ʼ����ɣ�����");
		
		this.add(blank);
		this.add(bookTicket);
		this.add(bookTrain);
		this.add(bttn);
		
		this.add(blank1);
		this.add(returnTicket);
		this.add(returnTrain);
		this.add(rttn);
		
		this.add(buyTickTime);
		this.add(year);
		this.add(ylabel);
		this.add(month);
		this.add(mlabel);
		this.add(day);
		this.add(dlabel);
		
		this.add(blank2);
		this.add(query);
		this.add(returnAllTicket);
		
		this.add(service);
		this.add(showChat);
		showChat.setEditable(false);
		this.add(chat);
		this.add(send);
		
		this.setTitle("12309��Ʊ��");
		this.setVisible(true);
		this.setSize(280, 540);
		this.setLayout(new FlowLayout());
		this.setLocation(725, 20);
		
		//��ȡ��Ʊ��Ϣ,���з�����Ϊ2
		String info =tool.read(s);
		if(info!=System.lineSeparator()){
			String[] data=info.split(System.lineSeparator());
			for(int i=0;i<data.length;i++){
				String[] subData = data[i].split(":"); 	
			    subtm.put(subData[0], subData[1]+":"+subData[2]);
			}
		}	
		System.out.println("�û���Ʊ��Ϣ��ʼ����ɣ�"+subtm);
		
		exit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				tool.write(s,"��Ҫ�˳���");
				System.exit(0);
			}
		});
		
		bookTicket.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				//��ȡ����
				tn = bttn.getSelectedItem();
				//��ȡ��Ʊʱ��
				if (tn!="") {
					time =new Date(System.currentTimeMillis()) ;
					//��Ʊ���ƣ�һ�첻��������ͬһ���Σ���һ����ֻ��һ�ţ���һ�충Ʊ����������3�ţ�ע�ⲻ������ͨ�ţ�ֻ���Ƚ��ͻ��˵Ķ�Ʊʵ�ּ���
					if(ticketNum<3&&!subtm.containsKey(tn)){
						//����������
						try {
							leftNum = sendTicketInfo(s,tn,time+"",0);
						} catch (Exception e1) {
						
						}
						//Ʊ����һ���
						ticketNum++;
						//�Ž���������
						subtm.put(tn, time+":"+leftNum);//���ʣ��Ʊ��
						//��ʾ
						tool.myAlert2(new buyTicket(), "��Ʊ�ɹ�", 725, 50);
					}	
				}
			}	
		});
				
		returnTicket.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				//��ȡ����
				String str = rttn.getSelectedItem();
				//��ȡʱ��
				String y = year.getSelectedItem();
				String m = month.getSelectedItem();
				String d = day.getSelectedItem();
					
				//ɾ����������
				if (str!=""&&y!=""&&m!=""&&d!="") {	
					int flag = 0;
					Entry<String, String> tickentry;
					Iterator<Entry<String, String>> i = subtm.entrySet().iterator();
					while(i.hasNext()){
						tickentry = i.next();
						key = tickentry.getKey();
						String[] strInfo = tickentry.getValue().split(":");
						value = strInfo[0];	
						if(flag==1)
							break;
						else if(key.equals(str)&&value.equals(y+"-"+m+"-"+d)){
							//������Ʊ��Ϣ��������
							try {
								leftNum = sendTicketInfo(s,key,value,1);
							} catch (Exception e1) {
							
							}
							//Ʊ����Ǽ�һ
							ticketNum--;
							//ɾ�����ǵ�ǰƱ���޷�������ָ��
							i.remove();
							/*ɾ�������һ��Ʊ : ht.remove(db.getId()-1);*/
							
							//��ʾ
							tool.myAlert2(new buyTicket(), "��Ʊ�ɹ�", 725, 50);
							//��־�ı�Ϊ1���˳�������Ʊ����
							flag = 1;	
						}
					}
					if (leftNum!=null) {//�Գ����ڲ�ͬ�충��ͬ���ε�ʣƱ�����и���
						while (i.hasNext()) {
							tickentry = i.next();
							String[] strInfo = tickentry.getValue().split(":");
							if (tickentry.getKey().equals(str)) {
								tickentry.setValue(strInfo[0] + ":"+ leftNum);
							}
						}
					}	
				}	
			}
		});
		
		query.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				new Thread(showTickInfo.getInstance()).start();
			}
			
		});
		
		returnAllTicket.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				ticketNum=0;
				subtm.clear();
				//�����˷�����־��˵�����û�����Ʊ��Ϣɾ��
				tool.write(s,System.lineSeparator());
				//��ʾ
				tool.myAlert2(new buyTicket(), "�û�����ȫƱ�ɹ������Ǹо����ź�", 725, 50);
			}	
		});
		
		send.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				//1.��ȡ�ı�����
				String chatText = chat.getText();
				if(chatText!=null){
					//2.���ı�������ʾ���ı�����,�����ı�������ɾ��
					String content = user+":"+chatText+System.lineSeparator();
					showChat.append(content);
					chat.setText(null);
					//3.ͨ��TCPЭ���������ʵ��ȫ˫��ͨ��
//					Socket s;
//					try {
//						s = new Socket("localhost", 20001);
//						Thread cr =new Thread(new clientRead(s));
//						cr.start();
//						System.out.println("�ͻ��˶��߳�"+cr.getName()+"����");
//						
//						Thread cw =new Thread(new clientWrite(s));
//						cw.start();
//						System.out.println("�ͻ���д�߳�"+cw.getName()+"����");
//					} catch (Exception e3) {
//						
//					} 	
				}	
			}
			
		});
	}


	private String getProperMonth(int i) {
		if(i%12==0)
			i=12;
		else
			i=i%12;
		if(i<10)
			return "0"+i;
		else
			return i+"";
	}

	private boolean hasTwelve() {
	
		return (c.get(Calendar.MONTH)+1)%12==0||(c.get(Calendar.MONTH)+2)%12==0?true:false;
	}
	
	//ʵ�ַ�����Ʊ����Ʊ��Ϣ������ˣ���ȡ�����ʣ��Ʊ����Ϊʲô���ܴӷ����һ�λ�ȡƱ�����������ڶ��û��Ŀɿ���
	private String sendTicketInfo(Socket s,String key,String value,int flag) throws Exception{
		tool.write(s,(key+":"+value+":"+flag));
		return tool.read(s);
	}
}
