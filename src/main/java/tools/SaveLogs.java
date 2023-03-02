package tools;

import java.io.*;

public class SaveLogs {
    private static final String LOG_PATH = System.getProperty("user.dir") + "/src/main/resources/databank/info.log";
    public static synchronized void saveLog(LogType logType, String logString) {
        String log = readLogFile();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(log);

        stringBuilder.append("[ ").append(logType).append(" ] ").append("UNIX Time: ").append(System.currentTimeMillis()).append(" . ");
        stringBuilder.append("Message: ").append(logString).append("\n");

        writeLogFile(stringBuilder.toString());
    }

    private static String readLogFile() {
        try {
            FileReader fr = new FileReader(LOG_PATH);
            BufferedReader br = new BufferedReader(fr);

            String ans;
            StringBuilder stringBuilder = new StringBuilder();
            while ((ans = br.readLine()) != null)
                stringBuilder.append(ans + "\n");

            fr.close();
            br.close();
            return stringBuilder.toString();


        } catch (FileNotFoundException e) {
            final String firstSt = """
                    -------------------------------------------------------------------------------------------------------------------------
                    INFO = Information about starting connections.
                    ERROR = Internal server errors.
                    WARNING = Errors during the exchange of messages between the client and the server (Including intentional client errors).
                    GETSEND_MESSAGE = The server received the message and sent it to the addressee.
                    ADDQUEUE_MESSAGE = The server received the message, but the recipient is not online.
                    SENDQUEUE_MESSAGE = Send all accumulated messages to the addressee.
                    DISCONNECTION = Disconnecting the client.
                    BREAK_CONNECTION = Failed to get IO Streams.
                    AUTHORIZATION = Client authorization.
                    REGISTRATION = Client registration.
                    -------------------------------------------------------------------------------------------------------------------------
                    """;
            writeLogFile(firstSt);
            return firstSt;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeLogFile(String logString) {
        try {
            FileWriter fileWriter = new FileWriter(LOG_PATH);
            fileWriter.write(logString);
            fileWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
