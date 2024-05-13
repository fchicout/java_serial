package br.edu.uniaeso;

import java.util.Scanner;
import com.fazecast.jSerialComm.SerialPort;

public class App {

    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        // Configuração da comunicação serial
        ArduinoSerialCommunicator asc = new ArduinoSerialCommunicator(115200, 8, SerialPort.ONE_STOP_BIT,
                SerialPort.NO_PARITY);

        System.out.println("\n\n SerialPort Data Transmission");

        // Exibição das portas seriais disponíveis
        asc.getPortsInfo();

        System.out.println("Closing ports");
        // Fechamento de todas as portas seriais abertas
        asc.closeAllPorts();

        // Seleção da porta COM6 (pode ser alterada conforme necessário)
        SerialPort commPort = asc.getCOMMPortByName("COM6");

        // Configuração dos parâmetros da porta serial
        asc.setSerialParameters();

        // Abertura da porta serial
        commPort.openPort(2000);
        
        System.out.println(" Watch Arduino for Reset ");

        // Verificação se a porta serial está aberta
        if (!asc.verifyPortOpen()) {
            System.out.println(" Port not open ");
            System.exit(-1);
        }

        // Exibição das informações da porta serial selecionada
        asc.showSelectedPortInfo();

        // Aguardar um breve intervalo após a abertura da porta serial
        Thread.sleep(2000); 

        // Loop para solicitar comandos ao usuário até que seja digitado "/quit"
        String comando;
        do {
            System.out.print("Digite o comando a ser enviado para o Arduino (ou '/quit' para sair): ");
            comando = scanner.nextLine();
            if (!comando.equals("/quit")) {
                // Envio do comando para o dispositivo
                asc.writeMessage(comando);
                System.out.println(asc.readMessage());
            }
        } while (!comando.equals("/quit"));

        // Fechamento da porta serial
        asc.closeCOMMPort();

        // Verificação adicional se a porta serial foi fechada corretamente
        if (!asc.verifyPortOpen()) {
            System.out.println(" Port not open ");
            System.exit(-1);
        }

        // Fechamento do scanner
        scanner.close();
    }
}
