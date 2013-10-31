import java.rmi.*;

 /* Server interface : it specifies the method available for the object
  at client side 
  */
public interface ServerInterface extends Remote {
	
	/*
	 * login: register the client at server side  
	 */
	public void login(ClientInterface client) throws RemoteException;
	
	/*
	 * broadcastMessage: send every clients the string message (nickname specifies who sends the message) 
	 */
	public void broadcastMessage(String message, String nickname) throws RemoteException;
	
	/*
	 * sendMessage: send a user a message
	 *	sender   : who sends the message
		message  : message
		nickname : receiver of the message
	 */
	public void sendMessage(String sender, String message, String nickname) throws RemoteException;
	
	/*
	 * whom: print the list of the client active at server side. Print on the shell of the client who send the
	 * 	 request
	 */
	public void whom(ClientInterface client) throws RemoteException;
	
	/*
	 * exit: remove the client from the register of the active client on the server
	 */
	public void exit(ClientInterface client) throws RemoteException;
}

