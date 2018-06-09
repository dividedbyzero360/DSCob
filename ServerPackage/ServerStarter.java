package ServerPackage;

import ServerOperationApp.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;

import java.io.File;
import java.util.Arrays;
import java.util.Properties;

public class ServerStarter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			new File("./Serverlogs").mkdirs();
			Log mtlLog = new Log("./Serverlogs/MtlServerLog.txt", "MtlServer");
			Log ddoLog = new Log("./Serverlogs/DdoServerLog.txt", "DdoServer");
			Log lvlLog = new Log("./Serverlogs/LvlServerLog.txt", "LvlServer");
			// create and initialize the ORB
			ORB orb = ORB.init(args, null);
			// get reference to rootpoa & activate the POAManager
			POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			rootpoa.the_POAManager().activate();
			// create servant and register it with the ORB
			ServerImplementation mtlImplementation=new ServerImplementation("Montreal",6668, mtlLog);
			mtlImplementation.setUDPPortOfOtherServers(Arrays.asList(6665, 6667));
			mtlImplementation.setORB(orb); 
			// get object reference from the servant
			org.omg.CORBA.Object ref = rootpoa.servant_to_reference(mtlImplementation);
			ServerOperationIDL so=ServerOperationIDLHelper.narrow(ref);
			// get the root naming context
			// NameServiceinvokes the name service
			org.omg.CORBA.Object objRef=
			orb.resolve_initial_references("NameService");
			// Use NamingContextExtwhich is part of the Interoperable Naming Service (INS) specification.
			NamingContextExt ncRef= NamingContextExtHelper.narrow(objRef);
			// bind the Object Reference in Naming
			String name = "Hello";
			NameComponent path[] = ncRef.to_name( name );
			ncRef.rebind(path, so);
			System.out.println("HelloServerready and waiting ...");
			// wait for invocations from clients
			orb.run();
			
		} catch (Exception e) {
			System.err.println("ERROR: " + e);
			e.printStackTrace(System.out);
		}

		System.out.println("HelloServer Exiting ...");

	}
	
	

}
