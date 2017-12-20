# p2p-chat-app

## Features:
1. Simultaneous single/multi-chatter chatrooms
2. Asychronous message passing
3. Handling of various of message types, e.g., text, image, mp3, simple games ---- most importantly a receiver can ultimately handle a message type originally unknown to him/her.

## Technical Overview:
* Java RMI
* MVC pattern
* Extended visitor pattern
* Command dispatching pattern
* Java multithreading


Instructions:
* run ChatAppController.launch to start the app.
* IGNORE WORKING MODE panel and TESTING panel, unless you are going to run two ChatApp simultaneously on the same machine.
* Go to LOG IN panel; enter a desired user name and click LOG IN button.
* Go to NEW CHATROOM panel; enter a desired chatroom name and click CREATE to create a chatroom of desired name.
* Go to CONNECT TO panel; enter an IP address and click CONNECT button to connect to a remote host. The host name will be shown in the drop list in CONNECTED HOST panel, if connection is built successfully.
* In CONNECTED HOST panel, click REQUEST button to request a list of chatrooms from remote host. Choose one to join.
* Click the log out button to quit the app.
  * In chatroom 
    * Enter text in the text field and click SEND TEXT button to send a msg.
    * Click SEND IMAGE to choose from a local file systems.
    * Click LEAVE to exit the chatroom.
	
In particular, if you wish to run two ChatApp simultaneously on the same machine. 
* Click TESTING USER 1 for the first app.
* Click TESTING USER 2 for the second app.
* Choose different names for two users.
* Otherwise the same as normal instructions when running the ChatApp.
	
In addition to 5 well known datatype, there are 2 unknown data type:
* TextMsg
* ImageMsg

Notice:
* When a user is receiving these unknown datapacket, it will send an data packet of IRequestCmdType to the original sender to request the cmd for unknown data type.
* A user doesn't necessarily 
