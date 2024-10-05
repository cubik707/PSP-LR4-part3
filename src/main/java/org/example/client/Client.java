package org.example.client;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class Client  extends Frame implements ActionListener, WindowListener {
    TextField tf, tf1, tf2;
    TextArea ta;
    Socket sock = null;
    InputStream is = null;
    OutputStream os = null;

    public static void main(String args[]) {
        Client c = new Client();
        c.GUI();
    }

    private void GUI() {
        setTitle("КЛИЕНТ");
        tf = new TextField("127.0.0.1"); // IP адрес клиента
        tf1 = new TextField("1024"); // порт клиента
        tf2 = new TextField(); // поле для ввода букв
        ta = new TextArea(); // текстовая область для вывода результатов
        Button btnConnect = new Button("Connect");
        Button btnSend = new Button("Send");

        tf.setBounds(200, 50, 70, 25);
        tf1.setBounds(330, 50, 70, 25);
        tf2.setBounds(150, 100, 200, 25);
        btnConnect.setBounds(50, 50, 70, 25);
        btnSend.setBounds(50, 100, 70, 25);
        ta.setBounds(150, 150, 300, 200);

        add(tf);
        add(tf1);
        add(tf2);
        add(btnConnect);
        add(btnSend);
        add(ta);
        setSize(600, 400);
        setLayout(null);
        setVisible(true);
        addWindowListener(this);

        btnConnect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                connectToServer();
            }
        });

        btnSend.addActionListener(this);

        // Добавляем KeyListener для фильтрации ввода
        tf2.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                // Запретить ввод цифр
                if (!Character.isLetter(c) && !Character.isWhitespace(c)) {
                    e.consume(); // Отменяем событие
                }
            }
        });
    }

    private void connectToServer() {
        try {
            sock = new Socket(InetAddress.getByName(tf.getText()), Integer.parseInt(tf1.getText()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (sock == null) {
            ta.append("Не подключены к серверу\n");
            return;
        }
        try {
            is = sock.getInputStream();
            os = sock.getOutputStream();

            String letters = tf2.getText();

            // Проверка на пустое поле
            if (letters.trim().isEmpty()) {
                ta.append("Поле ввода букв не может быть пустым\n");
                return;
            }

            os.write(letters.getBytes()); // отправляем данные на сервер

            byte[] bytes = new byte[1024];
            int bytesRead = is.read(bytes); // получаем отсортированные буквы от сервера
            String sortedStr = new String(bytes, 0, bytesRead, "UTF-8"); // преобразуем байты в строку
            ta.append("Отсортированные буквы: " + sortedStr + "\n");
        } catch (IOException ex) {
            ta.append("Ошибка: " + ex.getMessage() + "\n");
        }
    }

    public void windowClosing(WindowEvent we) {
        if (sock != null && !sock.isClosed()) {
            try {
                sock.close(); // закрываем сокет
            } catch (IOException e) {}
        }
        this.dispose(); // закрываем окно
    }


    public void windowActivated(WindowEvent we) {}
    public void windowClosed(WindowEvent we) {}
    public void windowDeactivated(WindowEvent we) {}
    public void windowDeiconified(WindowEvent we) {}
    public void windowIconified(WindowEvent we) {}
    public void windowOpened(WindowEvent we) {}
}
