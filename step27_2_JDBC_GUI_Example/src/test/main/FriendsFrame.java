package test.main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import test.dao.FriendsDao;
import test.dto.FriendsDto;

public class FriendsFrame extends JFrame implements ActionListener{
	JTextField inputNum, inputName, inputPhone, inputRegdate;
	JButton saveBtn, deleteBtn, updateBtn;
	
	DefaultTableModel model;
	
	JTable table;
	
	String sql=" TO_CHAR(regdate, 'YYYY\"�� \"MM\"�� \"')";
	
	public FriendsFrame() {
		initUI();
		
	}

	public void initUI() {
		setLayout(new BorderLayout());
		
		JPanel topPanel=new JPanel();
		
		JLabel label1=new JLabel("��ȣ");
		JLabel label2=new JLabel("�̸�");
		JLabel label3=new JLabel("�ڵ���");
		JLabel label4=new JLabel("��¥");
		
		inputNum=new JTextField(20);
		inputName=new JTextField(20);
		inputPhone=new JTextField(20);
		inputRegdate=new JTextField(20);
		
		saveBtn=new JButton("����");
		deleteBtn=new JButton("����");
		updateBtn=new JButton("����");
		
		saveBtn.addActionListener(this);
		deleteBtn.addActionListener(this);
		updateBtn.addActionListener(this);
		
		saveBtn.setActionCommand("save");
		deleteBtn.setActionCommand("delete");
		updateBtn.setActionCommand("update");
		
		//��ȣ�� �Է��̳� ������ �� �� ������ ����
		inputNum.setEditable(false);
		inputRegdate.setEditable(false);
		
		

		topPanel.add(label1);
		topPanel.add(inputNum);
		topPanel.add(label2);
		topPanel.add(inputName);
		topPanel.add(label3);
		topPanel.add(inputPhone);
		topPanel.add(label4);
		topPanel.add(inputRegdate);
		topPanel.add(saveBtn);
		topPanel.add(deleteBtn);
		topPanel.add(updateBtn);
		
		add(topPanel, BorderLayout.NORTH);
		
		String[] colNames= {"��ȣ", "�̸�", "�ڵ���", "��¥"};
		//�⺻ ���̺� �� ��ü ����
		model=new DefaultTableModel(colNames, 0);
		//JTable ��ü ����
		table=new JTable();
		//���̺� �� ����
		table.setModel(model);
		//��ũ�� ������ �г� ��ü
		JScrollPane sPanel=new JScrollPane(table);
		//���̺��� ��� ��ġ
		add(sPanel, BorderLayout.CENTER);
		
		//�������� ��ġ�� ũ�� ����
				setBounds(200, 200, 200, 200);
				setVisible(true);
				setDefaultCloseOperation(EXIT_ON_CLOSE);
				//ȸ������ ���
				displayFriends();	
			}
				//���� �޼ҵ�
				public static void main(String[] args) {
					new FriendsFrame();
				}
				//ActionListener �������̽� �������� ������ �޼ҵ� 
				@Override
				public void actionPerformed(ActionEvent ae) {
					//�̺�Ʈ�� �Ͼ ��ư�� action command�� �о�´�.
					String command=ae.getActionCommand();
					
					if(command.equals("save")) {
					//�Է��� �̸��� �ּ� �о����
						String name=inputName.getText();
						String phone=inputPhone.getText();
						//MemberDto �� ��´�.
						FriendsDto dto=new FriendsDto();
						dto.setName(name);
						dto.setPhone(phone);
						//MemberDao �� �̿��ؼ� ����
						FriendsDao dao=FriendsDao.getInstance();
						boolean isSuccess=dao.insert(dto);
						if(isSuccess) {
							JOptionPane.showMessageDialog(this, "����");
						}else {
							JOptionPane.showMessageDialog(this, "����");
						}
						
					}else if(command.equals("delete")) {//���� ��ư�� �������� 
						//��, �ƴϿ�, ��� �߿� � ��ư�� �������� ������ int type ���� ���ϵȴ�.
						int result=JOptionPane.showConfirmDialog(this,"�����Ͻðڽ��ϰ�?");
						//�� ��ư�� ������ �ʾҴٸ�
						if(result !=JOptionPane.YES_OPTION) {
							return; //�޼ҵ� ���� 
						}
						//���õ� row �� �ε����� �о�´�.
						int selectedIndex=table.getSelectedRow();
						if(selectedIndex==-1) {
							JOptionPane.showMessageDialog(this, "������ row�� �������ּ���");
							return;
						}
						//������ row �� �ִ� ȸ�� ��ȣ�� �о�´�.
						int num=(int)table.getValueAt(selectedIndex, 0);
						//DB���� �ش� ȸ�������� �����Ѵ�.
						FriendsDao dao=FriendsDao.getInstance();
						dao.delete(num);
						
					}else if(command.equals("update")) {
						int selectedIndex=table.getSelectedRow();
						if(selectedIndex==-1) {
							JOptionPane.showMessageDialog(this, "������ row�� �������ּ���");
							return;
						}
						//������ ȸ�������� �о�ͼ� 
						int num=(int)table.getValueAt(selectedIndex, 0);
						String name=(String)table.getValueAt(selectedIndex, 1);
						String phone=(String)table.getValueAt(selectedIndex, 2);
						String regdate=(String)table.getValueAt(selectedIndex, 3);
						//MemberDto ��ü�� ���
						FriendsDto dto=new FriendsDto(num, name, phone, regdate);
						//DB�� ���� �ݿ�
						FriendsDao.getInstance().update(dto);
						JOptionPane.showMessageDialog(this, "�����Ͽ����ϴ�.");
						
					}
					//ȸ�� ���� �ٽ� ���
					displayFriends();
					
				}// actionPerformed()
				
				//DB �� �ִ� ȸ�� ������ JTable �� ����ϴ� �޼ҵ� 
				public void displayFriends() {
					//ȸ�� ������ �о�´�.
					FriendsDao dao=FriendsDao.getInstance();
					List<FriendsDto> list=dao.getList();
					//���̺��� ������ ����� 
					model.setRowCount(0);
					//�ٽ� ���
					for(FriendsDto tmp:list) {
						//row ������ ���� 
						Object[] rowData= {tmp.getNum(), tmp.getName(), tmp.getPhone(), tmp.getRegdate()};
						model.addRow(rowData);//row �߰�
					}
					
					
			}
		
	}
	
