package Clientes;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import jdk.nashorn.internal.codegen.CompilerConstants;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author leandroall
 */


 public class Cliente2 extends Socket implements Runnable{
    public static int varControle;
    private Socket cliente;
    public static String buffer;
    public String nome;

    public Cliente2(Socket cliente){
        this.cliente = cliente;
    }

    public static void main(String args[]) throws IOException  {
        
       varControle= 1;
       buffer="a";
       conectaAoServidor(11111);
        System.out.println("========================================");
        System.out.println(buffer);
    }
    
    public static boolean conectaAoServidor(int porta) throws UnknownHostException, IOException{
        
         // para se conectar ao servidor, cria-se objeto Socket.
        // O primeiro parâmetro é o IP ou endereço da máquina que
        // se quer conectar e o segundo é a porta da aplicação.
        // Neste caso, usa-se o IP da máquina local (127.0.0.1)
        // e a porta da aplicação ServidorDeEco (12345).
        Socket socket = new Socket("127.0.0.1", porta);

        /*Cria um novo objeto Cliente com a conexão socket para que seja executado em um novo processo.
        Permitindo assim a conexão de vário clientes com o servidor.*/
        Cliente2 c = new Cliente2(socket);
        c.nome = "Cliente 2";
        Thread t = new Thread(c);
        t.start();
        while(t.isAlive());
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
            while(buffer.compareTo("s")!=0){
                // Envia a menssagem ao servidor
                buffer=teclado.nextLine();
                saida.println(buffer);
                
                  //Exibe mensagem de confirmação do servidor
            
                System.out.println(entrada.nextLine());
                
               
            
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
