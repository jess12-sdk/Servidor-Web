package redes;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;

//import org.omg.CORBA.portable.InputStream;
//import org.omg.CORBA.portable.OutputStream;

final class HttpRequest implements Runnable {
    
    private Socket socket;
    final static String CRLF = "\r\n";
    
    public HttpRequest(Socket socket) {
        this.socket = socket;
    }
    
    @Override
    public void run() {
        try {
            processRequest();
        } catch (Exception e) {
            System.out.println(e);
        }

    }
    
    private void processRequest() throws Exception{
        
        //cria um BufferedReader a partir do InputStream do cliente
            BufferedReader buffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Requisição: ");
            
            /*//Lê a primeira linha
            String linha = buffer.readLine();
            
            //Enquanto a linha não for vazia
            while (!linha.isEmpty()) {
                //imprime a linha
                System.out.println(linha);
                //lê a proxima linha
                linha = buffer.readLine();
            }*/
            
            
            /* Lê a primeira linha
             contem as informaçoes da requisição
             */
            String linha = buffer.readLine();
            //quebra a string pelo espaço em branco
            String[] dadosReq = linha.split(" ");
            //pega o metodo
            String metodo = dadosReq[0];
            //paga o caminho do arquivo
            String caminhoArquivo = dadosReq[1];
            //pega o protocolo
            String protocolo = dadosReq[2];
            //Enquanto a linha não for vazia
            
            while (!linha.isEmpty()) {
                
                //imprime a linha
                System.out.println(linha);
                //lê a proxima linha
                linha = buffer.readLine();
            }
            
            //se o caminho foi igual a / entao deve pegar o /index.html
            if (caminhoArquivo.equals("/")) {
                caminhoArquivo = "/home/jessica/NetBeansProjects/TrabalhoRedes/src/redes/index.html";
            }
            
            //abre o arquivo pelo caminho
            File arquivo = new File(caminhoArquivo);
            byte[] conteudo;
            
            //status de sucesso - HTTP/1.1 200 OK
            String status = protocolo + " 200 OK\r\n";
            //se o arquivo não existe então abrimos o arquivo de erro, e mudamos o status para 404
            if (!arquivo.exists()) {
                status = protocolo + " 404 Not Found\r\n";
                arquivo = new File("/home/jessica/NetBeansProjects/TrabalhoRedes/src/redes/404.html");
            }
            conteudo = Files.readAllBytes(arquivo.toPath());
            
            //cria um formato para o GMT espeficicado pelo HTTP
            SimpleDateFormat formatador = new SimpleDateFormat("E, dd MMM yyyy hh:mm:ss", Locale.ENGLISH);
            formatador.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date data = new Date();
            //Formata a dara para o padrao
            String dataFormatada = formatador.format(data) + " GMT";
            //cabeçalho padrão da resposta HTTP
            String header = status
                    + "Location: https://localhost:8000/\r\n"
                    + "Date: " + dataFormatada + "\r\n"
                    + "Server: MeuServidor/1.0\r\n"
                    + "Content-Type: text/html\r\n"
                    + "Content-Length: " + conteudo.length + "\r\n"
                    + "Connection: close\r\n"
                    + "\r\n";
            
            //cria o canal de resposta utilizando o outputStream
            OutputStream resposta = socket.getOutputStream();
            //escreve o headers em bytes
            resposta.write(header.getBytes());
            //escreve o conteudo em bytes
            resposta.write(conteudo);
            //encerra a resposta
            resposta.flush();
        
        
        
        
        
        
        
        
        
        /*// Obter uma referência para os trechos de entrada e saída do socket.
        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream os = new DataOutputStream(socket.getOutputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(input));

        // Obter a linha de requisição da mensagem de requisição HTTP.
        String requestLine = br.readLine();
        String[] dadosReq = requestLine.split(" ");
        //pega o metodo
        String metodo = dadosReq[0];
        //paga o caminho do arquivo
        String caminhoArquivo = dadosReq[1];
        //pega o protocolo
        String protocolo = dadosReq[2];
        //Enquanto a linha não for vazia

        // Obter e exibir as linhas de cabeçalho.
        //String headerLine = null;
        //while ((headerLine = br.readLine()).length() != 0) {
         //   System.out.println(headerLine);
        //}
        
        while (!requestLine.isEmpty()) {
                
            //imprime a linha
            System.out.println(requestLine);
            //lê a proxima linha
            requestLine = br.readLine();
        }

        // Extrair o nome do arquivo a linha de requisição.
        //StringTokenizer tokens = new StringTokenizer(requestLine);
        //tokens.nextToken(); // pular o método, que deve ser “GET”
        //String fileName = tokens.nextToken();

        // Acrescente um “.” de modo que a requisição do arquivo esteja dentro do diretório atual.
        //fileName = "." + fileName;
        
        //se o caminho foi igual a / entao deve pegar o /index.html
        if (caminhoArquivo.equals("/")) {
            caminhoArquivo = "/home/jessica/NetBeansProjects/TRedes/src/redes/index.html";
        }
        
        // Abrir o arquivo requisitado.
        File fis;
        Boolean fileExists = true;
        
        try {
            fis = new File(caminhoArquivo);
            //byte[] conteudo;
            // Construir a mensagem de resposta.
            String statusLine = null;
            String contentTypeLine = null;
            byte[] entityBody;
            
            if (fileExists) {
                statusLine = protocolo + "200 OK";
                contentTypeLine = "Content-type: " + contentType( caminhoArquivo ) + CRLF;
                entityBody = Files.readAllBytes(fis.toPath());
            } else {
                statusLine = "404 Not found";
                contentTypeLine = "Content-type:" + contentType( caminhoArquivo ) + CRLF;
                fis = new File("/home/jessica/NetBeansProjects/TRedes/src/redes/404.html");
            }

            // Enviar a linha de status.
            os.writeBytes(statusLine);
            // Enviar a linha de tipo de conteúdo.
            os.writeBytes(contentTypeLine);
            os.writeBytes(entityBody);
            // Enviar uma linha em branco para indicar o fim das linhas de cabeçalho.
            os.writeBytes(CRLF);

            // Enviar o corpo da entidade.
            if (fileExists) {
                sendBytes(fis, os);
                fis.close();
            } else {
                os.writeBytes(statusLine);
            }

        }catch (FileNotFoundException e) {
            fileExists = false;
        }

        os.close();
        br.close();
        socket.close();*/

    }
    
    /*private static void sendBytes(File fis, DataOutputStream os) throws Exception {
        
        // Construir um buffer de 1K para comportar os bytes no caminho para o socket.
        byte[] buffer = new byte[1024];
        int bytes = 0;
        
        // Copiar o arquivo requisitado dentro da cadeia de saída do socket.
        while((bytes = fis.read(buffer)) != -1 ) {
            os.write(buffer, 0, bytes);
        }
    }
    
    private static String contentType(String fileName) {
        if(fileName.endsWith(".htm") || fileName.endsWith(".html")) {
            return "text/html";
        }
        return "application/octet-stream";
    }*/
}