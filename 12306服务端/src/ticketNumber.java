import java.util.Hashtable;

public class ticketNumber {
	
	private static int tickNum = 500;
	
	public static String[] tn = {"���������Ϻ�","���ݡ�������","���ݡ�������","���ڡ�������"};
	
	private static Hashtable<String,Integer> ht;
	
	public static  Hashtable<String,Integer> getTicketContainer(){
		if(ht==null){
			ht= new Hashtable<String,Integer>();
			for(int i=0;i<tn.length;i++)
				ht.put(tn[i], tickNum);
		}	
		return ht;
	}	
}
