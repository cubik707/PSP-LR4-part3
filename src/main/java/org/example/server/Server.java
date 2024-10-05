package org.example.server;

import java.net.*;
import java.io.*;
import java.util.Arrays;

public class Server {
    static int countclients = 0; // счетчик подключившихся клиентов

    public static void main(String args[]) throws IOException {
        ServerSocket sock = null;
        InputStream is = null;
        OutputStream os = null;

        try {
            sock = new ServerSocket(1024);

            while (true) {
                Socket client = sock.accept();
                countclients++;
                System.out.println("=======================================");
                System.out.println("Client " + countclients + " connected");
                is = client.getInputStream();
                os = client.getOutputStream();

                boolean flag = true;
                while (flag) {
                    byte[] bytes = new byte[1024];
                    is.read(bytes); // читаем информацию от клиента
                    String str = new String(bytes, "UTF-8").trim(); // переводим в строку и убираем пробелы

                    // Сортируем символы
                    char[] charArray = str.toCharArray();
                    Arrays.sort(charArray);
                    String sortedStr = new String(charArray);

                    // Отправляем отсортированную строку клиенту
                    os.write(sortedStr.getBytes());
                }
            }
        } catch (Exception e) {
            System.out.println("Error " + e.toString());
        } finally {
            if (is != null) is.close(); // закрытие входного потока
            if (os != null) os.close(); // закрытие выходного потока
            if (sock != null) sock.close(); // закрытие серверного сокета
            System.out.println("Client " + countclients + " disconnected");
        }
    }

}