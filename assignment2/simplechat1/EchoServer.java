// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;

import common.ChatIF;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer implements ChatIF
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
    System.out.println("Message received: " + msg + " from " + client);
    this.sendToAllClients(msg);
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }

    /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen. IMPLEMENTED FOR QUESTION 50 SP
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
    System.out.println("> " + message);
  }

  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler. IMPLEMENTED FOR QUESTION 50 SP
   */
  public void accept() 
  {
    try
    {
      BufferedReader fromConsole = 
        new BufferedReader(new InputStreamReader(System.in));
      String message;

      while (true) 
      {
        message = fromConsole.readLine();
        handleMessageFromServerUI(message);
      }
    } 
    catch (Exception ex) 
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }
  
    /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromServerUI(String message) //Modified for E50 SP
  {
    if(message.startsWith("#setport ") && !isListening()){
      setPort(Integer.parseInt(message.substring(9)));
    }
    // Every command excluding setport
    else if(message.charAt(0) == '#'){
      switch(message){
        case "#quit":
        System.exit(0);
        break;

        case "#stop":
        stopListening();
        break;

        case "#close":
        try{
          close();
        }
        catch(IOException e){
          System.out.println("Could not close connections.");
        }
        break;

        case "#start":
        if(!isListening()){
          try{
            listen();
          }
          catch(IOException e){
            System.out.println("Connection could not be established.");
          }
        }
        else{
          System.out.println("Error: Already listening.");
        }
        break;

        case "#getport":
        System.out.println(getPort());
        break;

        /*IMPORTANT: THREAD COMMANDn
         * Shows how many threads are active currently.
         * This is our solution for Part 1, Question 3.1.
         * Enter this command into the terminal to view the current number of threads running.
         */
        case "#showthreads":
        System.out.println(getClientConnections().length);
        break;

        default:
        System.out.println("Error. Not a valid command / cannot use command while logged on.");
      }
    }
    else{
        display(message);
        sendToAllClients("SERVER MSG>" + message);
      
    }
  }
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */

  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }

    // Start listening for server-user messages
    sv.accept();
  }
}
//End of EchoServer class
