package test.dto;

public class FriendsDto {
	private int num;
	private String name;
	private String phone;
	private String regdate;
	
	public FriendsDto() {
		
	}

	public FriendsDto(int num, String name, String phone, String regdate) {
		super();
		this.num = num;
		this.name = name;
		this.phone = phone;
		this.regdate = regdate;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRegdate() {
		return regdate;
	}

	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}
	
	
}
