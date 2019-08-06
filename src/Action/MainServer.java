package Action;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;


public class MainServer implements Runnable {
	// ServerŬ����: ������ ���� ���Ӽ���, ����Ŭ���̾�Ʈ ����

	Vector<UserDTO> allV;// ��� �����(���ǻ���� + ��ȭ������)
	Vector<UserDTO> waitV;// ���� �����
	Vector<Room> roomV;// ������ ��ȭ�� Room-vs(Vector) : ��ȭ������

	public MainServer() {

		allV = new Vector<>();

		waitV = new Vector<>();

		roomV = new Vector<>();

		// Thread t = new Thread(run�޼ҵ��� ��ġ); t.start();

		new Thread(this).start();

	}// ������

	@Override

	public void run() {

		try {

			ServerSocket ss = new ServerSocket(5000);

			// ���� �������� ip + ��õ� port ----> ���ϼ���

			System.out.println("Start Server.......");

			while (true) {

				Socket s = ss.accept();// Ŭ���̾�Ʈ ���� ���

				// s: ������ Ŭ���̾�Ʈ�� ��������

				UserDTO ser = new UserDTO(s, this);

				// allV.add(ser);//��ü����ڿ� ���

				// waitV.add(ser);//���ǻ���ڿ� ���

			}

		} catch (IOException e) {

			e.printStackTrace();

		}

	}// run

	public static void main(String[] args) {

		new MainServer();

	}
}
