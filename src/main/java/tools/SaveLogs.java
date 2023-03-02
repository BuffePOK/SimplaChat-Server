package tools;

import java.io.*;

public class SaveLogs {
    private static final String LOG_PATH = System.getProperty("user.dir") + "/src/main/resources/databank/info.log";
    public static synchronized void saveLog(LogType logType, String logString) {
        String log = readLogFile();

        String logConstructor = log +
                "[ " + logType + " ] " + "UNIX Time: " + System.currentTimeMillis() + " . " +
                "Message: " + logString + "\n";

        writeLogFile(logConstructor);
    }

    private static String readLogFile() {
        try {
            FileReader fr = new FileReader(LOG_PATH);
            BufferedReader br = new BufferedReader(fr);

            String ans;
            StringBuilder stringBuilder = new StringBuilder();
            while ((ans = br.readLine()) != null)
                stringBuilder.append(ans).append("\n");

            fr.close();
            br.close();
            return stringBuilder.toString();


        } catch (FileNotFoundException e) {
            final String firstSt =
                    "-------------------------------------------------------------------------------------------------------------------------\n" +
                    "INFO = Information about starting connections.\n" +
                    "ERROR = Internal server errors.\n" +
                    "WARNING = Errors during the exchange of messages between the client and the server (Including intentional client errors).\n" +
                    "GETSEND_MESSAGE = The server received the message and sent it to the addressee.\n" +
                    "ADDQUEUE_MESSAGE = The server received the message, but the recipient is not online.\n" +
                    "SENDQUEUE_MESSAGE = Send all accumulated messages to the addressee.\n" +
                    "DISCONNECTION = Disconnecting the client.\n" +
                    "BREAK_CONNECTION = Failed to get IO Streams.\n" +
                    "AUTHORIZATION = Client authorization.\n" +
                    "REGISTRATION = Client registration.\n" +
                    "-------------------------------------------------------------------------------------------------------------------------\n";
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
