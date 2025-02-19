package jagex2.client;

import static client.Client.nodeId;

import client.Client;
import client.events.LoggerMessage;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class WebSocketProxy {
    public static final int LOCAL_TCP = 6666;
    public static String REMOTE_WSS = "wss://w1-2004.lostcity.rs";

    static Thread proxyThread;
    static Thread tcpProxyThread;

    public static void start() throws IOException, InterruptedException, URISyntaxException {
        final WebSocketClient webSocketClient = new WebSocketClient(new URI(REMOTE_WSS)) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
            }

            @Override
            public void onMessage(ByteBuffer bytes) {
                if (bytes.hasRemaining()) {
                    forwardToTcpClients(bytes);
                }
            }
            @Override
            public void onMessage(String message) {
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
            }

            @Override
            public void onError(Exception ex) {
                ex.printStackTrace();
            }
        };

        final AtomicBoolean proxyStarted = new AtomicBoolean(false);

        if (proxyThread != null) {
            proxyThread.interrupt();
        }
        final Socket[] tcpSocket = {null};

        tcpProxyThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    handleTcpConnection(tcpSocket[0], webSocketClient);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Thread proxyThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerSocket serverSocket = new ServerSocket(LOCAL_TCP);

                    proxyStarted.set(true);
                    tcpSocket[0] = serverSocket.accept();

                    if (tcpProxyThread != null) {
                        tcpProxyThread.interrupt();
                    }
                    tcpProxyThread.start();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        proxyThread.start();

        while (!proxyStarted.get()) {
            Thread.sleep(5);
        }
        int world = nodeId - 9;
        webSocketClient.addHeader("Host", "w" + world +"-2004.lostcity.rs");
        webSocketClient.addHeader("Origin", "https://w" + world + "-2004.lostcity.rs");
        webSocketClient.addHeader("Referer", "https://w" + world + "-2004.lostcity.rs/rs2.cgi?plugin=0&world=" + world + "&lowmem=0");
        webSocketClient.connect();
    }

    private static OutputStream out = null;

    public static WebSocketClient webSocketClient = null;

    private static void handleTcpConnection(Socket tcpSocket, WebSocketClient webSocketClient) throws IOException {
        InputStream tcpIn = tcpSocket.getInputStream();
        OutputStream tcpOut = tcpSocket.getOutputStream();

        try {
            WebSocketProxy.webSocketClient = webSocketClient;
            Client.client.post(new LoggerMessage("WebSocket", "Set proxy: " + REMOTE_WSS + " to localhost:" + LOCAL_TCP));
            out = tcpOut;

            byte[] buffer = new byte[1024];
            int bytesRead;

            while (!webSocketClient.isClosed()) {
                bytesRead = tcpIn.read(buffer);
                if (bytesRead > 0) {
                    webSocketClient.send(Arrays.copyOf(buffer, bytesRead));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void forwardToTcpClients(ByteBuffer message) {
        byte[] byteArray = new byte[message.remaining()];
        message.get(byteArray);

        try {
            out.write(byteArray);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
