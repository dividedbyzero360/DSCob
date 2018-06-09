package ServerOperationApp;


/**
* ServerOperationApp/ServerOperationIDLPOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from ServerOperationIDL.idl
* Friday, June 8, 2018 4:19:55 PM EDT
*/

public abstract class ServerOperationIDLPOA extends org.omg.PortableServer.Servant
 implements ServerOperationApp.ServerOperationIDLOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("shutdown", new java.lang.Integer (0));
    _methods.put ("createSRecord", new java.lang.Integer (1));
    _methods.put ("createTRecord", new java.lang.Integer (2));
    _methods.put ("editRecord", new java.lang.Integer (3));
    _methods.put ("getRecordCounts", new java.lang.Integer (4));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {
       case 0:  // ServerOperationApp/ServerOperationIDL/shutdown
       {
         this.shutdown ();
         out = $rh.createReply();
         break;
       }

       case 1:  // ServerOperationApp/ServerOperationIDL/createSRecord
       {
         String managerID = in.read_string ();
         String firstName = in.read_string ();
         String lastName = in.read_string ();
         String coursesRegistered[] = ServerOperationApp.ServerOperationIDLPackage.stringSeqHelper.read (in);
         String status = in.read_string ();
         String statusDate = in.read_string ();
         String $result = null;
         $result = this.createSRecord (managerID, firstName, lastName, coursesRegistered, status, statusDate);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 2:  // ServerOperationApp/ServerOperationIDL/createTRecord
       {
         String managerID = in.read_string ();
         String firstName = in.read_string ();
         String lastName = in.read_string ();
         String address = in.read_string ();
         String phone = in.read_string ();
         String specialization = in.read_string ();
         String location = in.read_string ();
         String $result = null;
         $result = this.createTRecord (managerID, firstName, lastName, address, phone, specialization, location);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 3:  // ServerOperationApp/ServerOperationIDL/editRecord
       {
         String managerID = in.read_string ();
         String recordID = in.read_string ();
         String fieldName = in.read_string ();
         String newValue = in.read_string ();
         String $result = null;
         $result = this.editRecord (managerID, recordID, fieldName, newValue);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 4:  // ServerOperationApp/ServerOperationIDL/getRecordCounts
       {
         String managerID = in.read_string ();
         String $result = null;
         $result = this.getRecordCounts (managerID);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:ServerOperationApp/ServerOperationIDL:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public ServerOperationIDL _this() 
  {
    return ServerOperationIDLHelper.narrow(
    super._this_object());
  }

  public ServerOperationIDL _this(org.omg.CORBA.ORB orb) 
  {
    return ServerOperationIDLHelper.narrow(
    super._this_object(orb));
  }


} // class ServerOperationIDLPOA
