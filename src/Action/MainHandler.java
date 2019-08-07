package Action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainHandler extends Thread {
	private BufferedReader br;
	private PrintWriter pw;
	private Socket socket;
	private Connection conn;
	private PreparedStatement pstmt;
	private User user;

	private ArrayList<MainHandler> allUserList; // ��ü�����

	// ����, ��ü�����
	public MainHandler(Socket socket, ArrayList<MainHandler> allUserList, Connection conn) throws IOException {
		this.user = new User();
		this.socket = socket;
		this.allUserList = allUserList;
		this.conn = conn;
		br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
	}

	@Override
	public void run() {
		// ������ �Է¹��� �������Ľ� -> ��� �����������
		try {

			String[] line = null;
			while (true) {
				line = br.readLine().split("\\|");

				if (line == null) {
					break;
				}
				if (line[0].compareTo(Protocol.ENTERWAITROOM) == 0) { // �α��� -> ��������
					String sql = "Insert into UserContent values(num.nextval, ? ,'tkd456','�����',26,'jungsangsu456@gmail.com',010,877,301,0,0)";

					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, line[1]);
					int su = pstmt.executeUpdate(); // �׻� ��� ����(CRUD)���� ������ return
					System.out.println(su + "row create");

					sql = "SELECT priNumber FROM UserContent WHERE IDNAME = '" + line[1] + "'";
					ResultSet rs = pstmt.executeQuery(sql);
					int no = 0;
					while (rs.next()) {
						no = rs.getInt("priNumber");
					}
					System.out.println(no);
					pw.println("����Ϸ�");
					pw.flush();
				} else if (line[0].compareTo(Protocol.REGISTER) == 0) // ȸ������
				{
					String userContent[] = line[1].split("%");

					String sql = "Insert into UserContent values(num.nextval,?,?,?,?,?,?)";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, userContent[0]);
					pstmt.setString(2, userContent[1]);
					pstmt.setString(3, userContent[2]);
					pstmt.setString(4, userContent[3]);
					pstmt.setString(5, userContent[4]);
					pstmt.setString(6, userContent[5]);

					int su = pstmt.executeUpdate(); // �׻� ��� ����(CRUD)���� ������ return
					System.out.println(su + "ȸ������[DB]");

				} else if (line[0].compareTo(Protocol.IDSEARCHCHECK) == 0) // ȸ������ ID �ߺ�üũ
				{
					System.out.println(line[0] + "/" + line[1]);
					String sql = "select * from UserContent where IDNAME = '" + line[1] + "'";
					pstmt = conn.prepareStatement(sql);
					ResultSet rs = pstmt.executeQuery(sql);
					String name = null;
					int count = 0;
					while (rs.next()) {
						name = rs.getString("IDNAME");
						if (name.compareTo(line[1]) == 0) {
							count++;
						}
					}
					System.out.println(count);
					if (count == 0) // �ߺ��ȵǼ� ���԰���
					{
						pw.println(Protocol.IDSEARCHCHECK_OK + "|" + "MESSAGE");
						pw.flush();
					} else {
						pw.println(Protocol.IDSEARCHCHECK_NO + "|" + "MESSAGE");
						pw.flush();
					}
				} else if (line[0].compareTo(Protocol.IDSEARCH) == 0) // ID ã��
				{
					System.out.println("IDã��");
					String userContent[] = line[1].split("%");

					System.out.println(userContent[0]);
					System.out.println(userContent[1]);
					System.out.println(userContent[2]);
					System.out.println(userContent[3]);

					String sql = "select * from UserContent where (NAME = '" + userContent[0] + "' and age = '"
							+ userContent[1] + "' and email ='" + userContent[2] + "' and phoneNumber1 = '"
							+ userContent[3] + "')";

					pstmt = conn.prepareStatement(sql);
					ResultSet rs = pstmt.executeQuery(sql);
					String name = null;
					String id = null;
					int count = 0;
					while (rs.next()) {
						name = rs.getString("NAME");
						id = rs.getString("IDNAME");
						if (name.compareTo(userContent[0]) == 0) {
							count++;
						}
					}
					System.out.println(count);

					if (count == 0) // ID�� ����
					{
						pw.println(Protocol.IDSEARCH_NO + "|" + "��ϵ� ���̵� �����ϴ�.");
						pw.flush();
					} else { // ������ID ã��
						StringBuffer stb = new StringBuffer(id);
						stb.replace(stb.length() - 4, stb.length() - 1, "***");
						pw.println(Protocol.IDSEARCH_OK + "|" + "ID : " + stb.toString());
						pw.flush();
					}

				} else if (line[0].compareTo(Protocol.ENTERLOGIN) == 0) // login
				{
					System.out.println("login");
					String userContent[] = line[1].split("%");

					System.out.println(userContent[0] + "/" + userContent[1]);

//					String sql = "select * from UserContent where (IDNAME = '" + userContent[0] + "' and PASSWORD = '"
//							+ userContent[1] + "')";

					String sql = "select * from UserContent where idname = '" + userContent[0] + "' and password = '"
							+ userContent[1] + "'";

					pstmt = conn.prepareStatement(sql);
					ResultSet rs = pstmt.executeQuery(sql);
					int count = 0;

					while (rs.next()) {
						System.out.println("While");
						user.setName(rs.getString("NAME"));
						user.setIdName(rs.getString("IDNAME"));
						user.setAge(rs.getString("AGE"));
						user.setPassword(rs.getString("PASSWORD"));
						user.setPryNumber(rs.getInt("priNumber"));
						user.setPhoneNumber(rs.getString("phoneNumber1"));
						user.setEmail(rs.getString("email"));

						count++;
					}

					System.out.println(count);

					if (count == 0) // ID,PW Ʋ����
					{
						pw.println(Protocol.ENTERLOGIN_NO + "|" + "�α��ο� �����Ͽ����ϴ�");
						pw.flush();

						user.setName("");
						user.setIdName("");
						user.setAge("");
						user.setPassword("");
						user.setPryNumber(0);
						user.setPhoneNumber("");
						user.setEmail("");

					} else { // ������ID ã��
						pw.println(Protocol.ENTERLOGIN_OK + "|" + "�α��� ����");
						pw.flush();
					}
					
					System.out.println(user.toString());

				}

			} // while

			br.close();
			pw.close();
			socket.close();

		} catch (IOException io) {
			io.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
