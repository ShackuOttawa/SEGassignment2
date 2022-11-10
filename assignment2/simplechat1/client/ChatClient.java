// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;
import java.net.ConnectException;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    openConnection();
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message) //Modified for E49 SP
  {
    // Modified for E
    if(message.startsWith("#sethost ") && !isConnected()){
      setHost(message.substring(9));
    }
    else if(message.startsWith("#setport ") && !isConnected()){
      setPort(Integer.parseInt(message.substring(9)));
    }
    // Every command excluding sethost and setport
    else if(message.charAt(0) == '#'){
      switch(message){
        case "#quit":
        quit();
        break;

        case "#logoff":
        try{
          closeConnection();
        }
        catch(IOException e) {
          System.out.println("Could not close connection.");
        }
        break;

        case "#login":
        if(!isConnected()){
          try{
            openConnection();
          }
          catch(IOException e){
            System.out.println("Connection could not be established.");
          }
        }
        else{
          System.out.println("Error: Already connected to server.");
        }
        break;

        case "#gethost":
        System.out.println(getHost());
        break;

        case "#getport":
        System.out.println(getPort());
        break;

        default:
        System.out.println("Error. Not a valid command. / cannot use command while logged on.");
      }
    }
    else{
      try
      {
        sendToServer(message);
      }
      catch(IOException e)
      {
        clientUI.display
          ("Could not send message to server.  Terminating client.");
        quit();
      }
    }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
}
//End of ChatClient class
