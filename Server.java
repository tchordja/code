import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/*
 * The Server class implements the interface ServerInterface and extends the class
 * UnicastRemoteObject.
 */
public class Server extends UnicastRemoteObject implements ServerInterface {
	
	/*
	 * Vector stores a remote reference for each currently logged in client, 
	 * when a client invokes the remotely accessible method login.
	 */
	protected Vector<ClientInterface> clients = new Vector<ClientInterface>();
	
	/*
	 * Constructor for a remote object of type Server.
	 * RemoteException: the object handle cannot be constructed.
	 */
	public Server() throws RemoteException {
	}

	/*
	 * Implementation of the login method. If a client invokes this 
	 * method then all other actually logged in clients get a notification
	 * that a new user has joined the chat. 
	 * A reference to the new client is added to the ArrayList.
	 */
	public void login(ClientInterface client) throws RemoteException {
	
		// new nickname
		String nickname = client.getNickname();
		
		String oldnickname;
		
		int nclient;
		
		nclient = clients.size();
		
		int count;
		
		count=0;
		
		// check if the nickname specified already exists.. 
		// Necessary?????? 
		for (int i = 0; i < nclient; i++) {
			ClientInterface c = clients.get(i);
			oldnickname = c.getNickname();
			
			if(nickname.equals(oldnickname)){
			try {
				// Mandare indietro anche un messaggio di errore per non far andare avanti il client
				client.getMessage("Nickname already in use, change it! ", "Server");
			} catch (RemoteException e) {
		
				logout(c);
				i = i - 1;
			} 
			
			break;				  }
		      else count = count+1;
		}
		
		// Ok the nickname hasn't used by an active client
		if (count == nclient){
		System.out.println("Login: " + nickname);
		
		broadcastMessage("New user: " + nickname + "", "");	
		// ad the client to the vector
		clients.add(client);
		}
		
	}
	
	/*
	 * Implementation of the broadcastMessage method. 
	 * Send to every clients a message
	 * This broadcasting is performed by a remote call of the method getMessage which is
	 * defined for a client object. The references to such clients are obtained from
	 * the vector which stores a remote reference to each currently logged in client.
	 */
	public void broadcastMessage(String message, String nickname) throws RemoteException {
		for (int i = 0; i < clients.size(); i++) {
			ClientInterface c = clients.get(i);
			try {
				c.getMessage(message, nickname);
			} catch (RemoteException e) {
				/*
				 * If a client is not accessible, then it is removed from
				 * the vector and the index i is decremented because 
				 * all other clients go down one place
				 */
				logout(c);
				i = i - 1;
			} 
		}
		
	}
	
	/*
	 * Implementation of the sendMessage method. 
	 * Send to a client a message
	 * This method is performed by a remote call of the method getMessage which is
	 * defined for a client object. 
	 * Sender: who sends the message
	 * message: message
	 * nickname: receiver of the message
	 */
	public void sendMessage(String sender, String message, String nickname) throws RemoteException{
	
		String destination;
		
		// send the message to the right client
		for (int i = 0; i < clients.size(); i++) {
			ClientInterface c = clients.get(i);
			destination = c.getNickname();
			
			if(nickname.equals(destination)){
			try {
				c.getMessage(message, sender);
			    } 
			catch (RemoteException e) {
				logout(c);
				i = i - 1;
			}
			break;
			}
			
		}
	
	
	}
	
	/*
	 * Implementation of the whom method. 
	 * Print on the client shell the list of all the user actually connected
	 * client: who calls the method 
	 */
	public void whom(ClientInterface client) throws RemoteException{
	
		String user;
		
		for (int i = 0; i < clients.size(); i++) {
			
			ClientInterface c = clients.get(i);
			user = c.getNickname();
			
			try {
				client.getMessage(user, "");
			    } 
			catch (RemoteException e) {
				logout(c);
				i = i - 1;
			}
						      }
			
	
	}
	
	
	// delete a client from the client connected list
	// advise all the user of the deletion
	public void exit(ClientInterface client) throws RemoteException {
		
		String nickname = client.getNickname();
		
		broadcastMessage(nickname + " left the conversation", "");
		
		System.out.println(nickname + " left the conversation");
		
		logout(client);
	}
	

	
	/*
	 * Local method which is used for removing a connected client
	 * from the vector of all connections.
	 */
	public void logout(ClientInterface client) {
		clients.remove(client);
	}
	
	/*
	 * Main method
	 * Registers a remote accessible Server object by invoking the 
	 * rebind method on the class Naming. The name of this object is "Server". This
	 * means that all clients which want to get a reference to this object have to
	 * specify this name in the lookup method. 
	 */
	public static void main(String[] args) {
		try {
			Naming.rebind("Server", new Server());
			
			System.out.println("Server is ready");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
