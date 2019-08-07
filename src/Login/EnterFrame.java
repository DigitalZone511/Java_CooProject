package Login;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Action.Protocol;
import FunctionTest.Email.SendMail;
import Room.RoomFrame;

public class EnterFrame extends JFrame implements ActionListener, Runnable {
	private JTextField idT, pwT;
	private JButton idB, pwB, accessB, searchidB, searchpwB, membershipB;
	private JLabel loginL, logoutL;
	private ImageIcon loginC, logoutC, modifiedC;
	private Socket socket;
	private BufferedReader br;
	private PrintWriter pw;

	MembershipB menbersShipF; // ȸ������
	SearchidB searchF; // ID ã��
	SearchpwB searchpwF; // PASSWORD ã��
	RoomFrame RoomF; // ����

	private String sNumber = "><^^"; // default ��ũ���ѹ�
	private boolean condition_S = false; // ����Ȯ��

	public EnterFrame() {

		menbersShipF = new MembershipB();
		searchF = new SearchidB();
		searchpwF = new SearchpwB();
		RoomF = new RoomFrame();

		idB = new JButton("���̵�");
		idT = new JTextField(15);
		pwB = new JButton("�н�����");
		pwT = new JTextField(15);

		JPanel p2 = new JPanel(new FlowLayout());
		p2.add(idB);
		p2.add(idT);
		p2.add(pwB);
		p2.add(pwT);

		searchidB = new JButton("���̵� ã��");
		searchpwB = new JButton("��й�ȣ ã��");
		membershipB = new JButton("ȸ������");
		accessB = new JButton("����");

		JPanel p3 = new JPanel();
		p3.add(searchidB);
		p3.add(searchpwB);
		p3.add(membershipB);
		p3.add(accessB);

		loginC = new ImageIcon("img/�α���.png");
		loginL = new JLabel(loginC);

		JPanel p4 = new JPanel();
		p4.add(loginL);

		Container contentPane = this.getContentPane();
		contentPane.add("Center", p2);
		contentPane.add("South", p3);
		contentPane.add("East", p4);

		setVisible(true);
		setResizable(false);
		setBounds(400, 200, 1000, 800);
//		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		event();

	}

	public void event() {
		// --------------------ȸ�����԰���----------------------------------
		membershipB.addActionListener(this); // ȸ������(��ư)
		menbersShipF.calneB.addActionListener(this); // ȸ������ ���(�α���ȭ������)
		menbersShipF.joinB.addActionListener(this); // ȸ������ ȭ�鿡�� join
		menbersShipF.idoverlapB.addActionListener(this);// ȸ������ ȭ�� �ߺ�Ȯ��
		menbersShipF.emailB.addActionListener(this);// ȸ������ �̸��� ����
		menbersShipF.emeilokB.addActionListener(this); // �̸��� ����Ȯ��

		// --------------------IDã�����----------------------------------
		searchidB.addActionListener(this); // ���̵� ã��
		searchF.joinB.addActionListener(this); // ���̵�ã�� (join��ư)
		searchF.emailB.addActionListener(this); // ���̵�ã�� (Email ��������)
		searchF.emeilokB.addActionListener(this); // ���̵�ã��(Email ����Ȯ��)
		searchF.cancelB.addActionListener(this); // IDã�� ���

		// --------------------PWã�����----------------------------------
		searchpwB.addActionListener(this); // PW ã��
		searchpwF.cancleB.addActionListener(this); // PWã�� ���

		// --------------------�α��ΰ���----------------------------------
		accessB.addActionListener(this); // ����(Login)
		RoomF.exitB.addActionListener(this); // Room -> �α���Page

	}

	public void network() {

		// ���� ����
		try {
			socket = new Socket("192.168.0.43", 9500);
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

		} catch (UnknownHostException e) {
			System.out.println("������ ã�� �� �����ϴ�");
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			System.out.println("������ ������ �ȵǾ����ϴ�");
			e.printStackTrace();
			System.exit(0);
		}

		// �̺�Ʈ

		// ������ ����
		Thread t = new Thread(this);
		t.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == membershipB) { // ���������� -----------> ȸ�����Թ�ư
			this.setVisible(false);
			menbersShipF.setVisible(true);
		} else if (e.getSource() == menbersShipF.joinB) { // ȸ������������ -----------> ���Թ�ư

			String name = menbersShipF.nameT.getText();
			String id = menbersShipF.idT.getText();
			String pw1 = menbersShipF.pwT.getText();
			String ageYear = (String) menbersShipF.ageYearC.getSelectedItem();
			String ageMonth = (String) menbersShipF.ageMonthC.getSelectedItem();
			String ageDay = (String) menbersShipF.ageDayC.getSelectedItem();
			String tel = (String) menbersShipF.telC.getSelectedItem();
			String tel2 = menbersShipF.tel2T.getText();
			String tel3 = menbersShipF.tel3T.getText();
			String email = menbersShipF.emailT.getText();
			String email1 = (String) menbersShipF.emailC.getSelectedItem();
			String emailok = menbersShipF.emailadductionT.getText();

			if (name.length() == 0 || id.length() == 0 || pw1.length() == 0 || tel2.length() == 0 || tel3.length() == 0
					|| email.length() == 0 || emailok.length() == 0) {
				JOptionPane.showMessageDialog(this, "���� �Է����ּ���");
			} else if (condition_S) { // -> ������ �Ȱ�

				String line = "";
				line += (menbersShipF.idT.getText() + "%" + menbersShipF.pwT.getText() + "%"
						+ menbersShipF.nameT.getText() + "%" + menbersShipF.ageYearC.getSelectedItem()
						+ menbersShipF.ageMonthC.getSelectedItem() + menbersShipF.ageDayC.getSelectedItem() + "%"
						+ menbersShipF.emailT.getText() + "@" + menbersShipF.emailC.getSelectedItem()) + "%"
						+ menbersShipF.telC.getSelectedItem() + "" + menbersShipF.tel2T.getText()
						+ menbersShipF.tel3T.getText();
				System.out.println(line);

				pw.println(Protocol.REGISTER + "|" + line);
				pw.flush();
				JOptionPane.showMessageDialog(this, "ȸ������ �Ϸ�");
				menbersShipF.setVisible(false);
				this.setVisible(true);

				menbersShipF.nameT.setText("");
				menbersShipF.idT.setText("");
				menbersShipF.pwT.setText("");
				menbersShipF.ageYearC.setSelectedIndex(0);
				menbersShipF.ageMonthC.setSelectedIndex(0);
				menbersShipF.ageDayC.setSelectedIndex(0);
				menbersShipF.telC.setSelectedIndex(0);
				menbersShipF.tel2T.setText("");
				menbersShipF.tel3T.setText("");
				menbersShipF.emailT.setText("");
				menbersShipF.emailC.setSelectedIndex(0);
				menbersShipF.emailadductionT.setText("");

				condition_S = false;
				sNumber = "><^^";

			}

		} else if (e.getSource() == menbersShipF.calneB) { // ȸ������������ -----------> ���
			menbersShipF.setVisible(false);
			this.setVisible(true);
			condition_S = false;
			sNumber = "><^^";

		} else if (e.getSource() == menbersShipF.idoverlapB) { // ȸ������ ������ID -----------> �ߺ�Ȯ��
			pw.println(Protocol.IDSEARCHCHECK + "|" + menbersShipF.idT.getText());
			pw.flush();
		} else if (e.getSource() == menbersShipF.emailB) // ȸ������ ������ -----------> ������ȣ ����
		{
			if (menbersShipF.emailT.getText().length() == 0) {
				JOptionPane.showMessageDialog(this, "�̸��� �Է��ϼ���");
			} else {
				String emailString = menbersShipF.emailT.getText() + "@"
						+ (String) menbersShipF.emailC.getSelectedItem();
				System.out.println(emailString);
				sNumber = String.valueOf(SendMail.SendMail(emailString));
			}

		} else if (e.getSource() == menbersShipF.emeilokB) { // ȸ������ ������ -----------> ������ȣȮ��
			if (sNumber.compareTo(menbersShipF.emailadductionT.getText()) == 0) {
				JOptionPane.showMessageDialog(this, "�����Ǿ����ϴ�");
				condition_S = true;
			} else {
				JOptionPane.showMessageDialog(this, "������ȣ�� Ʋ�Ƚ��ϴ�");
			}
		} else if (e.getSource() == searchpwB) { // ���������� -----------> ���ã�� ��ư
			this.setVisible(false);
			searchpwF.setVisible(true);
		} else if (e.getSource() == searchidB) { // ���������� -----------> ���̵� ã��
			this.setVisible(false);
			searchF.setVisible(true);
		} else if (e.getSource() == searchF.joinB) { // ID ã�� -----------> Ȯ��
			String name = searchF.nameT.getText();
			String ageYear = (String) searchF.ageYearC.getSelectedItem();
			String ageMonth = (String) searchF.ageMonthC.getSelectedItem();
			String ageDay = (String) searchF.ageDayC.getSelectedItem();
			String tel2 = searchF.tel2T.getText();
			String tel3 = searchF.tel3T.getText();
			String email = searchF.emailT.getText();
			String email1 = (String) searchF.emailC.getSelectedItem();
			String emailok = searchF.emailadductionT.getText();

			if (name.length() == 0 || tel2.length() == 0 || tel3.length() == 0 || email.length() == 0
					|| emailok.length() == 0) {
				JOptionPane.showMessageDialog(this, "��ĭ�� �Է����ּ���");
			} else if (condition_S) {
				String line = "";

				line += (searchF.nameT.getText() + "%" + searchF.ageYearC.getSelectedItem()
						+ searchF.ageMonthC.getSelectedItem() + searchF.ageDayC.getSelectedItem() + "%"
						+ searchF.emailT.getText() + "@" + searchF.emailC.getSelectedItem()) + "%"
						+ searchF.telC.getSelectedItem() + "" + searchF.tel2T.getText() + searchF.tel3T.getText();
				System.out.println(line);

				pw.println(Protocol.IDSEARCH + "|" + line);
				pw.flush();

				searchF.nameT.setText("");
				searchF.ageYearC.setSelectedIndex(0);
				searchF.ageMonthC.setSelectedIndex(0);
				searchF.ageDayC.setSelectedIndex(0);
				searchF.telC.setSelectedIndex(0);
				searchF.tel2T.setText("");
				searchF.tel3T.setText("");
				searchF.emailT.setText("");
				searchF.emailC.setSelectedIndex(0);
				searchF.emailadductionT.setText("");
				searchF.emailadductionT.setText("");
				condition_S = false;
				sNumber = "><^^";

			}

		} else if (e.getSource() == searchF.emeilokB) // IDã�������� -----------> ������ȣ Ȯ��
		{
			if (sNumber.compareTo(searchF.emailadductionT.getText()) == 0) {
				JOptionPane.showMessageDialog(this, "�����Ǿ����ϴ�");
				condition_S = true;
			} else {
				JOptionPane.showMessageDialog(this, "������ȣ�� Ʋ�Ƚ��ϴ�");
			}
		} else if (e.getSource() == searchF.emailB) // IDã�� ������ -----------> ������ȣ ����
		{
			if (searchF.emailT.getText().length() == 0) {
				JOptionPane.showMessageDialog(this, "�̸��� �Է��ϼ���");
			} else {
				String emailString = searchF.emailT.getText() + "@" + (String) searchF.emailC.getSelectedItem();
				System.out.println(emailString);
				sNumber = String.valueOf(SendMail.SendMail(emailString));
			}
		} else if (e.getSource() == searchF.cancelB) { // IDã�������� -----------> IDã�� ���

			searchF.setVisible(false);
			this.setVisible(true);
			searchF.nameT.setText("");
			searchF.ageYearC.setSelectedIndex(0);
			searchF.ageMonthC.setSelectedIndex(0);
			searchF.ageDayC.setSelectedIndex(0);
			searchF.telC.setSelectedIndex(0);
			searchF.tel2T.setText("");
			searchF.tel3T.setText("");
			searchF.emailT.setText("");
			searchF.emailC.setSelectedIndex(0);
			searchF.emailadductionT.setText("");
			searchF.emailadductionT.setText("");
			condition_S = false;
			sNumber = "><^^";

		} else if (e.getSource() == accessB) { // ���������� --> ���� (Login)

			String id = idT.getText();
			String pwss = pwT.getText();

			if (id.length() == 0 || pwss.length() == 0) {
				JOptionPane.showMessageDialog(this, "��ĭ�� �Է����ּ���");
			} else {
				String line = id + "%" + pwss;
				pw.println(Protocol.ENTERLOGIN + "|" + line);
				pw.flush();
			}
			idT.setText("");
			pwT.setText("");

		} else if (e.getSource() == searchpwF.cancleB) { // PWã�������� -->PW ã�� ���
			searchpwF.setVisible(false);
			this.setVisible(true);
		} else if (e.getSource() == RoomF.exitB) { // ���� -> �α���Page
			RoomF.setVisible(false);
			this.setVisible(true);
		}
	}

	@Override
	public void run() {
		// �޴���
		String line[] = null;
		while (true) {
			try {
				line = br.readLine().split("\\|");
				if (line == null) {
					br.close();
					pw.close();
					socket.close();

					System.exit(0);
				} else if (line[0].compareTo(Protocol.IDSEARCHCHECK_OK) == 0) { // ȸ������ ID �ߺ� �ȵ�
					JOptionPane.showMessageDialog(this, "��밡��");
				} else if (line[0].compareTo(Protocol.IDSEARCHCHECK_NO) == 0) { // ȸ������ ID �ߺ� ��
					JOptionPane.showMessageDialog(this, "��� �Ұ���");
				} else if (line[0].compareTo(Protocol.IDSEARCH_OK) == 0) // ID ã�� ������ ����
				{
					JOptionPane.showMessageDialog(this, line[1]);
					searchF.setVisible(false);
					this.setVisible(true);
				} else if (line[0].compareTo(Protocol.IDSEARCH_NO) == 0) // ID�� ����
				{
					JOptionPane.showMessageDialog(this, line[1]);
					searchF.setVisible(false);
					this.setVisible(true);
				} else if (line[0].compareTo(Protocol.ENTERLOGIN_OK) == 0) // �α��� ����
				{
					JOptionPane.showMessageDialog(this, line[1]);
					this.setVisible(false);
					RoomF.setVisible(true);
					System.out.println("�α��� ����");

				} else if (line[0].compareTo(Protocol.ENTERLOGIN_NO) == 0) // �α��� ����
				{
					JOptionPane.showMessageDialog(this, line[1]);
					System.out.println("�α��ν���");
				}

			} catch (IOException io) {
				io.printStackTrace();
			}

//			output.append(line + "\n");
//			int pos = output.getText().length();
//			output.setCaretPosition(pos);
		} // while
	}
}
