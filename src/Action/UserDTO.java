package Action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Vector;

public class UserDTO extends Thread {

	User user;
	Room myRoom;

	BufferedReader in;
	OutputStream out;

	Vector<UserDTO> allV;// ��� �����(���ǻ���� + ��ȭ������)
	Vector<UserDTO> waitV;// ���� �����
	Vector<Room> roomV;// ������ ��ȭ�� Room-vs(Vector) : ��ȭ������

	Socket s;

	public UserDTO(Socket s, MainServer server) {
		user = new User();
		allV = server.allV;

		waitV = server.waitV;

		roomV = server.roomV;

		this.s = s;

		try {

			in = new BufferedReader(new InputStreamReader(s.getInputStream()));

			out = s.getOutputStream();

			start();

		} catch (IOException e) {

			e.printStackTrace();

		}

	}// ������

	@Override
	public void run() {
		try {

			while (true) {

				String msg = in.readLine();// Ŭ���̾�Ʈ�� ��� �޽����� �ޱ�

				if (msg == null)
					return; // ���������� ����

				if (msg.trim().length() > 0) {

					System.out.println("from Client: " + msg + ":" +

							s.getInetAddress().getHostAddress());

					// �������� ��Ȳ�� �����!!

					String msgs[] = msg.split("\\|");

					String protocol = msgs[0];

					switch (protocol) {

					case "100": // ���� ����

						allV.add(this);// ��ü����ڿ� ���

						waitV.add(this);// ���ǻ���ڿ� ���

						break;

					case "150": // ��ȭ�� �Է�

						user.setName(msgs[1]);
						System.out.println("userName : " + user.getName());
						// ���� ��ȭ�� �Է������� ������ ������ ���

						messageWait("160|" + getRoomInfo());

						messageWait("180|" + getWaitInwon());

						break;

					case "160": // �游��� (��ȭ�� ����)

						myRoom = new Room();

						myRoom.setTitle(msgs[1]);// ������

						myRoom.setUserCount(1);

						myRoom.setAdminName(user.getName());

						roomV.add(myRoom);

						// ����----> ��ȭ�� �̵�!!

						waitV.remove(this);

						myRoom.userV.add(this);

						messageRoom("200|" + user.getName());// ���ο����� ���� �˸�

						// ���� ����ڵ鿡�� �������� ���

						// ��) ��ȭ���:JavaLove

						// -----> roomInfo(JList) : JavaLove--1

						messageWait("160|" + getRoomInfo());

						messageWait("180|" + getWaitInwon());

						break;

					case "170": // (���ǿ���) ��ȭ�� �ο�����

						messageTo("170|" + getRoomInwon(msgs[1]));

						break;

					case "175": // (��ȭ�濡��) ��ȭ�� �ο�����

						messageRoom("175|" + getRoomInwon());

						break;

					case "200": // ����� (��ȭ�� ����) ----> msgs[] = {"200","�ڹٹ�"}

						for (int i = 0; i < roomV.size(); i++) {// ���̸� ã��!!

							Room r = roomV.get(i);

							if (r.getTitle().equals(msgs[1])) {// ��ġ�ϴ� �� ã��!!

								myRoom = r;
								int count = r.getUserCount();
								myRoom.setUserCount(count++);

								break;

							}

						} // for

						// ����----> ��ȭ�� �̵�!!

						waitV.remove(this);

						myRoom.userV.add(this);

						messageRoom("200|" + user.getName());// ���ο����� ���� �˸�

						// �� ���� title����

						messageTo("202|" + myRoom.getTitle());

						messageWait("160|" + getRoomInfo());

						messageWait("180|" + getWaitInwon());

						break;

					case "300": // �޽���

						messageRoom("300|[" + user.getName() + "]�� " + msgs[1]);

						// Ŭ���̾�Ʈ���� �޽��� ������

						break;

					case "400": // ��ȭ�� ����

						int count = myRoom.getUserCount();
						myRoom.setUserCount(count++);// �ο��� ����

						messageRoom("400|" + user.getName());// ���ο��鿡�� ���� �˸�!!

						// ��ȭ��----> ���� �̵�!!

						myRoom.userV.remove(this);

						waitV.add(this);

						// ��ȭ�� ������ ���ο� �ٽ����

						messageRoom("175|" + getRoomInwon());

						// ���ǿ� ������ �ٽ����

						messageWait("160|" + getRoomInfo());

						break;

					}// ���� switch

				} // if

			} // while

		} catch (IOException e) {

			System.out.println("��");

			e.printStackTrace();

		}

	}// run

	public String getRoomInfo() {

		String str = "";

		for (int i = 0; i < roomV.size(); i++) {

			// "�ڹٹ�--1,����Ŭ��--1,JDBC��--1"

			Room r = roomV.get(i);

			str += r.getTitle() + "--" + r.getUserCount();

			if (i < roomV.size() - 1)
				str += ",";

		}

		return str;

	}// getRoomInfo

	public String getRoomInwon() {// �������� �ο�����

		String str = "";

		for (int i = 0; i < myRoom.userV.size(); i++) {

			// "�浿,����,�ֿ�"

			UserDTO ser = myRoom.userV.get(i);

			str += ser.user.getName();

			if (i < myRoom.userV.size() - 1)
				str += ",";

		}

		return str;

	}// getRoomInwon

	public String getRoomInwon(String title) {// ������ Ŭ���� ���� �ο�����

		String str = "";

		for (int i = 0; i < roomV.size(); i++) {

			// "�浿,����,�ֿ�"

			Room room = roomV.get(i);

			if (room.getTitle().equals(title)) {

				for (int j = 0; j < room.userV.size(); j++) {

					UserDTO ser = room.userV.get(j);

					str += ser.user.getName();

					if (j < room.userV.size() - 1)
						str += ",";

				}

				break;

			}

		}

		return str;

	}// getRoomInwon

	public String getWaitInwon() {

		String str = "";

		for (int i = 0; i < waitV.size(); i++) {

			// "�浿,����,�ֿ�"

			UserDTO ser = waitV.get(i);
			System.out.println(ser.user.getName());
			str += ser.user.getName();

			if (i < waitV.size() - 1)
				str += ",";

		}

		return str;

	}// getWaitInwon

	public void messageAll(String msg) {// ��ü�����

		// ���ӵ� ��� Ŭ���̾�Ʈ(����+��ȭ��)���� �޽��� ����

		for (int i = 0; i < allV.size(); i++) {// ���� �ε���

			UserDTO service = allV.get(i); // ������ Ŭ���̾�Ʈ ������

			try {

				service.messageTo(msg);

			} catch (IOException e) {

				// �����߻� ---> Ŭ���̾�Ʈ ���� ����!!

				allV.remove(i--); // ���� ���� Ŭ���̾�Ʈ�� ���Ϳ��� ����!!

				System.out.println("Ŭ���̾�Ʈ ���� ����!!");

			}

		}

	}// messageAll

	public void messageWait(String msg) {// ���� �����

		for (int i = 0; i < waitV.size(); i++) {// ���� �ε���

			UserDTO service = waitV.get(i); // ������ Ŭ���̾�Ʈ ������

			try {

				service.messageTo(msg);

			} catch (IOException e) {

				// �����߻� ---> Ŭ���̾�Ʈ ���� ����!!

				waitV.remove(i--); // ���� ���� Ŭ���̾�Ʈ�� ���Ϳ��� ����!!

				System.out.println("Ŭ���̾�Ʈ ���� ����!!");

			}

		}

	}// messageWait

	public void messageRoom(String msg) {// ��ȭ������

		for (int i = 0; i < myRoom.userV.size(); i++) {// ���� �ε���

			UserDTO service = myRoom.userV.get(i); // ������ Ŭ���̾�Ʈ ������

			try {

				service.messageTo(msg);

			} catch (IOException e) {

				// �����߻� ---> Ŭ���̾�Ʈ ���� ����!!

				myRoom.userV.remove(i--); // ���� ���� Ŭ���̾�Ʈ�� ���Ϳ��� ����!!

				System.out.println("Ŭ���̾�Ʈ ���� ����!!");

			}

		}

	}// messageAll

	public void messageTo(String msg) throws IOException {

		// Ư�� Ŭ���̾�Ʈ���� �޽��� ���� (���� ����--->Ŭ���̾�Ʈ �޽��� ����)

		out.write((msg + "\n").getBytes());

	}

}