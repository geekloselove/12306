import java.net.Socket;

public class startClient {
	//�����߳��������߳�ʱ�����߳�ִ�н�����ִ�����̣߳����̱߳����߳����ȼ���
	public static void main(String[] args) throws Exception {
		
		Socket s =new Socket("localhost",20000);
		new Thread(new login(s)).start();
		
		
	}

}
