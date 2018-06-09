package ServerPackage;

public class Teacher {
	String recordID;
	String firstName;
	String lastName;
	String address;
	String phone;
	String specialization;
	ServerNameEnum location;
	
	public Teacher(String recordID,String firstName,String lastName,String address,String phone, String specialization, String location)
	{
		this.recordID=recordID;
		this.firstName=firstName;
		this.lastName=lastName;
		this.address=address;
		this.phone=phone;
		this.specialization=specialization;
		try{
			this.location=ServerNameEnum.valueOf(location);	
		}
		catch(IllegalArgumentException ex){
			System.out.println(ErrorMessages.locationError);
			ex.printStackTrace();
		}
	}
}

