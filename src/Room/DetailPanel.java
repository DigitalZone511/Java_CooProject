package Room;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DetailPanel extends JPanel {

	public static String labelName[] = { "�� ��ȣ :", "      ", "�� ���� :", "      ", "�ο� �� : ", "      ", "�� ���� : ",
			"      ", "      " };
	private JLabel labelArray[];
	private JButton enterButton;

	public DetailPanel() {
		this.setLayout(new GridLayout(5, 2, 1, 1));

		labelArray = new JLabel[labelName.length];

		for (int i = 0; i < labelName.length; i++) {
			labelArray[i] = new JLabel(labelName[i]);
			this.add(labelArray[i]);
		}

		enterButton = new JButton("�� ��");
		this.add(enterButton);

	}

}