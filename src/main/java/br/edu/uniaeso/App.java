package br.edu.uniaeso;

import com.fazecast.jSerialComm.SerialPort;

public class App {

    public static void getPortsInfo() {
        System.out.println("\n\n Available Ports");
        SerialPort[] ports = SerialPort.getCommPorts();
        for (SerialPort serialPort : ports) {
            System.out.println("Descriptive Port Name:\t" + serialPort.getDescriptivePortName());
            System.out.println("Is Open:\t\t" + serialPort.isOpen());
            System.out.println("Description:\t\t" + serialPort.getPortDescription());
            System.out.println("Location:\t\t" + serialPort.getPortLocation());
            System.out.println("Serial Number:\t\t" + serialPort.getSerialNumber());
            System.out.println("Port Name:\t\t" + serialPort.getSystemPortName());
            System.out.println("Port Path:\t\t" + serialPort.getSystemPortPath());
            System.out.println();
        }
    }

    public static void closeAllPorts() {
        SerialPort[] ports = SerialPort.getCommPorts();
        for (SerialPort serialPort : ports) {
            if (serialPort.isOpen()) {
                serialPort.flushIOBuffers();
                serialPort.closePort();
            }
        }
    }

    public static SerialPort getCOMMPortByName(String portName) {
        SerialPort[] ports = SerialPort.getCommPorts();
        for (SerialPort serialPort : ports) {
            if (serialPort.getSystemPortName().equals(portName)) {
                return serialPort;
            }
        }
        return null;
    }

    public static void main(String[] args) throws InterruptedException {

        int BaudRate = 115200;
        int DataBits = 8;
        int StopBits = SerialPort.ONE_STOP_BIT;
        int Parity = SerialPort.NO_PARITY;
        System.out.println("\n\n SerialPort Data Transmission");

        // use the for loop to print the available serial ports
        getPortsInfo();
        System.out.println("Closing ports");
        closeAllPorts();

        // Open the first Available port
        SerialPort commPort = getCOMMPortByName("COM6");

        // Set Serial port Parameters
        commPort.setComPortParameters(BaudRate, DataBits, StopBits, Parity);// Sets all serial port parameters at
                                                                            // one time

        commPort.openPort(2000); // open the port
        // Arduino Will Reset
        System.out.println(" Watch Arduino for Reset ");

        if (commPort.isOpen()) {
            System.out.println("\n" + commPort.getSystemPortName() + "  is Open ");
        } else {
            System.out.println(" Port not open ");
            System.exit(-1);
        }

        // Display the Serial Port parameters
        System.out.println("\n Selected Port               = " + commPort.getSystemPortName());
        System.out.println(" Selected Baud rate          = " + commPort.getBaudRate());
        System.out.println(" Selected Number of DataBits = " + commPort.getNumDataBits());
        System.out.println(" Selected Number of StopBits = " + commPort.getNumStopBits());
        System.out.println(" Selected Parity             = " + commPort.getParity());

        Thread.sleep(2000); // Delay introduced because when the SerialPort is opened ,Arduino gets resetted
                            // Time for the code in Arduino to rerun after Reset

        try {
            byte[] WriteByte = "on".getBytes();

            int bytesTxed = 0;

            bytesTxed = commPort.writeBytes(WriteByte, WriteByte.length);

            System.out.print(" Bytes Transmitted -> " + bytesTxed);

        } catch (Exception e) {
            e.printStackTrace();
        }

        commPort.closePort(); // Close the port

        if (commPort.isOpen())
            System.out.println(commPort.getSystemPortName() + " is Open ");
        else
            System.out.println("\n Port not open ");

        commPort.flushIOBuffers();
    }

}
