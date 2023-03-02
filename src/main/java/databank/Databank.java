package databank;

import client.Client;
import generaLinfo.Message;
import tools.LogType;
import tools.LongGenerator;
import tools.SaveLogs;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Databank {
    private static final Set<Long> allUniqueIDs = loadAllIDs();
    private static final Map<Long, Client> clientConnections = new ConcurrentHashMap<>();
    private static final Map<Long, List<Message>> savedMessages = new ConcurrentHashMap<>();

    private static final String IDENTIFY_PATH = System.getProperty("user.dir") + "/src/main/resources/databank/allIDs.identify";

    private Databank() {}

    public static synchronized long addUniqueID() {
        long id;
        synchronized (allUniqueIDs) {
            id = LongGenerator.generate();
            allUniqueIDs.add(id);
            writeAllIDs(allUniqueIDs);
        }
        return id;
    }

    private synchronized static Set<Long> loadAllIDs() {
        try {
            Set<Long> uniqueIDs = new LinkedHashSet<>();

            FileReader fr = new FileReader(IDENTIFY_PATH);
            BufferedReader br = new BufferedReader(fr);

            String ans;
            while ((ans = br.readLine()) != null)
                uniqueIDs.add(Long.parseLong(ans));

            fr.close();
            br.close();
            return uniqueIDs;

        } catch (FileNotFoundException e) {
            writeAllIDs(new LinkedHashSet<>());
        } catch (IOException e) {
            SaveLogs.saveLog(LogType.ERROR, e.getMessage());
            throw new RuntimeException(e);
        }

        return new LinkedHashSet<>();
    }

    private static synchronized void writeAllIDs(Set<Long> allIDs) {
        try {
            FileWriter fw = new FileWriter(IDENTIFY_PATH);

            if(allIDs.size() == 0) fw.write("0\n");

            for(long IDs:allIDs)
                fw.write(IDs + "\n");

            fw.close();
        }
        catch (IOException e) {
            SaveLogs.saveLog(LogType.ERROR, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public synchronized static void addNewClient(Client client) {
        synchronized (clientConnections) {
            clientConnections.put(client.getUniqueID(), client);
        }
    }

    public synchronized static void dellClient(long id) {
        synchronized (clientConnections) {
            clientConnections.remove(id);
        }
    }

    public synchronized static boolean isOnline(long id) { return clientConnections.containsKey(id);}

    public synchronized static Client getClient(long id) {
        return clientConnections.get(id);
    }

    public static synchronized boolean isHasID(long id) {
        return allUniqueIDs.contains(id);
    }

    public static synchronized void saveMessage( Message message) {
        synchronized (savedMessages) {
            List<Message> messagesList = savedMessages.get(message.getToUserID());
            if(messagesList == null)
                messagesList = new LinkedList<>();

            messagesList.add(message);
            savedMessages.put(message.getToUserID(), messagesList);
        }
    }

    public static synchronized Message[] getMessages(long id) {
        synchronized (savedMessages) {
            List<Message> messagesList = savedMessages.get(id);
            if (messagesList == null)
                return new Message[0];

            Message[] messages = new Message[messagesList.size()];
            for (int index = 0; index < messagesList.size(); index++) {
                messages[index] = messagesList.get(index);
            }

            savedMessages.remove(id);

            return messages;
        }
    }
}
