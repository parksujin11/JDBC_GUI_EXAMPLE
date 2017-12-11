package test.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import test.dto.FriendsDto;
import test.util.DBConnect;

/*
 *  Dao => Data Access Object �� ����
 *  
 *  - ȸ�� ������ ���ؼ� SELECT , INSERT, UPDATE, DELETE 
 *    �۾��� ������ ��ü�� �����ϱ����� Ŭ���� �����ϱ�
 *  
 *  - Application �������� MemberDao ��ü�� ���� 1���� 
 *    �����ɼ� �ֵ��� �����Ѵ�.
 */
public class FriendsDao {
	//1. �ڽ��� �������� ���� private static �ʵ� �����
	private static FriendsDao dao;
	//2. �ܺο��� ��ü �����Ҽ� ������ �������� ���������ڸ�
	//   private �� ���� 
	private FriendsDao() {}
	//3. �ڽ��� �������� �������ִ� static �ɹ� �޼ҵ� ����� 
	public static FriendsDao getInstance() {
		if(dao==null) {
			dao=new FriendsDao();
		}
		return dao;
	}
	
	// DB �� ȸ�������� �����ϴ� �޼ҵ�
	public boolean insert(FriendsDto dto) {
		Connection conn=null;
		PreparedStatement pstmt=null;
		//�۾��� �������θ� ���� ���� 
		boolean isSuccess=false;
		try {
			conn=new DBConnect().getConn();
			//������ sql ��
			String sql="INSERT INTO member (num,name,phone,regdate) "
					+ "VALUES(friend_seq.NEXTVAL,?,?,?)";
			pstmt=conn.prepareStatement(sql);
			// ? �� �� ���ε��ϱ� 
			pstmt.setString(1, dto.getName());
			pstmt.setString(2, dto.getPhone());
			pstmt.setString(3, dto.getRegdate());
			pstmt.setInt(4, dto.getNum());
			
			// sql �� �����ϰ� �߰��� row �� ���� ������
			int flag=pstmt.executeUpdate();
			if(flag>0) {//�۾� �����̸�
				isSuccess=true;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(conn!=null)conn.close();
				if(pstmt!=null)pstmt.close();
			}catch(Exception e) {}
		}
		//���� ���θ� ���� 
		return isSuccess;
	}
	// DB �� ȸ�������� �����ϴ� �޼ҵ�
	public boolean delete(int num) {
		Connection conn=null;
		PreparedStatement pstmt=null;
		//�۾��� �������θ� ���� ���� 
		boolean isSuccess=false;
		try {
			conn=new DBConnect().getConn();
			//������ sql ��
			String sql="DELETE FROM member WHERE num=?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			// sql �� �����ϰ� �߰��� row �� ���� ������
			int flag=pstmt.executeUpdate();
			if(flag>0) {//�۾� �����̸�
				isSuccess=true;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(conn!=null)conn.close();
				if(pstmt!=null)pstmt.close();
			}catch(Exception e) {}
		}
		//���� ���θ� ���� 
		return isSuccess;
	}
	// DB �� ȸ�������� �����ϴ� �޼ҵ�
	public boolean update(FriendsDto dto) {
		Connection conn=null;
		PreparedStatement pstmt=null;
		//�۾��� �������θ� ���� ���� 
		boolean isSuccess=false;
		try {
			conn=new DBConnect().getConn();
			//������ sql ��
			String sql="UPDATE member SET name=?,"
					+ "WHERE num=?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, dto.getName());
			pstmt.setString(2, dto.getPhone());
			pstmt.setInt(3, dto.getNum());
			pstmt.setString(4, dto.getRegdate());
			// sql �� �����ϰ� �߰��� row �� ���� ������
			int flag=pstmt.executeUpdate();
			if(flag>0) {//�۾� �����̸�
				isSuccess=true;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(conn!=null)conn.close();
				if(pstmt!=null)pstmt.close();
			}catch(Exception e) {}
		}
		//���� ���θ� ���� 
		return isSuccess;
	}
	// DB �� ȸ�� ����� �������ִ� �޼ҵ�
	public List<FriendsDto> getList(){
		Connection conn=null;
		//�ʿ��� ��ü�� ���� ���� ����� 
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		//MemberDto ��ü�� ���� ArrayList ��ü����
		List<FriendsDto> list=new ArrayList<>();
		try{
			conn=new DBConnect().getConn();
			//������ sql �� �غ� 
			String sql="SELECT num, name, addr FROM member";
			//PreparedStatement ��ü�� ������ ������
			//(sql ���� ��� �������� ��ü)
			pstmt=conn.prepareStatement(sql);
			//ResultSet ��ü�� ������ ������ 
			//(SELECT ���� ���� ��� ���� ������ �ִ� ��ü)
			rs=pstmt.executeQuery();
			//�ݺ��� ���鼭 cursor �� ��ĭ�� ������. 
			while(rs.next()){
				//���� cursor �� ��ġ�� ���� 
				//row ���� ���ϴ� Į���� �����͸� ���´�.
				int  num=rs.getInt("num");
				String name=rs.getString("name");
				String phone=rs.getString("phone");
				String regdate=rs.getString("regdate");
				//MemberDto ��ü�� ȸ������ ���
				FriendsDto dto=new FriendsDto(num, name, phone, regdate);
				//ArrayList ��ü�� MemberDto ��� 
				list.add(dto);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(rs!=null)rs.close();
				if(pstmt!=null)pstmt.close();
				if(conn!=null)conn.close();
			}catch(Exception e){}
		}//try
		
		return list;
	}
	//���ڷ� ���޵Ǵ� ��ȣ�� �ش��ϴ� ȸ�������� �������ִ� �޼ҵ� 
	public FriendsDto getData(int num) {
		Connection conn=null;
		//�ʿ��� ��ü�� ���� ���� ����� 
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		//MemberDto ��ü�� �������� ���� ���� ����� 
		FriendsDto dto=null;
		try{
			conn=new DBConnect().getConn();
			//������ sql �� �غ� 
			String sql="SELECT name, phone, regdate FROM friends WHERE num=?";
			//PreparedStatement ��ü�� ������ ������
			//(sql ���� ��� �������� ��ü)
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			//ResultSet ��ü�� ������ ������ 
			//(SELECT ���� ���� ��� ���� ������ �ִ� ��ü)
			rs=pstmt.executeQuery();
			//�ݺ��� ���鼭 cursor �� ��ĭ�� ������. 
			if(rs.next()){
				int num1=rs.getInt("num");
				String name=rs.getString("name");
				String phone=rs.getString("phone");
				String regdate=rs.getString("regdate");
				
				dto=new FriendsDto();
				dto.setNum(num);
				dto.setName(name);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(rs!=null)rs.close();
				if(pstmt!=null)pstmt.close();
				if(conn!=null)conn.close();
			}catch(Exception e){}
		}//try
		return null;
	}
}
