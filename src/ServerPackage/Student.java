package ServerPackage;
import java.util.*;
public class Student {
	String recordID;
	String firstName;
	String lastName;
	List<String> coursesRegistered=new ArrayList<String>();
	StatusEnum status;
	String statusDate;

	public Student(String recordID,String firstName,String lastName,List<String> coursesRegistered,StatusEnum status, String statusDate)
	{
		this.recordID=recordID;
		this.firstName=firstName;
		this.lastName=lastName;
		this.coursesRegistered=coursesRegistered;
		this.statusDate=statusDate;
		this.status=status;
	}
}


