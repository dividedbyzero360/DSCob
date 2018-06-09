package ClientPackage;
import ServerOperationApp.*;
import org.omg.CosNaming.*;
import org.omg.CORBA.*;

public class Client {
	static ServerOperationIDL serverImpl;
	public static void main(String[] args) {

		try{
			
			// create and initialize the ORB
	        ORB orb = ORB.init(args, null);
	     // get the root naming context
	        org.omg.CORBA.Object objRef = 
	            orb.resolve_initial_references("NameService");
	     // Use NamingContextExt instead of NamingContext. This is 
	        // part of the Interoperable naming Service.  
	        NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
	     // resolve the Object Reference in Naming
	        String name = "mtl";
	        serverImpl = ServerOperationIDLHelper.narrow(ncRef.resolve_str(name));

	      //System.out.println("Obtained a handle on server object: " + serverImpl);
	        System.out.println(serverImpl.createTRecord("mtl1111", "Test", "TestL", "Babupatty", "1111111111", "CS", "mtl"));
	         name = "lvl";
	        serverImpl = ServerOperationIDLHelper.narrow(ncRef.resolve_str(name));
	        System.out.println(serverImpl.createTRecord("lvl1111", "Test", "TestL", "Babupatty", "1111111111", "CS", "lvl"));
	        name = "ddo";
	        serverImpl = ServerOperationIDLHelper.narrow(ncRef.resolve_str(name));
	        System.out.println(serverImpl.createTRecord("ddo1111", "Test", "TestL", "Babupatty", "1111111111", "CS", "lvl"));
			
		}catch(Exception ex){
			System.out.println("ERROR : " + ex) ;
			ex.printStackTrace(System.out);
		}
		
		while(true){
			
		}
	}

}
