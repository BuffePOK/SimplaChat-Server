import server.ServerGate;
import tools.LogType;
import tools.SaveLogs;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            ServerGate serverGate = new ServerGate();
            serverGate.findConnection();
        } catch (IOException e) {
            SaveLogs.saveLog(LogType.ERROR, e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
