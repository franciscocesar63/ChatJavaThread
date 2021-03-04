package devChat;

import java.io.BufferedWriter;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ServidorThChat extends Thread {

    private static ArrayList<BufferedWriter> clientes;
    private static ServerSocket server;
    private String nome;
    private static Socket con;
    private InputStream in;
    private InputStreamReader inr;
    private BufferedReader bfr;
    private static int x = 0;

    public ServidorThChat(Socket con) {
        this.con = con;
        try {
            in = con.getInputStream();
            inr = new InputStreamReader(in);
            bfr = new BufferedReader(inr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            server = new ServerSocket(1234);
            clientes = new ArrayList<BufferedWriter>();
            System.out.println("Servidor ativo ");

            while (true) {

                System.out.println("Aguardando conex�o...");
                try {
                    con = server.accept();

                } catch (Exception e) {
//                    System.err.println("Erro: " + e.getMessage());
                    return;
                }
                System.out.println("Cliente conectado...");
                Thread t = new ServidorThChat(con);
                t.start();
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        try {
            x++;

            String msg;
            OutputStream out = this.con.getOutputStream();//enviar dados para o cliente (writer)
            Writer ouw = new OutputStreamWriter(out);
            BufferedWriter bfw = new BufferedWriter(ouw);

            clientes.add(bfw);
            nome = msg = bfr.readLine();//recebe dados do cliente (reader)
            System.out.println("\n" + nome.toUpperCase() + " est� no chat");

            while (!"Sair".equalsIgnoreCase(msg) && msg != null) {

                x++;
                msg = bfr.readLine();
                enviarAll(bfw, msg);
                System.out.println(nome + " -> " + msg);

            }
            x++;
            clientes.remove(bfw);
            if (clientes.isEmpty()) {
                if (x != 0) {
                    server.close();
                    return;
                }
            }
//            System.out.print("saindo do run!!");
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public void enviarAll(BufferedWriter bwSaida, String msg) throws IOException {
        BufferedWriter bwS;

        for (BufferedWriter bw : clientes) {
            bwS = (BufferedWriter) bw;
            if (!(bwSaida == bwS)) {
                bw.write(nome + " -> " + msg + "\r\n");
                bw.flush();
            }
        }
    }

}
