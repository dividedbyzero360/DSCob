javac ServerOperationApp/*.java ServerOperationApp/ServerOperationIDLPackage/*.java ServerPackage/*.java

javac ClientPackage/*.java

start orbd -ORBInitialPort 1050

start java ServerPackage.ServerStarter -ORBInitialPort 1050 -ORBInitialHost localhost

start java ClientPackage.ManagerClient -ORBInitialPort 1050 -ORBInitialHost localhost