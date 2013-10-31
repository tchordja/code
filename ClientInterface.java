import java.rmi.Remote;
import java.rmi.RemoteException;

 
 /* Client interface : it specifies the method available for the object
  at server side 
  */
public interface ClientInterface extends Remote {
	
	/*
	  Get message: server uses this method to send a message to the client
	    message  : text of the message
	    nickname : user that sent the message
	 */
	public void getMessage(String message, String nickname) throws RemoteException;
	
	/*
	    getNickname: server uses this method to know the user's nickname (private local client paramet)
	*/
	public String getNickname() throws RemoteException;
}
