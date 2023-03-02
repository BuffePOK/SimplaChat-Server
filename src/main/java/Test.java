import databank.Databank;
import generaLinfo.Message;
import generaLinfo.Talk;
import generaLinfo.TypeOfPartConnection;
import tools.LogType;
import tools.SaveLogs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

public class Test {
    public static void main(String[] args) throws Exception {
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(
                whatismyip.openStream()));

        String ip = in.readLine();
        System.out.println(ip);
    }
}
