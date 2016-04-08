import java.io.IOException;
import java.math.BigDecimal;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;

public class service implements Runnable {

	 private dataBase db = dataBase.getInstance();
	 private Hashtable<String, String> ticketInfo;
	 private String[] ticInfFromcli;
	 private String phone;
	 private int code ;
	 private Hashtable<String, Integer> ht =ticketNumber.getTicketContainer();
	 private  String data;
	 private String[] str;
	 private boolean flag;

	 private Socket s;
	 private Lock l;
	 
	 public service(Socket s,Lock l){
		this.s = s;
		this.l = l;
	 }
	
	public void run(){
		try {
			flag =true;
			while(true){
				if(flagService())
					loginService();//��¼�����ݽ��ܵĵ绰�����Ƿ�ע����д�����ע�ᷢ����֤�뵽�ͻ��˺��ֻ��ϣ�δע�᲻����	
				else 
					registerService();//ע��
			}	
		} catch (Exception e) {
		}
	}
	
	private boolean flagService() throws Exception{
		if(flag)
			System.out.println("���������������̣߳�"+Thread.currentThread().getName()+"�����ˡ�������������������");	
		flag =false;	
		//��,���ⲻ�ù����࣬��Ϊ������Ҫ������з���ֵ�����ֲ�������ʽ����������
		byte[] b =new byte[1024];
		int len;
		len = s.getInputStream().read(b);
		data = new String(b,0,len);
		System.out.println("�������յ���Ϣ��"+data);
		//�ָ����ݣ���ȡ����
		str= data.split(":");
		phone = str[0];
		return str.length==1?true:false;
	}
	 
	private void registerService() throws Exception{
		System.out.println("����������������������������ע�ᴦ��������������������������������");
		if(!db.contain(phone)){//��ֹ�ظ�ע��
			//д�����ݿ�
			db.getuserInfo().add(data);
			//��ȡ�û���
			String user = str[1];
			//ע��ɹ�
			System.out.println("�û�"+user+"ע��ɹ�������");
			//д
			IOTool.write(s, "r");
		}else{
			System.out.println("���û���ע�����");
			//д
			IOTool.write(s, "ur");
		}
	}

	private void loginService() throws Exception {
		//�ж��ֻ����Ƿ�ע�ᣬ���Ƿ�����֤�뵽12306�ͻ���
		if(recePhoService()){
			//������֤�뵽�ֻ�
			sendCodeService();		
			//���Ϳͻ����ͳ�Ʊ��Ϣ����Ʊ����
			sendInitInfo();	
			//ʵ�ֶ�ȡ�ͻ��˵�Ʊ��Ϣ�����͸��ͻ���ʣ���Ʊ��
			ticketService();
		}	
	}

	private void ticketService() throws Exception  {
		System.out.println("���������������Ĳ����û�����Ʊ������־���û���ʽ��½�ˣ�������������������");
		while(true){ 
			ticInfFromcli =IOTool.read(s).split(":");
			if(ticInfFromcli[0].equals("��Ҫ�˳���")){//�ͻ����˳���¼
				System.out.println("���ź����û�"+db.getUser(phone)+"�˳��ͻ���"+System.lineSeparator()
						+"������������������������������������������������������������������������������");
				break;		
			}else if(ticInfFromcli.length==1){
				System.out.println("�ͻ�����ȫƱ��Ϣ����");
				returnAllService();//��ȫƱ����
			}else if(ticInfFromcli.length==3){
				if(new Integer(ticInfFromcli[2])==0){
					System.out.println("�ͻ��˶�Ʊ��Ϣ����:"+ticInfFromcli[0]+":"+ticInfFromcli[1]);
					bookService();//��Ʊ����
				}	
				else if(new Integer(ticInfFromcli[2])==1){
					System.out.println("�ͻ�����Ʊ��Ϣ����:"+ticInfFromcli[0]+":"+ticInfFromcli[1]);
					returnService();//��Ʊ����
				}		
			}
		}	
	}
	
	private void returnAllService() {
		l.lock();
		try{
			//ʣƱ����
			String usertn,dbtn;
			Iterator<String> dbIt;
			Iterator<String> userIt =ticketInfo.keySet().iterator();
			while(userIt.hasNext()){
				 usertn= userIt.next();
				 dbIt=ht.keySet().iterator();
				 while(dbIt.hasNext()){
					dbtn=dbIt.next();
					if(usertn.equals(dbtn)){
						 Integer v = ht.get(dbtn);
						 v++;
						 ht.remove(dbtn);
						 ht.put(dbtn, v);
						 break;
					}		
				}
			}
			//ȫ��
			ticketInfo.clear();
			System.out.println("����û��϶�̫�������ˣ���Ȼ��ȫƱ������");
			System.out.println("****************************************");
		}finally{
			l.unlock();
		}	
	}

	private void returnService() throws Exception{
		l.lock();
		try{
			//ɾ��ָ������
			if(ticketInfo!=null){
				Iterator<Entry<String, String>> i =  ticketInfo.entrySet().iterator();
				while(i.hasNext()){
					Entry<String, String> entryInfo = i.next();
					String key = entryInfo.getKey();
					String value = entryInfo.getValue();
					if(ticInfFromcli[0].equals(key)&&ticInfFromcli[1].equals(value)){
						System.out.println("�û���Ʊ��Ϣ������Ϊ"+key+"����Ʊʱ��Ϊ"+value);
						i.remove();
						System.out.println("��ʱ�û���Ʊ��ϢΪ��"+System.lineSeparator()+ticketInfo);
						System.out.println("****************************************************");
						//ʣƱ����
						Iterator<String> it =ht.keySet().iterator();
						while(it.hasNext()){
							String k =it.next();
							if(ticInfFromcli[0].equals(k)){
								 Integer v = ht.get(k);
								 v++;
								 ht.remove(k);
								 ht.put(k, v);
								 IOTool.write(s,new String(v+""));
								 break;
							}		
						}
						break;
					}
				}
			}
		}finally{
			l.unlock();
		}
	}
	
	private void bookService() throws Exception{
		l.lock();
		try{	
			ticketInfo.put( ticInfFromcli[0],  ticInfFromcli[1]);
			System.out.println("�û���Ʊ��Ϣ������Ϊ"+ticInfFromcli[0]+"����Ʊʱ��Ϊ"+ticInfFromcli[1]);
			System.out.println("��ʱ�û���Ʊ��ϢΪ��"+System.lineSeparator()+ticketInfo);
			System.out.println("****************************************************");
			String k;
			//ʣƱ����
			Iterator<String> it =ht.keySet().iterator();
			while(it.hasNext()){
				k =it.next();
				if(ticInfFromcli[0].equals(k)){
					 Integer v = ht.get(k);
					 v--;
					 ht.remove(k);
					 ht.put(k, v);
					 IOTool.write(s,new String(v+""));
					 break;
				}		
			}
		}finally{
			l.unlock();
		}	
	}

	private boolean recePhoService() throws Exception{
		System.out.println("������������������������������¼��������������������������������������");
		System.out.println("��ע����û���Ϣ���绰+�û���+���֤����");
		System.out.println(db.getuserInfo());
		System.out.println("����"+db.getuserInfo().size()+"���û�ע���ˣ�Ŭ��ͻ��һ���û�������");
		System.out.println("������������������������һ�����û��Ƿ�ע�ᴦ����������������������������");
		if(!db.contain(phone)){
			System.out.println("����"+phone+"��δע��");	
			//д
			IOTool.write(s,"ur");
			//��,ֻΪ����д������Ӧ
			System.out.println(IOTool.read(s));
			return false;
		}else{
			System.out.println("����"+phone+"ע����");	
			//������֤��
			code =new BigDecimal(1000+Math.ceil(Math.random()*8999)).setScale(0).intValue();
			System.out.println("������֤�룺"+code);
			//д
			IOTool.write(s,"r");
			//����ֻΪ����д������Ӧ
			System.out.println(IOTool.read(s));
			return true;
		}
		
	}
	
	private void sendCodeService() throws InterruptedException, IOException {
		System.out.println("���������������������ڶ�����������֤�뵽�ֻ���12306�ͻ��ˡ�������������������");
		
		//д���ֻ�
		IOTool.write(s, new String(phone+":"+new Integer(code)));
		
		//��
		System.out.println("�յ��ֻ���Ϣ��"+IOTool.read(s));
		
		//д��12306�ͻ���
		s.getOutputStream().write(new String(code+"").getBytes());
		
		//Ϊ�˽�ǰ��д�����ֿ�����û�и��õĵķ���
		//��ʽһ���ر�Socket������Ҫ���´���Socket
//		s.close();
//		s=ss.accept();
		//��ʽ�����رձ�ǣ����Ǻ��治�ܽ���д����
//		s.shutdownOutput();
		//��ʽ����������Socketĳ����������Ч
//		System.out.println(s.getInetAddress().getHostAddress());
		//��ʽ�ģ���12306�ͻ���
		System.out.println(IOTool.read(s));	
	}
	
	private void sendInitInfo() throws Exception {		
		System.out.println("�������������������������������û���ʼ������������������������������������");
		
		//�����û����ֺͳ���
		IOTool.write(s, db.getUser(phone)+":"+ht.keySet().toString());
		//��ȡ������Ϣ
		System.out.println("�յ��ͻ�����Ϣ��"+IOTool.read(s));
		
		//1.���ݺ����ȡ�û���Ϣ����ȡ�ô��Ʊ��Ϣ������
		String info = phone+":"+db.getUser(phone)+":"+db.getID(phone);
		ticketInfo = db.getInfo().get(info);
		if (ticketInfo!=null) {
			System.out.println("��ע���û��ĳ�Ʊ��Ϣ"+System.lineSeparator()+ticketInfo);
			if (ticketInfo.size()!=0) {
				//2.�����洢��Ʊ��Ϣ���������η���ȥ
				Iterator<Entry<String, String>> i = ticketInfo.entrySet().iterator();
				while (i.hasNext()) {
					Entry<String, String> ei = i.next();
					String key = ei.getKey();
					Iterator<String> it =ht.keySet().iterator();
					while(it.hasNext()){
						String k =it.next();
						if(key.equals(k)){
							 Integer v = ht.get(k);
							 IOTool.write(s,new String(key + ":" + ei.getValue() + ":" +v));
						}		
					}
				}
			}else{
				IOTool.write(s,System.lineSeparator());
			}		
		}else{
			ticketInfo = new Hashtable<String, String>();
			db.getInfo().put(info, ticketInfo);
			IOTool.write(s,System.lineSeparator());
		}
	}
}
