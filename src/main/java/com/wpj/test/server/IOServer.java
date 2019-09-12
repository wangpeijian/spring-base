package com.wpj.test.server;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * @author wangpejian
 * @date 2019/9/12 上午10:37
 */

@Slf4j
public class IOServer {

    public static void main(String[] args) {
        new Server(8888).start();
        new Client(8888).start();
    }

    static class Server {

        private ServerSocket serverSocket;

        Server(int port) {
            try {
                this.serverSocket = new ServerSocket(port);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("启动失败");
            }
        }

        public void start() {

            new Thread(() -> {
                try {

                    System.out.println("启动服务");

                    while (true) {
                        Socket socket = serverSocket.accept();

                        new Thread(() -> {
                            try {
                                int len;
                                byte[] data = new byte[1024];
                                InputStream inputStream = socket.getInputStream();
                                len = inputStream.read(data);
                                System.out.println("收到客户端内容：" + new String(data, 0, len));

                                socket.shutdownInput();

                                OutputStream out = socket.getOutputStream();

                                StringBuilder res = new StringBuilder();
                                res.append("HTTP/1.1 200 OK \n\r");
                                res.append("Server: wpj \n\r");
                                res.append("Content-Type: application/json;charset=UTF-8 \n\r");
                                res.append("Connection: close \n\r");
                                res.append("\n");
                                res.append("{\"status\":\"1\",\"count\":\"2\",\"info\":\"OK\",\"infocode\":\"10000\",\"lives\":[{\"province\":\"湖南\",\"city\":\"长沙市\",\"adcode\":\"430100\",\"weather\":\"阵雨\",\"temperature\":\"25\",\"winddirection\":\"东北\",\"windpower\":\"7\",\"humidity\":\"78\",\"reporttime\":\"2018-05-06 16:00:00\"},{\"province\":\"湖南\",\"city\":\"长沙县\",\"adcode\":\"430121\",\"weather\":\"阵雨\",\"temperature\":\"25\",\"winddirection\":\"东北\",\"windpower\":\"7\",\"humidity\":\"78\",\"reporttime\":\"2018-05-06 16:00:00\"}]}");

                                out.write(res.toString().getBytes());
                                out.flush();
                                out.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }).start();
                    }

                } catch (IOException e) {
                    System.out.println("io 异常");
                }

            }).start();
        }
    }

    static class Client {

        private int port;

        public Client(int port) {
            this.port = port;
        }

        public void start() {
            new Thread(() -> {
                try {
                    while (true) {
                        Socket socket = new Socket("localhost", this.port);
                        OutputStream os = socket.getOutputStream();
                        os.write(("我是客户端").getBytes("utf-8"));
                        os.flush();
                        socket.shutdownOutput();

                        int len;
                        byte[] data = new byte[1024];
                        InputStream input = socket.getInputStream();
                        while ((len = input.read(data)) != -1) {
                            System.out.println("服务端响应内容：" + new String(data, 0, len));
                        }
                        TimeUnit.SECONDS.sleep(1);
                        input.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("接受不成功");
                }
            }).start();


        }
    }
}
