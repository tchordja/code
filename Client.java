
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;

/*
 * The Client class implements the interface ClientInterface and extends the class
 * UnicastRemoteObject.
 */
 
public class Client extends UnicastRemoteObject implements ClientInterface {

	/* parameter of the class
	 * I think it can not be static... (I have just finished to make the new version of the code)
	 */
	private static String nickname;
	
	/*
	 * Constructor for the object of type Client. 
	 * RemoteException: the object handle cannot be constructed
	 */
	public Client(String nickname) throws RemoteException {
	
	this.nickname=nickname;

								}
								
	/*
	 * Implementation of the getNickname method. It should be called
	 * by the server to know the nickname of the client that calls a function.
	 * RemoteException: error during the remote method call.
	 */
	public String getNickname() throws RemoteException {
      
	return this.nickname;
			      }

	/*
	 * Implementation of the getMessage method. It should be called
	 * by the server to send the message to the client. 
	 * RemoteException: error occurs during the remote method call.
	 */
	public void getMessage(String message, String nickname) throws RemoteException {

		if (!nickname.equals(""))
		    System.out.println(nickname + ": " + message + "\n");
		
		else System.out.println(message + "\n");
	}

	
	
	
public static void main(String[] args) {
		
	
	ServerInterface server;
	Scanner read_line;
	String nickname;
	String command;	
	String message;
	String destination;
		
		
		try{
		      // create a server object (connect to the server??)
		      server = (ServerInterface)Naming.lookup("rmi://localhost:1099/Server");
		      
		      // Scanner method to read a line.. Do you know something else? (Better than Scanner)		
		      read_line = new Scanner(System.in);
		
		      System.out.println( "Insert your nickname: ");
		
		      // save in nickname the nickname written by the user
		      nickname = read_line.nextLine();
    
		      System.out.println( "# ");
		      
		      if (nickname != null && !nickname.equals("")) {
			try {	
			
				// create a new object of kind client
				Client client = new Client(nickname);
				
				// log the client at server side
				server.login(client);
				
				
				// infinite loop to perform every request from the client
				for(;;){
				
				
				System.out.println( "# ");
				
				// read the command
				command = read_line.nextLine(); 
				
				
				// print the nickname of the client connected
				if(command.equals("who")){server.whom(client);}
				
				// send a message to a client
				else if(command.equals("send")){
				
				// specifies the receiver
				System.out.println( "To whom? ");
				destination = read_line.nextLine(); 
				
				// specifies the message
				System.out.println( "Insert message: ");
				message = read_line.nextLine(); 
				
				// remote method to send a message to another client
				server.sendMessage(nickname, message,destination);
					       }
				
				// exit from the chat room
				else if(command.equals("logout")){server.exit(client);
					       break;}
				
				// method of previous version 
				//else server.broadcastMessage(command, nickname);
				
				else System.out.println("Command not found");
				
					       
				}
				//client.startIteration(server);
				
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
		
		
		
		
		else System.exit(0);
		      
		      
		      
		      
		    }
	
	      catch (MalformedURLException murle) {
			  System.out.println("MalformedURLException");
			  System.out.println(murle);
					      }
	      catch (RemoteException re) {
			  System.out.println("RemoteException");
			  System.out.println(re);
				  }
	      catch (NotBoundException nbe) {
			  System.out.println("NotBoundException");
			  System.out.println(nbe);
					}	
				
		System.exit(0);
	}	
	
}

