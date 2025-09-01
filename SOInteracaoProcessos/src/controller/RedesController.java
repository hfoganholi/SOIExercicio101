package controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class RedesController {

    private String os() {
    	return System.getProperty("os.name");
    }

    public void ip() {
        System.out.println("Sistema Operacional: " + os());
        String comando = os().contains("Windows") ? "ipconfig" : "ifconfig";

        try {
            Process processo = Runtime.getRuntime().exec(comando);
            BufferedReader leitor = new BufferedReader(new InputStreamReader(processo.getInputStream()));
            String linha;

            String adaptadorAtual = null;
            String ipv4 = null;

            while ((linha = leitor.readLine()) != null) {
                linha = linha.trim(); // 🔹 remover espaços extras

                if (!os().contains("Windows")) { // Linux/mac
                    if (linha.contains("flags")) {
                        // Novo adaptador
                        adaptadorAtual = linha.split(":")[0].trim();
                        ipv4 = null; // zera até achar
                    } else if (linha.startsWith("inet ") && !linha.contains("127.0.0.1")) {
                        // Só pega IPv4 (ignora inet6 e loopback)
                        ipv4 = linha.split(" ")[1].trim();
                    }

                    // Se já temos adaptador + IPv4, mostramos e limpamos
                    if (adaptadorAtual != null && ipv4 != null) {
                        System.out.println(adaptadorAtual + " -> " + ipv4);
                        adaptadorAtual = null;
                        ipv4 = null;
                    }
                } else { // Windows
                    if (linha.contains("Adaptador")) {
                        adaptadorAtual = linha.trim();
                    } else if (linha.contains("IPv4")) {
                        ipv4 = linha.split(":")[1].trim();
                    }

                    if (adaptadorAtual != null && ipv4 != null) {
                        System.out.println(adaptadorAtual + " -> " + ipv4);
                        adaptadorAtual = null;
                        ipv4 = null;
                    }
                }
            }
            leitor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(" ");
    }



    public void ping() {
        System.out.println("Sistema Operacional: " + os());
        String comando = os().contains("Windows") 
                ? "ping -4 -n 10 www.google.com.br" 
                : "ping -4 -c 10 www.google.com.br";

        try {
            Process processo = Runtime.getRuntime().exec(comando);
            BufferedReader leitor = new BufferedReader(new InputStreamReader(processo.getInputStream()));
            String linha;

            while ((linha = leitor.readLine()) != null) {
                linha = linha.trim();

                if (!os().contains("Windows") && linha.contains("rtt min/avg/max")) {
                    // Linux/mac
                    String valores = linha.split("=")[1].trim(); // pega a parte "36.218/45.559/72.231/10.155 ms"
                    String avg = valores.split("/")[1];          // pega o segundo campo (médio)
                    System.out.println("Tempo médio do ping: " + avg + " ms");
                } else if (os().contains("Windows") && linha.contains("Média")) {
                    // Windows
                    String[] partes = linha.split(",");
                    for (String parte : partes) {
                        if (parte.contains("Média")) {
                            System.out.println("Tempo médio do ping: " + parte.split("=")[1].trim());
                        }
                    }
                }
            }
            leitor.close();

            int exitCode = processo.waitFor();
            System.out.println("Processo finalizado com código: " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(" ");
    }

}
