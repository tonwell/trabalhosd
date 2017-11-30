/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidores;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.ArrayList;

/**
 *
 * @author leandroall
 */
public class Servidor1 extends java.applet.Applet implements Runnable{
    private Socket cliente1;
    private static ArrayList<String> arquivos = new ArrayList();
    private static String buffer;

    public Servidor1(Socket cliente){
        this.cliente1 = cliente;
    }

   
    public static void main(String[] args)  throws IOException{ 
        buffer="a";
        arquivos.add("a/Imagem1.png");
        arquivos.add("b/Imagem2.png");
        arquivos.add("a/Imagem3.png");
        arquivos.add("c/Imagem4.png");
        arquivos.add("a/Imagem5.png");
        arquivos.add("b/Imagem6.png");
        arquivos.add("a/Imagem7.png");
        levantaServidorNaPorta(11111);
      
    }

    /* A classe Thread, que foi instancia no servidor, implementa Runnable.
       Então você terá que implementar sua lógica de troca de mensagens dentro deste método 'run'.
    */
    public static void levantaServidorNaPorta(int porta) throws IOException{
          //Cria um socket na porta 11111
        ServerSocket servidor = new ServerSocket (porta);
        System.out.println("Porta 11111 aberta!");
        System.out.println("Listando arquivos locais: ");
        for(String arquivo: arquivos){
            System.out.println(" - "+arquivo);
        }

        // Aguarda alguém se conectar. A execução do servidor
        // fica bloqueada na chamada do método accept da classe
        // ServerSocket. Quando alguém se conectar ao servidor, o
        // método desbloqueia e retorna com um objeto da classe
        // Socket, que é uma porta da comunicação.
        System.out.println("Aguardando conexão do cliente...");   

        while (true) {
          Socket cliente = servidor.accept();
          // Cria uma thread do servidor para tratar a conexão
          Servidor1 tratamento = new Servidor1(cliente);
          Thread t = new Thread(tratamento);
          // Inicia a thread para o cliente1 conectado
          t.start();
        }
    }
    
    
    public static String executaListar() throws IOException{
            Socket socket = new Socket("localhost", 11110);
            PrintStream saida;
            saida = new PrintStream(socket.getOutputStream());
            Scanner entrada = null;
            entrada = new Scanner(socket.getInputStream());
            saida.println("Lista");
            String aux = "";
            if(entrada.hasNextLine())
                aux = entrada.nextLine();
            socket.close();
            return aux;
           
           
        
    }
    
    public void run(){
        System.out.println("Nova conexao com o cliente " + this.cliente1.getInetAddress().getHostAddress());
        
        try {
            
            // Cria o objeto para receber as mensagens
            Scanner entrada = null;
            entrada = new Scanner(this.cliente1.getInputStream());
            
            //Cria  objeto para enviar a mensagem ao servidor
            PrintStream saida;
            saida = new PrintStream(this.cliente1.getOutputStream());

            
            while(entrada.hasNextLine()){
                // lê a menssagem do cliente1 e exibe mensagem no console
                buffer=(entrada.nextLine());
                if(buffer.compareTo("Req")==0){
                    String resp="";
                    for (int i = 0; i < arquivos.size(); i++) {
                        resp=resp + arquivos.get(i) + "\n";
                    }
                    saida.println(resp);
                }else{
                    if(buffer.compareTo("Lista")==0){
                        saida.println(executaListar());
                        
                    }else{
                // envia pro cliente1 a confirmação
                      saida.println("Confirmo Recebimento");
                    }
                }
            }
     
            //Finaliza objetos
            saida.close();
            entrada.close();
            this.cliente1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
