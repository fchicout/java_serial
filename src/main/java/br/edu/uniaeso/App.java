package br.edu.uniaeso;

import com.fazecast.jSerialComm.SerialPort;

public class App {

    public static void main(String[] args) throws InterruptedException {
        ArduinoSerialCommunicator asc = new ArduinoSerialCommunicator(115200, 8, SerialPort.ONE_STOP_BIT,
                SerialPort.NO_PARITY);
        System.out.println("\n\n SerialPort Data Transmission");

        // use the for loop to print the available serial ports
        asc.getPortsInfo();
        System.out.println("Closing ports");
        asc.closeAllPorts();

        // Open the first Available port
        SerialPort commPort = asc.getCOMMPortByName("COM6");

        asc.setSerialParameters();

        commPort.openPort(2000); // open the port
        // Arduino Will Reset
        System.out.println(" Watch Arduino for Reset ");

        if (!asc.verifyPortOpen()) {
            System.out.println(" Port not open ");
            System.exit(-1);
        }

        asc.showSelectedPortInfo();

        Thread.sleep(2000); // Delay introduced because when the SerialPort is opened ,Arduino gets resetted
                            // Time for the code in Arduino to rerun after Reset

        asc.writeMessage("on");

        
        asc.closeCOMMPort(); // Close the port

        if (!asc.verifyPortOpen()) {
            System.out.println(" Port not open ");
            System.exit(-1);
        }
    }

}
