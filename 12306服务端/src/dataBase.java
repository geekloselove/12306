import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

public class dataBase {
	
	private static dataBase db;
	
	private Hashtable<String,Hashtable<String,String>> mydatabase ;//�洢�ص㣺����ȳ������û���Ϣ�ͳ�Ʊ��Ϣ
	private ArrayList<String> userinfo ;//���û���Ϣ
	
	private final int maxsize =100;//��ζ�̬�������ڴ����
	private String[] elem  = new String[maxsize];
	private String[] pho = new String[maxsize];
	private String[] user = new String[maxsize];
	private String[] id = new String[maxsize];
	
	private dataBase(){
		
	}
	
	public static dataBase getInstance(){	
		if(db==null)
			db = new dataBase();
		return db;
	}
	
	public int query(String phone){
		int i;
		if (userinfo!=null&&userinfo.size()!=0){
			for (i = 0; i < pho.length; i++)
				if (pho[i].equals(phone))
					return i;
			return -1;
		}			
		return -1;
			
	}
	public String getUser(String phone){
		int index = query(phone);
		if(index==-1)
			return null;
		return user[index];
		
	}
	
	public String getID(String phone){
		int index = query(phone);
		if(index==-1)
			return null;
		return id[index];
		
	}
	
	public boolean contain(String phone){
		splitUserInfo();
		int index = query(phone);
		if(index==-1)
			return false;
		return true;
	}
	
	private void splitUserInfo(){
		int i=0;
		if(userinfo!=null&&userinfo.size()!=0){
			Iterator<String> it = userinfo.iterator();
			while(it.hasNext()){
				elem= it.next().split(":");
				pho[i] =elem[0];
				user[i] =elem[1];
				id[i] = elem[2];
				i++;
			}
		}
	}
	
	public Hashtable<String,Hashtable<String,String>> getInfo(){
		if(mydatabase==null){
			mydatabase = new Hashtable<String,Hashtable<String,String>>() ;
		}
		 return mydatabase;
	}
	
	
	public  ArrayList<String> getuserInfo(){
		if(userinfo==null)
			userinfo= new  ArrayList<String>() ;
		 return userinfo;
	}
	
	
}
