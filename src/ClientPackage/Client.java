package ClientPackage;

import ServerOperationApp.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import HelperPackage.ServerInstance;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.omg.CORBA.*;

public class Client {
	static ServerOperationIDL serverImpl;

	public static void main(String[] args) {
		Client test = new Client();

		try {

			// create and initialize the ORB
			ORB orb = ORB.init(args, null);
			// get the root naming context
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			// Use NamingContextExt instead of NamingContext. This is
			// part of the Interoperable naming Service.
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			// resolve the Object Reference in Naming
			String name = "mtl";
			// serverImpl =
			// ServerOperationIDLHelper.narrow(ncRef.resolve_str(name));
			ServerOperationIDL operationsMTL1 = ServerInstance.getServerInstance("mtl1111", ncRef);
			ServerOperationIDL operationsMTL2 = ServerInstance.getServerInstance("mtl1234", ncRef);

			ServerOperationIDL operationsLVL1 = ServerInstance.getServerInstance("lvl1111", ncRef);
			ServerOperationIDL operationsLVL2 = ServerInstance.getServerInstance("lvl1234", ncRef);

			ServerOperationIDL operationsDDO1 = ServerInstance.getServerInstance("ddo1111", ncRef);
			ServerOperationIDL operationsDDO2 = ServerInstance.getServerInstance("ddo1234", ncRef);

			// System.out.println("Obtained a handle on server object: " +
			// serverImpl);
			/*
			 * System.out.println( serverImpl.createTRecord("mtl1111", "Test",
			 * "TestL", "Babupatty", "1111111111", "CS", "mtl")); name = "lvl";
			 * serverImpl =
			 * ServerOperationIDLHelper.narrow(ncRef.resolve_str(name));
			 * System.out.println( serverImpl.createTRecord("lvl1111", "Test",
			 * "TestL", "Babupatty", "1111111111", "CS", "lvl")); name = "ddo";
			 * serverImpl =
			 * ServerOperationIDLHelper.narrow(ncRef.resolve_str(name));
			 * System.out.println( serverImpl.createTRecord("ddo1111", "Test",
			 * "TestL", "Babupatty", "1111111111", "CS", "lvl"));
			 */
			Thread montreal1 = new Thread(new Runnable() {

				public void run() {

					test.commonServerCalls(operationsMTL1, "MTL5555", "mtl");
				}
			});
			montreal1.setName("Thread of mtl 1 manager");
			montreal1.start();

			Thread montreal2 = new Thread(new Runnable() {

				public void run() {
					test.commonServerCalls(operationsMTL2, "MTL6666", "mtl");
				}
			});
			montreal2.setName("Thread of mtl 2 manager");
			montreal2.start();

			Thread laval1 = new Thread(new Runnable() {

				public void run() {
					test.commonServerCalls(operationsLVL1, "LVL0000", "lvl");
				}
			});
			laval1.setName("Thread of lvl 1 manager");
			laval1.start();

			Thread laval2 = new Thread(new Runnable() {

				public void run() {
					test.commonServerCalls(operationsLVL2, "LVL1111", "lvl");
				}
			});
			laval2.setName("Thread of lvl 2 manager");
			laval2.start();

			Thread dollard1 = new Thread(new Runnable() {

				public void run() {
					test.commonServerCalls(operationsDDO1, "DDO3333", "ddo");
				}
			});
			dollard1.setName("Thread of ddo 1 manager");
			dollard1.start();

			Thread dollard2 = new Thread(new Runnable() {

				public void run() {

					test.commonServerCalls(operationsDDO2, "DDO4444", "ddo");
				}
			});
			dollard2.setName("Thread of ddo 2 manager");
			dollard2.start();

		} catch (Exception ex) {
			System.out.println("ERROR : " + ex);
			ex.printStackTrace(System.out);
		}
	}

	public void commonServerCalls(ServerOperationIDL server, String managerID, String serverShortName) {
		List<String> studentIdsToBeEdited = new ArrayList<>();
		List<String> teacherIdsToBeEdited = new ArrayList<>();
		String currentThreadName = " (" + Thread.currentThread().getName() + ")";
		for (int i = 0; i < 1; i++) {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
			LocalDateTime now = LocalDateTime.now();
			String statusDate = dtf.format(now);
			try {

				// Creating teachers and students records here
				String[] tempArray = new String[2];
				String output = server.createSRecord(managerID, "FirstS " + i, "LastS " + i,
						Arrays.asList("DS", "Comp").toArray(tempArray), "1", statusDate);

				studentIdsToBeEdited.add(output.substring(output.length() - 7)); // getting
																					// student
																					// record
																					// id
																					// from
																					// the
																					// message
																					// sent
																					// back
																					// from
																					// the
																					// server
				System.out.println(output + currentThreadName);
				output = server.createTRecord(managerID, "FirstT " + i, "LastT " + i, "Concordia", "2222222222", "DS",
						serverShortName);
				teacherIdsToBeEdited.add(output.substring(output.length() - 7));// getting
																				// teacher
																				// record
																				// id
																				// from
																				// the
																				// message
																				// sent
																				// back
																				// from
																				// the
																				// server
				System.out.println(output + currentThreadName);
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				System.out.println(server.getRecordCounts(managerID) + currentThreadName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			// Editing teacher record with valid data
			for (String teacherRecordID : teacherIdsToBeEdited) {
				System.out.println(
						server.editRecord(managerID, teacherRecordID, "address", "Boulevard") + currentThreadName);
				System.out.println(
						server.editRecord(managerID, teacherRecordID, "phone", "1111111111") + currentThreadName);
				System.out.println(
						server.editRecord(managerID, teacherRecordID, "location", serverShortName) + currentThreadName);

			}

			// Editing student record with valid data
			for (String studentRecordID : studentIdsToBeEdited) {

				System.out.println(
						server.editRecord(managerID, studentRecordID, "course", "Moral Science") + currentThreadName);
				System.out.println(server.editRecord(managerID, studentRecordID, "status", "2") + currentThreadName);

			}

			// Editing a teacher record with invalid data

			// 1. Editing with wrong teacher record id
			System.out.println(server.editRecord(managerID, "TRDDDD", "address", "Moral Science") + currentThreadName);

			// 2. Editing non-changeable field for teacher
			System.out.println(server.editRecord(managerID, teacherIdsToBeEdited.get(0), "firstName", "Shinska")
					+ currentThreadName);

			// 3 Editing phone no of teacher with wrong format
			System.out.println(
					server.editRecord(managerID, teacherIdsToBeEdited.get(0), "phone", "3333") + currentThreadName);

			// 3 Editing location of teacher with wrong data
			System.out.println(
					server.editRecord(managerID, teacherIdsToBeEdited.get(0), "location", "Delhi") + currentThreadName);

			// Editing a student record with invalid data

			// 1. Editing with wrong student record id
			System.out.println(server.editRecord(managerID, "SRDDDD", "address", "Moral Science") + currentThreadName);

			// 2. Editing non-changeable field for student
			System.out.println(server.editRecord(managerID, studentIdsToBeEdited.get(0), "firstName", "Nakamura")
					+ currentThreadName);

			// 3 Editing status of student with wrong format
			System.out.println(
					server.editRecord(managerID, studentIdsToBeEdited.get(0), "status", "3") + currentThreadName);

			// Finally again call record count
			System.out.println(server.getRecordCounts(managerID) + currentThreadName);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
