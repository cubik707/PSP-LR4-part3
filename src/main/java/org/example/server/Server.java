package org.example.server;

import java.net.*;
import java.io.*;
import java.util.Arrays;

public class Server {
    static int countclients = 0; // счетчик подключившихся клиентов

    public static void main(String args[]) throws IOException {
        ServerSocket sock = null;

        try {
            sock = new ServerSocket(1024);

            while (true) {
                Socket client = sock.accept();
                countclients++;
                System.out.println("=======================================");
                System.out.println("Client " + countclients + " connected");

                try (InputStream is = client.getInputStream();
                     OutputStream os = client.getOutputStream()) {

                    boolean flag = true;
                    while (flag) {
                        byte[] bytes = new byte[1024];
                        int readBytes = is.read(bytes); // читаем информацию от клиента

                        if (readBytes == -1) { // клиент отключился
                            flag = false;
                            System.out.println("Client " + countclients + " disconnected");
                            break;
                        }

                        String str = new String(bytes, 0, readBytes, "UTF-8").trim(); // переводим в строку и убираем пробелы

                        // Сортируем символы
                        char[] charArray = str.toCharArray();
                        Arrays.sort(charArray);
                        String sortedStr = new String(charArray);

                        // Отправляем отсортированную строку клиенту
                        os.write(sortedStr.getBytes());
                    }
                } catch (IOException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("Error " + e.toString());
        } finally {
            if (sock != null) sock.close(); // закрытие серверного сокета
            System.out.println("Client " + countclients + " disconnected");
        }
    }

}