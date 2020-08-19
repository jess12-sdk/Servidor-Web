package redes;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;

public class WebServer{
    
    // porta para ouvir a conexão
    static final int PORT =  6789;

    private Socket connect;
    
    public WebServer(Socket c) {
        connect = c;
    }
    
    public static void main(String[] args) throws IOException {
        ServerSocket serverConnect = new ServerSocket(PORT);
        System.out.println("Server started.\nListening for connections on port: " + PORT + " ...\n");
        
        // ouvimos até que o usuário pare a execução do servidor
        while (true) {
            WebServer myServer = new WebServer(serverConnect.accept());
            
            //Construir um objeto para processar a mensagem de requisição HTTP.
            HttpRequest request = new HttpRequest (myServer.getConnect());
            
            // Criar um novo thread para processar a requisição.
            Thread thread = new Thread(request);
            
            //Iniciar o thread.
            thread.start();
        }

    }
    
    public Socket getConnect() {
        return connect;
    }

    public void setConnect(Socket connect) {
        this.connect = connect;
    }

}
