package server;

import tools.LogType;
import tools.SaveLogs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

public class ServerGate {
    public void findConnection() throws IOException {
        ServerSocket serverSocket = new ServerSocket(65535);
        SaveLogs.saveLog(LogType.INFO, "Start server with: " + getMyIP() + ":" + serverSocket.getLocalPort());

        while (true) {
            Socket socket = serverSocket.accept();
            SaveLogs.saveLog(LogType.INFO, "Get new connection from: " + socket.getRemoteSocketAddress());

            try {
                ConnectionManagement management = new ConnectionManagement(socket);
                management.start();
            }
            catch (IOException e) {
                SaveLogs.saveLog(LogType.BREAK_CONNECTION, "Connection broken . Connection info: " + socket.getRemoteSocketAddress());
                System.err.println("Error. Line 19. ServerGate.class");
            }
        }
    }

    private String getMyIP() {
        URL whatismyip = null;
        try {
            whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));

            return in.readLine();
        } catch (IOException e) {
            SaveLogs.saveLog(LogType.ERROR, e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
