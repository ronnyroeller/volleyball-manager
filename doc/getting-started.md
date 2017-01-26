# Getting started

## Windows

### Installation

Volleyball Manager is easy to install and configure on your computer. Start the installation program install.exe and follow the instructions.
Ensure that you install the server- and the administratormodul. If you are new to the Volleyball Manager that is the configuration you want to have most likely. A single installation makes only sense if you want to run the server and administrator on two different computers.

### Start Volleyball Manager

The Volleyball Manager consists of two parts: the server and the administrator client. The server saves all data. For that reason it have to be started always before the administraor application can be started.

1. **Start the Server**: Click on the server icon on your desktop or in your start menu. Now a black box appears in which the server is loaded. Wait until no further changes appear in the box and the line *Started in [..:..]ms*> is printed. The duration of this startup depends on the speed of your computer. Times between 10 sec and 2 minutes are common.<

2. **Start the Administrator Client**: After starting the server you can open the administrator client. Therefor click on the corrosponding icon on the desktop or in the start menu.

3. **Test Volleyball Manager**:  After starting the server and administrator select *Help->Help* from the menu. In this manual read the sectioin *Introduction-&gt;Quickstart*.

## Linux

1. To install Volleyball Manager under Linux, you need Java JDK 5 or higher. Please download this for free from http://www.java.com and install it.

2. Start the automatic installation program of Volleyball Manager and follow the instructions. Therefor, change to the directory that includes the install.jar and type in:
  ~~~~
  $ java -jar install.jar
  ~~~~

3. To start the server, change to the Volleyball Manager directory that you selected during the installation process. Type in:
  ~~~~
  $ cd server/bin
  $ java -jar run.jar
  ~~~~

4. After the server was started successfully, you will see a line "Server started in XX sec". Open now a new terminal and change also to the Volleyball Manager directory. To start the administrator application type  in:
  ~~~~
  $ cd client
  $ java -jar volley.jar
  ~~~~
