package ServerPackage;

public class ErrorMessages {

	static String locationError="Error occured at Server. Location can be either mtl, lvl or ddo";
	static String statusError="Error occured at Server. Status can be either 0 (inactive) or 1 active";
	
	public static String getSuccessTeacherInsertRecord(String recordID)
	{
		return "Teacher record successfully inserted. The record id of the new record is "+recordID;
	}
	
	public static String getSuccessStudentInsertRecord(String recordID)
	{
		return "Student record successfully inserted. The record id of the new record is "+recordID;
	}
}
