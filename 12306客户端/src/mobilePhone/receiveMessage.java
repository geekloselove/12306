package mobilePhone;

import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.TextField;
import java.net.Socket;
import javax.swing.JFrame;

public class receiveMessage extends JFrame implements Runnable{
			
	private static final long serialVersionUID = 1L;
	private Label num;
	private Label message;
	private TextField mes;
	private Socket s;
	
	public receiveMessage(Socket s){
			this.s =s;
	}
	
	public void run() {
		
		num =new Label("�ֻ��ţ�                           ");
		message =new Label("����");
		mes =new TextField(10);
		
		this.add(num);
		this.add(message);
		this.add(mes);
		
		this.setTitle("ģ���ֻ�");
		this.setSize(170,300);
		this.setLocation(200, 20);
		this.setLayout(new FlowLayout());
		this.setVisible(true);

		try {
			//��
			String[] data = tool.read(s).split(":");
			num.setText("�ֻ���:"+data[0]);
			mes.setText(data[1]);
			//д
			tool.write(s,new String("�ֻ����ѽ��յ���֤��:"+data[1]));
		} catch (Exception e1) {

		}	
	}
}
