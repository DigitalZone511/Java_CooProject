package Action;

public class Protocol {
	public static final String REGISTER = "100"; //ȸ������(request)
	
	public static final String IDSEARCH = "110"; //IDã�� Join (request)
	
	public static final String IDSEARCH_OK = "111"; //IDã�� (������ �ִ°�) (ACK)
	
	public static final String IDSEARCH_NO = "112"; //IDã�� (������ ����) (NACK)
	
	public static final String IDSEARCHCHECK = "113"; //(using ȸ������)IDã�� �ߺ�Ȯ��(request)
	
	public static final String IDSEARCHCHECK_OK = "114"; //(using ȸ������)ID �ߺ�Ȯ�� (��밡��) (ACK)
	
	public static final String IDSEARCHCHECK_NO = "115"; //(using ȸ������)ID �ߺ�Ȯ�� (��� �Ұ���) (NACK)
	
	public static final String ENTERLOGIN = "120"; // �α���(request)
	
	public static final String ENTERLOGIN_OK = "121"; // �α��� ����(ACK)
	
	public static final String ENTERLOGIN_NO = "122"; //�α��� ����(NACK)
	
	public static final String PWSEARCH = "130"; //PWã��
	
	public static final String ROOMMAKE = "200"; //�游���
	
	public static final String ROOMSORT = "210"; //������
	
	public static final String EXITWAITROOM = "220"; // ���� ������
	
	public static final String ENTERWAITROOM = "230"; // ���� ����
	
	public static final String SENDWAITROOM = "250"; //���� �޼���
	
	public static final String ENTERROOM = "300"; //������
	
	public static final String EXITROOM = "310"; //�泪����
	
	public static final String SENDMESSAGE = "400"; //�޼��� ������
}
