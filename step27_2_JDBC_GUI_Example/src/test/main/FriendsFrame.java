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
	
	String sql=" TO_CHAR(regdate, 'YYYY\"년 \"MM\"월 \"')";
	
	public FriendsFrame() {
		initUI();
		
	}

	public void initUI() {
		setLayout(new BorderLayout());
		
		JPanel topPanel=new JPanel();
		
		JLabel label1=new JLabel("번호");
		JLabel label2=new JLabel("이름");
		JLabel label3=new JLabel("핸드폰");
		JLabel label4=new JLabel("날짜");
		
		inputNum=new JTextField(20);
		inputName=new JTextField(20);
		inputPhone=new JTextField(20);
		inputRegdate=new JTextField(20);
		
		saveBtn=new JButton("저장");
		deleteBtn=new JButton("삭제");
		updateBtn=new JButton("수정");
		
		saveBtn.addActionListener(this);
		deleteBtn.addActionListener(this);
		updateBtn.addActionListener(this);
		
		saveBtn.setActionCommand("save");
		deleteBtn.setActionCommand("delete");
		updateBtn.setActionCommand("update");
		
		//번호는 입력이나 수정을 할 수 없도록 설저
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
		
		String[] colNames= {"번호", "이름", "핸드폰", "날짜"};
		//기본 테이블 모델 객체 생성
		model=new DefaultTableModel(colNames, 0);
		//JTable 객체 생성
		table=new JTable();
		//테이블에 모델 연결
		table.setModel(model);
		//스크롤 가능한 패널 객체
		JScrollPane sPanel=new JScrollPane(table);
		//테이블을 가운데 배치
		add(sPanel, BorderLayout.CENTER);
		
		//프레임의 위치와 크기 설정
				setBounds(200, 200, 200, 200);
				setVisible(true);
				setDefaultCloseOperation(EXIT_ON_CLOSE);
				//회원정보 출력
				displayFriends();	
			}
				//메인 메소드
				public static void main(String[] args) {
					new FriendsFrame();
				}
				//ActionListener 인터페이스 ㄸㅐ문에 구현한 메소드 
				@Override
				public void actionPerformed(ActionEvent ae) {
					//이벤트가 일어난 버튼의 action command를 읽어온다.
					String command=ae.getActionCommand();
					
					if(command.equals("save")) {
					//입력한 이름과 주소 읽어오기
						String name=inputName.getText();
						String phone=inputPhone.getText();
						//MemberDto 에 담는다.
						FriendsDto dto=new FriendsDto();
						dto.setName(name);
						dto.setPhone(phone);
						//MemberDao 를 이용해서 저장
						FriendsDao dao=FriendsDao.getInstance();
						boolean isSuccess=dao.insert(dto);
						if(isSuccess) {
							JOptionPane.showMessageDialog(this, "저장");
						}else {
							JOptionPane.showMessageDialog(this, "실패");
						}
						
					}else if(command.equals("delete")) {//삭제 버튼을 눌렀을때 
						//예, 아니요, 취소 중에 어떤 버튼을 눌렀는지 정보가 int type 으로 리턴된다.
						int result=JOptionPane.showConfirmDialog(this,"삭제하시겠습니가?");
						//예 버튼을 누르지 않았다면
						if(result !=JOptionPane.YES_OPTION) {
							return; //메소드 종료 
						}
						//선택된 row 의 인덱스를 읽어온다.
						int selectedIndex=table.getSelectedRow();
						if(selectedIndex==-1) {
							JOptionPane.showMessageDialog(this, "삭제할 row를 선택해주세요");
							return;
						}
						//삭제할 row 에 있는 회원 번호를 읽어온다.
						int num=(int)table.getValueAt(selectedIndex, 0);
						//DB에서 해당 회원정보를 삭제한다.
						FriendsDao dao=FriendsDao.getInstance();
						dao.delete(num);
						
					}else if(command.equals("update")) {
						int selectedIndex=table.getSelectedRow();
						if(selectedIndex==-1) {
							JOptionPane.showMessageDialog(this, "수정할 row를 선택해주세요");
							return;
						}
						//수정할 회원정보를 읽어와서 
						int num=(int)table.getValueAt(selectedIndex, 0);
						String name=(String)table.getValueAt(selectedIndex, 1);
						String phone=(String)table.getValueAt(selectedIndex, 2);
						String regdate=(String)table.getValueAt(selectedIndex, 3);
						//MemberDto 객체에 담고
						FriendsDto dto=new FriendsDto(num, name, phone, regdate);
						//DB에 수정 반영
						FriendsDao.getInstance().update(dto);
						JOptionPane.showMessageDialog(this, "수정하였습니다.");
						
					}
					//회원 정보 다시 출력
					displayFriends();
					
				}// actionPerformed()
				
				//DB 에 있는 회원 정보를 JTable 에 출력하는 메소드 
				public void displayFriends() {
					//회원 정보를 읽어온다.
					FriendsDao dao=FriendsDao.getInstance();
					List<FriendsDto> list=dao.getList();
					//테이블의 내용을 지우고 
					model.setRowCount(0);
					//다시 출력
					for(FriendsDto tmp:list) {
						//row 데이터 구성 
						Object[] rowData= {tmp.getNum(), tmp.getName(), tmp.getPhone(), tmp.getRegdate()};
						model.addRow(rowData);//row 추가
					}
					
					
			}
		
	}
	
