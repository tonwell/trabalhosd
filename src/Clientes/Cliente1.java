package Clientes;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author leandroall
 */


 public class Cliente1 implements Runnable{
    private static int  portaAConectar;
    public Socket cliente;
    private static String controleC;
    public String nome;

    public Cliente1(Socket cliente){
        this.cliente = cliente;
    }

    public static void main(String args[]) throws IOException  {
        
       portaAConectar= 11110;
       controleC="1";
       while(controleC.compareTo("sair")!=0){
         conectaAoServidor(portaAConectar);
         controleC="d";
       }
    }
    
    public static boolean conectaAoServidor(int porta) throws UnknownHostException, IOException{
        
         // para se conectar ao servidor, cria-se objeto Socket.
        // O primeiro parâmetro é o IP ou endereço da máquina que
        // se quer conectar e o segundo é a porta da aplicação.
        // Neste caso, usa-se o IP da máquina local (127.0.0.1)
        // e a porta da aplicação ServidorDeEco (12345).
        Socket socket = new Socket("localhost", porta);
        /*Cria um novo objeto Cliente com a conexão socket para que seja executado em um novo processo.
        Permitindo assim a conexão de vário clientes com o servidor.*/
        Cliente1 c = new Cliente1(socket);
        c.nome = "Cliente 1";
        Thread t = new Thread(c);
        t.start();
        while(t.isAlive());
        socket.close();
        return true;
    }

    public void run() {
        try {
            PrintStream saida;
            System.out.println("O cliente " + this.nome + "conectou ao servidor");

            //Prepara para leitura do teclado
            Scanner teclado = new Scanner(System.in);

            //Cria  objeto para enviar a mensagem ao servidor
            saida = new PrintStream(this.cliente.getOutputStream());
            
            // Cria o objeto para receber as respostas do servidor
            Scanner entrada = null;
            entrada = new Scanner(this.cliente.getInputStream());

            //Envia mensagem ao servidor
            while(controleC.compareTo("s")!=0){
                // Envia a menssagem ao servidor
                if(controleC.compareTo("1")==0){
                    saida.println(controleC); 
                    controleC="s";
                    //System.out.println(entrada.nextLine());
                    String entradalida=entrada.nextLine();
                    portaAConectar=Integer.parseInt(entradalida);
                    break;
                    
                }else{
                 
                controleC="Lista";
                saida.println(controleC);                
                //Exibe mensagem de confirmação do servidor
                System.out.println(entrada.nextLine());
                }
                
               
            
            }
            entrada.close();
            saida.close();
            teclado.close();
            this.cliente.close();
            System.out.println("Fim do cliente!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
