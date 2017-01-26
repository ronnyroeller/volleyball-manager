HOW TO INSTALL VOLLEYBALL MANAGERS UNDER LINUX

1. To install Volleyball Manager under Linux, you need Java JDK 1.4
or higher. Please download this for free from http://www.java.com and
install it.

2. Start the automatic installation program of Volleyball Manager and
follow the instructions. Therefor, change to the directory that
includes the install.jar and type in:
~~~~
$ java -jar install.jar
~~~~

4. To start the server, change to the Volleyball Manager directory
that you selected during the installation process. Type in:
~~~~
$ cd server/bin
$ java -jar run.jar
~~~~

5. After the server was started successfully, you will see a line "Server 
started in XX sec". Open now a new terminal and change also to the 
Volleyball Manager directory. To start the administrator application type 
in:
~~~~
$ cd client
$ java -jar volley.jar
~~~~
