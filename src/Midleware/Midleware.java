/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Midleware;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author leandroall
 */
public class Midleware extends java.applet.Applet implements Runnable{
    public Socket cliente;
    private static String controleM;
    public static String ArrayServidores[] ={ "11111","11112","11113","11114"};

    public Midleware(Socket cliente){
        this.cliente = cliente;
        
    }

    public static void main(String[] args)  throws IOException{  
            
          levantaServidorNaPorta(11110);
         
    }

    /* A classe Thread, que foi instancia no servidor, implementa Runnable.
       Então você terá que implementar sua lógica de troca de mensagens dentro deste método 'run'.
    */
    public static void levantaServidorNaPorta(int porta) throws IOException{
         

        while (true) {
            //Cria um socket na porta 1111
        ServerSocket servidor = new ServerSocket (porta);
        System.out.println("Porta 11110 aberta!");

        // Aguarda alguém se conectar. A execução do servidor
        // fica bloqueada na chamada do método accept da classe
        // ServerSocket. Quando alguém se conectar ao servidor, o
        // método desbloqueia e retorna com um objeto da classe
        // Socket, que é uma porta da comunicação.
        System.out.println("Aguardando conexão do cliente...");    
          Socket cliente = servidor.accept();
          // Cria uma thread do servidor para tratar a conexão
          Midleware tratamento = new Midleware(cliente);
          Thread t = new Thread(tratamento);
          // Inicia a thread para o cliente conectado
           t.start();
           while(t.isAlive());
           servidor.close();
        }
    }
    
    public static String recuperaListaDeTodosArqs() throws IOException{
        String resp="";
        for (int i = 0; i < ArrayServidores.length ; i++) {
           Socket socket = new Socket("localhost", Integer.parseInt(ArrayServidores[i]));
           Scanner entrada = null;
           entrada = new Scanner(socket.getInputStream());
           PrintStream saida;
           saida = new PrintStream(socket.getOutputStream());
           saida.println("Req");
           resp=resp+entrada.nextLine();
           socket.close();
        }
        return resp;
    }
    
    public static String recuperaArqsDeCliente(String hostAdress){
        String resp="";
        for (int i = 0; i < ArrayServidores.length ; i++) {
           Socket socket;
            try {
                socket = new Socket("localhost", Integer.parseInt(ArrayServidores[i]));
                if(!socket.getInetAddress().getHostAddress().equals(hostAdress)) continue;
                Scanner entrada = null;
                entrada = new Scanner(socket.getInputStream());
                PrintStream saida;
                saida = new PrintStream(socket.getOutputStream());
                saida.println("Req");
                resp=resp+entrada.nextLine();
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(Midleware.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return resp;
    }
    
    public void run(){
        System.out.println("Nova conexao com o cliente " + this.cliente.getInetAddress().getHostName() + " " + this.cliente.getInetAddress().getHostAddress());           
        try {
            
            // Cria o objeto para receber as mensagens
            Scanner entrada = null;
            entrada = new Scanner(this.cliente.getInputStream());
            
            //Cria  objeto para enviar a mensagem ao servidor
            PrintStream saida;
            saida = new PrintStream(this.cliente.getOutputStream());

             controleM="d";
            while(controleM.compareTo("s")!=0){
                // lê a menssagem do cliente e exibe mensagem no console
                controleM=(entrada.nextLine());
                // envia pro cliente  a porta que ele deve se conectar
                if(controleM.compareTo("1")==0){
                 saida.println(ArrayServidores[0]);
                controleM="s";
                break;
               }else{
                    if(controleM.compareTo("Lista")==0){
                        saida.println(recuperaListaDeTodosArqs());
                        controleM="s";
                    }else{
                    saida.println("recebido" + controleM);
                    }
                }
            }
            //F
            //finaliza objetos
            saida.close();
            entrada.close();
            this.cliente.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
