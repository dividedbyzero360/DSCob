package ServerPackage;
import ServerOperationApp.ServerOperationIDLPOA;
import HelperPackage.*;

import org.omg.CORBA.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.Object;
import java.net.*;

public class ServerImplementation extends ServerOperationIDLPOA{
	private String serverName;
	private String ErrorMessage = "";
	private HashMap<String, HashMap<String, java.lang.Object>> data = new HashMap<String, HashMap<String, java.lang.Object>>();
	private Random r = new Random();
	private ArrayList<Integer> randomNumberStorage = new ArrayList<Integer>();
	List<Integer> udpPortOfOtherServers = null;
	Log log;
	private ORB orb;
	public ServerImplementation(String serverName, int udpPort, Log log) {
		this.serverName = serverName;
		this.log = log;
		startUDPPort(udpPort);
	}
	  public void setORB(ORB orb_val) {
	    orb = orb_val; 
	  }
	    
	  // implement shutdown() method
	  public void shutdown() {
	    orb.shutdown(false);
	  }
	  
	  public void setUDPPortOfOtherServers(List<Integer> udpPortOfOtherServers) {
			this.udpPortOfOtherServers = udpPortOfOtherServers;
		}  

	public String createSRecord(String managerID, String firstName,
			String lastName, String[] coursesRegistered, String status,
			String statusDate) {
		String key;
		String recordID;
		if (!validateFirstNameLastName(firstName, lastName)) {
			String message = "First Name and Last Name can not be empty and can not contain special symbol";
			log.logger.log(Level.INFO, message + " by " + managerID);
			return message;

		}
		if (!validateChangeRequest("status", status, RecordTypeEnum.Student)) {
			// Logger
			log.logger.log(Level.INFO, this.ErrorMessage + " by " + managerID);
			return this.ErrorMessage;
		} else {
			synchronized (this) {
				key = lastName.substring(0, 1);
				recordID = "SR" + getUniqueRandomNumber();
				int i = Integer.parseInt(status);
				StatusEnum statusEnum = StatusEnum.values()[i - 1];
				Student record = new Student(recordID, firstName, lastName,Arrays.asList(coursesRegistered) , statusEnum, statusDate);

				if (data.containsKey(key)) {
					data.get(key).put(recordID, record);
				} else {
					HashMap<String, Object> value = new HashMap<>();
					value.put(recordID, record);
					data.put(key, value);
				}
			}
		}
		// Logger
		log.logger.log(Level.INFO, ErrorMessages.getSuccessStudentInsertRecord(recordID) + " by " + managerID);
		return ErrorMessages.getSuccessStudentInsertRecord(recordID);
	}

	public String createTRecord(String managerID, String firstName,
			String lastName, String address, String phone,
			String specialization, String location) {
		String key;
		String recordID;
		if (!validateFirstNameLastName(firstName, lastName)) {
			String message = "First Name and Last Name can not be empty and can not contain special symbol";
			log.logger.log(Level.INFO, message + " by " + managerID);
			return message;

		}
		if (!validateChangeRequest("phone", phone, RecordTypeEnum.Teacher)) {
			String tempErrorMessage = this.ErrorMessage;
			if (!validateChangeRequest("location", location, RecordTypeEnum.Teacher)) {
				tempErrorMessage += ". " + this.ErrorMessage + ".";
			}
			log.logger.log(Level.WARNING, tempErrorMessage + " by " + managerID);
			return tempErrorMessage;
		} else if (!validateChangeRequest("location", location, RecordTypeEnum.Teacher)) {
			log.logger.log(Level.WARNING, this.ErrorMessage + " by " + managerID);
			return this.ErrorMessage;
		}
		synchronized (this) {
			key = lastName.substring(0, 1);
			recordID = "TR" + getUniqueRandomNumber();
			Teacher record = new Teacher(recordID, firstName, lastName, address, phone, specialization, location);
			if (data.containsKey(key)) {
				data.get(key).put(recordID, record);
			} else {
				HashMap<String, Object> value = new HashMap<>();
				value.put(recordID, record);
				data.put(key, value);
			}
		}

		// Logger
		log.logger.log(Level.INFO, ErrorMessages.getSuccessTeacherInsertRecord(recordID) + " by " + managerID);
		return ErrorMessages.getSuccessTeacherInsertRecord(recordID);
	}

	public String editRecord(String managerID, String recordID,
			String fieldName, String newValue) {
		this.ErrorMessage = "";
		Object o = null;
		fieldName = fieldName.toLowerCase().trim();
		for (Map.Entry<String, HashMap<String, Object>> entry : data.entrySet()) {
			for (Map.Entry<String, Object> innerEntry : entry.getValue().entrySet()) {
				if (innerEntry.getKey().equals(recordID)) {
					o = innerEntry.getValue();
					break;
				}
				if (o != null) {
					break;
				}
			}
		}
		if (o != null && recordID.toLowerCase().startsWith("tr")) {
			Teacher obj = (Teacher) o;
			synchronized (this) {

				if (validateChangeRequest(fieldName, newValue, RecordTypeEnum.Teacher)) {
					if (fieldName.equals("address")) {
						obj.address = newValue;
					} else if (fieldName.equals("phone")) {
						obj.phone = newValue;
					} else if (fieldName.equals("location")) {
						obj.location = ServerNameEnum.valueOf(newValue);
					}
					this.ErrorMessage = "Successfully update the field " + fieldName + " with the new value "
							+ newValue;
				}
			}
			this.ErrorMessage += " for record Id: " + recordID;
			// Logger
			log.logger.log(Level.INFO, this.ErrorMessage + " by " + managerID);
			return this.ErrorMessage;

		} else if (o != null && recordID.toLowerCase().startsWith("sr")) {
			Student obj = (Student) o;
			synchronized (this) {
				if (validateChangeRequest(fieldName, newValue, RecordTypeEnum.Student)) {
					if (fieldName.equals("status")) {
						StatusEnum prev = obj.status;
						obj.status = StatusEnum.values()[Integer.parseInt(newValue) - 1];
						if (obj.status != prev) {
							DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
							LocalDateTime now = LocalDateTime.now();
							String statusDate = dtf.format(now);
							obj.statusDate = statusDate;
						}
					} else if (fieldName.equals("courses")) {
						obj.coursesRegistered.add(newValue);

					}
					this.ErrorMessage = "Successfully update the field " + fieldName + " with the new value "
							+ newValue;
				}
				// Logger
				this.ErrorMessage += " for record Id: " + recordID;
				log.logger.log(Level.INFO, this.ErrorMessage + " by " + managerID);
				return this.ErrorMessage;
			}

		} else {
			// Logger
			log.logger.log(Level.INFO, "No record found with record id:" + recordID + " by " + managerID);
			return "No record found with record id: " + recordID;
		}
	}

	public String getRecordCounts(String managerID) {
		String recordCountWithNameOfServer = getShortFormOfServerName(serverName) + " " + calculateNumberOfRecords();
		DatagramSocket asocket = null;
		try {
			for (Integer i : udpPortOfOtherServers) {
				asocket = new DatagramSocket();
				byte[] sendMessage = "getRecordCount".getBytes();
				int serverPort = i;
				InetAddress serverAddress = InetAddress.getByName("localhost");
				DatagramPacket sendPacket = new DatagramPacket(sendMessage, sendMessage.length, serverAddress,
						serverPort);
				asocket.send(sendPacket);
				byte[] buffer = new byte[1000];
				DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
				asocket.receive(receivePacket);
				String recordCountWithNameOfOtherServer = new String(buffer).trim();
				recordCountWithNameOfServer += " " + recordCountWithNameOfOtherServer;
			}
		} catch (SocketException ex) {
			System.out.println(ex.getMessage());
			log.logger.log(Level.WARNING, "Socket exception occured by " + managerID);
		} catch (Exception ex) {
			log.logger.log(Level.WARNING, "Exception occured by " + managerID);
			System.out.println(ex.getMessage());
		} finally {
			if (asocket != null)
				asocket.close();
		}
		// Logger
		log.logger.log(Level.INFO,
				"Record Count with Server Names : " + recordCountWithNameOfServer + " by " + managerID);
		return recordCountWithNameOfServer;
	}
	
	private void startUDPPort(int udpPort) {
		Thread newThread = new Thread(new Runnable() {
			public void run() {
				System.out.println("Thread started " + Thread.currentThread().getName());
				DatagramSocket asocket = null;
				try {
					asocket = new DatagramSocket(udpPort);
					byte[] receiveBuffer = new byte[100];
					while (true) {
						DatagramPacket recievePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
						asocket.receive(recievePacket);
						String recordCountWithNameOfServer = getShortFormOfServerName(serverName) + " "
								+ calculateNumberOfRecords();
						receiveBuffer = recordCountWithNameOfServer.getBytes();
						int dataLength = recordCountWithNameOfServer.getBytes().length;
						DatagramPacket replyPacket = new DatagramPacket(receiveBuffer, dataLength,
								recievePacket.getAddress(), recievePacket.getPort());
						asocket.send(replyPacket);
					}
				} catch (SocketException ex) {
					System.out.println(ex.getMessage());
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
				} finally {
					if (asocket != null)
						asocket.close();
				}
			}
		});
		newThread.start();
	}
	public boolean validateFirstNameLastName(String firstName, String lastName) {
		if ((firstName != null && !firstName.isEmpty()) && (lastName != null && !lastName.isEmpty())) {
			Pattern p = Pattern.compile("[^a-z]", Pattern.CASE_INSENSITIVE);
			Matcher m = p.matcher(firstName);
			boolean isfirstNotNameValid = m.find();
			m = p.matcher(lastName);
			boolean islastNotNameValid = m.find();
			if (isfirstNotNameValid || islastNotNameValid)
				return false;
		} else {
			return false;
		}
		return true;
	}
	
	private boolean validateChangeRequest(String fieldName, String newValue, RecordTypeEnum recordType) {
		if (recordType == RecordTypeEnum.Teacher) {
			if (fieldName.equals("address") || fieldName.equals("phone") || fieldName.equals("location")) {
				if (fieldName.equals("phone")) {
					try {
						Long.parseLong(newValue);
						if (newValue.length() == 10) {
							return true;
						} else {
							this.ErrorMessage = "Phone number must be 10 digit number";
							return false;
						}

					} catch (NumberFormatException ex) {
						this.ErrorMessage = "Phone number must be 10 digit number";
						return false;
					}

				} else if (fieldName.equals("location")) {
					try {
						ServerNameEnum.valueOf(newValue);
					} catch (IllegalArgumentException ex) {
						this.ErrorMessage = "Location can only be mtl, lvl or ddo";
						return false;
					}
				}
				return true;
			}
		} else if (recordType == RecordTypeEnum.Student) {
			if (fieldName.equals("course") || fieldName.equals("status")) {
				if (fieldName.equals("status")) {
					try {
						int i = Integer.parseInt(newValue);
						if (i != 1 && i != 2) {
							this.ErrorMessage = "Incorrect input for status. For active type 1 and for inactive type 2";
							return false;
						}
					} catch (Exception ex) {
						this.ErrorMessage = "Incorrect input for status. For active type 1 and for inactive type 2";
						return false;
					}
				}
				return true;
			}

		}
		this.ErrorMessage = "Either there is no field with " + fieldName + " or it can not be changed";
		return false;
	}


	private int calculateNumberOfRecords() {
		int numberOfRecords = 0;
		for (Map.Entry<String, HashMap<String, java.lang.Object>> e : data.entrySet()) {
			numberOfRecords += e.getValue().size();
		}
		return numberOfRecords;
	}

	public String getShortFormOfServerName(String serverName) {
		String shortName = "";
		if (serverName.equals("Montreal")) {
			shortName = "MTL";
		} else if (serverName.equals("Laval")) {
			shortName = "LVL";
		} else if (serverName.equals("Dollard-des-Ormeaux")) {
			shortName = "DDO";
		}
		return shortName;
	}
	
	private int getUniqueRandomNumber() {
		int randomNumber = -1;
		while (true) {
			randomNumber = returnFiveDigitRandomNumber();
			if (!isRandomNumberIsAlreadyPresent(randomNumber)) {
				break;
			}
		}
		randomNumberStorage.add(randomNumber);
		return randomNumber;
	}

	private int returnFiveDigitRandomNumber() {
		return ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
	}

	private boolean isRandomNumberIsAlreadyPresent(int no) {
		return randomNumberStorage.contains(no);
	}
}
