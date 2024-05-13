package br.edu.uniaeso;

import com.fazecast.jSerialComm.SerialPort;

public class ArduinoSerialCommunicator {
    private int baudRate, dataBits, stopBits, parity;
    private SerialPort commPort;

    public SerialPort getCommPort() {
        return commPort;
    }

    public void setCommPort(SerialPort commPort) {
        this.commPort = commPort;
    }

    public int getBaudRate() {
        return baudRate;
    }

    public void setBaudRate(int baudRate) {
        this.baudRate = baudRate;
    }

    public int getDataBits() {
        return dataBits;
    }

    public void setDataBits(int dataBits) {
        this.dataBits = dataBits;
    }

    public int getStopBits() {
        return stopBits;
    }

    public void setStopBits(int stopBits) {
        this.stopBits = stopBits;
    }

    public int getParity() {
        return parity;
    }

    public void setParity(int parity) {
        this.parity = parity;
    }

    public ArduinoSerialCommunicator(int baudRate, int dataBits, int stopBits, int parity) {
        setBaudRate(baudRate);
        setDataBits(dataBits);
        setStopBits(stopBits);
        setParity(parity);

    }
    
    public void setSerialParameters(){
        // Set Serial port Parameters
        // parameters at one time
        getCommPort().setComPortParameters(getBaudRate(), getDataBits(), getStopBits(), getParity());// Sets all serial port

    }

    public boolean verifyPortOpen(){
        if (getCommPort().isOpen()) {
            System.out.println("\n" + commPort.getSystemPortName() + " is Open ");
            return true;
        } else {
            return false;
        }
    }

    public void showSelectedPortInfo(){
        // Display the Serial Port parameters
        System.out.println("\n Selected Port               = " + getCommPort().getSystemPortName());
        System.out.println(" Selected Baud rate          = " + getCommPort().getBaudRate());
        System.out.println(" Selected Number of DataBits = " + getCommPort().getNumDataBits());
        System.out.println(" Selected Number of StopBits = " + getCommPort().getNumStopBits());
        System.out.println(" Selected Parity             = " + getCommPort().getParity());
    }

    public int writeMessage(String msg){
        int bytesTxed = 0;
        try {
            byte[] writeByte = msg.getBytes();
            bytesTxed = commPort.writeBytes(writeByte, writeByte.length);
            System.out.println(" Bytes Transmitted -> " + bytesTxed);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytesTxed;
    }

    public String readMessage() {
        StringBuilder message = new StringBuilder();
        try {
            byte[] readBuffer = new byte[1024];
            int numRead = commPort.readBytes(readBuffer, readBuffer.length);
            if (numRead > 0) {
                message.append(new String(readBuffer, 0, numRead));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message.toString();
    }
    
    public void getPortsInfo() {
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

    public void closeAllPorts() {
        SerialPort[] ports = SerialPort.getCommPorts();
        for (SerialPort serialPort : ports) {
            if (serialPort.isOpen()) {
                serialPort.flushIOBuffers();
                serialPort.closePort();
            }
        }
    }

    public SerialPort getCOMMPortByName(String portName) {
        SerialPort[] ports = SerialPort.getCommPorts();
        for (SerialPort serialPort : ports) {
            if (serialPort.getSystemPortName().equals(portName)) {
                setCommPort(serialPort);
                return serialPort;
            }
        }
        return null;
    }

    public void closeCOMMPort() {
        getCommPort().flushIOBuffers();
        getCommPort().closePort();
    }
}
