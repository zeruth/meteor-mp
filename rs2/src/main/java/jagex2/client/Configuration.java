package jagex2.client;

import java.math.BigInteger;

import static client.Client.RSA_MODULUS;

public class Configuration {
    public static String CODEBASE = "https://w1-2004.lostcity.rs";
/*    public static byte[] WEBSOCKET = {104, (byte) 167, (byte) 241,83};*/
    public static int PORT_OFFSET = 0;

    public static boolean INTERCEPT_GRAPHICS = false;

    public static boolean PROXY_WSS = true;

    public static void updatePortOffset(int offset) {
        PORT_OFFSET = offset;
    }

    public static void updateModulus(BigInteger modulus) {
        RSA_MODULUS = modulus;
    }
}
