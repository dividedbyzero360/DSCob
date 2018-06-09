package ServerOperationApp;


/**
* ServerOperationApp/ServerOperationIDLOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from ServerOperationIDL.idl
* Friday, June 8, 2018 4:19:55 PM EDT
*/

public interface ServerOperationIDLOperations 
{
  void shutdown ();
  String createSRecord (String managerID, String firstName, String lastName, String[] coursesRegistered, String status, String statusDate);
  String createTRecord (String managerID, String firstName, String lastName, String address, String phone, String specialization, String location);
  String editRecord (String managerID, String recordID, String fieldName, String newValue);
  String getRecordCounts (String managerID);
} // interface ServerOperationIDLOperations
