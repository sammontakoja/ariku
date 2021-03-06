package ariku.rest.backend;

import ariku.settings.RestSettings;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * @author Ari Aaltonen
 */
public class Util {

    static RestSettings restSettings = new RestSettings();

    public static void startServerAndLetClientKnowAboutTCPPort() {
        int freePort = freePort();
        ArikuRest.start("localhost", freePort);
        restSettings.port = new Integer(freePort).toString();
    }

    private static int freePort() {
        try (ServerSocket socket = new ServerSocket(0)) {
            socket.setReuseAddress(true);
            return socket.getLocalPort();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
