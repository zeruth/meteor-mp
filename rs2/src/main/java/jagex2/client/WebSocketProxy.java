package jagex2.client;

import android.annotation.SuppressLint;
import client.events.LoggerMessage;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.rationalityfrontline.kevent.KEventKt;

import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

public class WebSocketProxy {
    public static final int TCP_PORT = 6666; // The port for your TCP connections
    public static String WEBSOCKET = "wss://w1-2004.lostcity.rs"; // WebSocket target URI
    public static int WORLD = 1;

    static Thread proxyThread;
    static Thread tcpProxyThread;

    public static void start() throws IOException, InterruptedException, URISyntaxException {
        // Create a WebSocket client that connects to the wss:// WebSocket server
        WebSocketClient webSocketClient = new WebSocketClient(new URI(WEBSOCKET)) {
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

        AtomicBoolean proxyStarted = new AtomicBoolean(false);

        if (proxyThread != null) {
            proxyThread.interrupt();
        }
        proxyThread = new Thread(() -> {
            // Create a ServerSocket to accept TCP connections
            try (ServerSocket serverSocket = new ServerSocket(TCP_PORT)) {
                proxyStarted.set(true);
                Socket tcpSocket = serverSocket.accept();

                if (tcpProxyThread != null) {
                    tcpProxyThread.interrupt();
                }
                tcpProxyThread = new Thread(() -> {
                    handleTcpConnection(tcpSocket, webSocketClient);
                });
                tcpProxyThread.start();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        proxyThread.start();

        while (!proxyStarted.get()) {
            Thread.sleep(5);
        }

        webSocketClient.addHeader("Host", "w" + WORLD +"-2004.lostcity.rs");
        webSocketClient.addHeader("Origin", "https://w" + WORLD + "-2004.lostcity.rs");
        webSocketClient.addHeader("Referer", "https://w" + WORLD + "-2004.lostcity.rs/rs2.cgi?plugin=0&world=" + WORLD + "&lowmem=0");
        webSocketClient.connect();
    }

    private static OutputStream out = null;

    public static WebSocketClient webSocketClient = null;

    @SuppressLint("NewApi")
    private static void handleTcpConnection(Socket tcpSocket, WebSocketClient webSocketClient) {
        try (InputStream tcpIn = tcpSocket.getInputStream(); OutputStream tcpOut = tcpSocket.getOutputStream()) {
            WebSocketProxy.webSocketClient = webSocketClient;
            KEventKt.getKEVENT().post(new LoggerMessage("WebSocket", "Set proxy: " + WEBSOCKET + " to localhost:" + TCP_PORT));
            out = tcpOut;

            while (!webSocketClient.isClosed()) {
                byte[] in = tcpIn.readAllBytes();
                if (in.length > 0) {
                    webSocketClient.send(in);
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
