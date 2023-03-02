package server;

import client.Client;
import databank.Databank;
import generaLinfo.Message;
import generaLinfo.Talk;
import generaLinfo.TypeOfPartConnection;
import generaLinfo.User;
import tools.LogType;
import tools.SaveLogs;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConnectionManagement extends Thread {
    private final Socket socket;

    private final ObjectInputStream input;
    private final ObjectOutputStream output;

    private boolean isAuthorized = false;
    private boolean isConnection = true;

    private User user = null;

    public ConnectionManagement(Socket socket) throws IOException {
        this.socket = socket;

        input = new ObjectInputStream(socket.getInputStream());
        output = new ObjectOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        while (true) {
            if (!isConnection)
                break;

            Talk talk = null;
            try {
                talk = (Talk) input.readObject();
            } catch (IOException | ClassNotFoundException e) {
                closeConnection();
                break;
            }

            if (talk.type == TypeOfPartConnection.REGISTRATION) {
                long id = Databank.addUniqueID();
                Talk answer = new Talk(TypeOfPartConnection.REGISTRATION, new User(id), null);

                SaveLogs.saveLog(LogType.REGISTRATION, "Registered new user: " + id + " . Connection info: " + socket.getRemoteSocketAddress());

                try {
                    output.writeObject(answer);
                } catch (IOException e) {
                    closeConnection();
                }
            }

            if (talk.type == TypeOfPartConnection.AUTHENTICATION) {
                user = (User) talk.body;
                try {
                    if (user == null) {
                        output.writeObject(new Talk(TypeOfPartConnection.AUTHENTICATION, false, null));
                        SaveLogs.saveLog(LogType.WARNING, "AUTHORIZATION FAILED. USER == NULL. Connection info: " + socket.getRemoteSocketAddress());
                        closeConnection();
                        break;
                    }

                    if (Databank.isHasID(user.getUniqID())) {
                        Databank.addNewClient(new Client(user.getUniqID(), output));
                        isAuthorized = true;
                        output.writeObject(new Talk(TypeOfPartConnection.AUTHENTICATION, true, null));
                        SaveLogs.saveLog(LogType.AUTHORIZATION, "Connection info: " + socket.getRemoteSocketAddress() + " User ID: " + user.getUniqID());
                    } else {
                        output.writeObject(new Talk(TypeOfPartConnection.AUTHENTICATION, false, null));
                        SaveLogs.saveLog(LogType.WARNING, "AUTHORIZATION FAILED. USER NOT FIND. Connection info: " + socket.getRemoteSocketAddress() + " User ID: " + user.getUniqID());
                        closeConnection();
                    }
                } catch (IOException e) {
                    closeConnection();
                }
            }

            if (talk.type == TypeOfPartConnection.ALL_MESSAGES) {
                if (!isAuthorized) {
                    SaveLogs.saveLog(LogType.WARNING, "Try to get all messages, but not authorized. Connection info: " + socket.getRemoteSocketAddress());
                    closeConnection();
                    break;
                }
                try {
                    output.writeObject(new Talk(TypeOfPartConnection.ALL_MESSAGES,
                            Databank.getMessages(user.getUniqID()), null));
                    SaveLogs.saveLog(LogType.SENDQUEUE_MESSAGE, "Connection info: " + socket.getRemoteSocketAddress() + "Send QueueMessages to: " + user.getUniqID());
                } catch (IOException e) {
                    closeConnection();
                }
            }

            if (talk.type == TypeOfPartConnection.MESSAGE) {
                if (!isAuthorized) {
                    SaveLogs.saveLog(LogType.WARNING, "Try to send message, but not authorized. Connection info: " + socket.getRemoteSocketAddress());
                    closeConnection();
                    break;
                }

                Message message = (Message) talk.body;
                if (message == null) {
                    SaveLogs.saveLog(LogType.WARNING, "Try to send null message. Connection info: " + socket.getRemoteSocketAddress() + " . User ID: " + user.getUniqID());
                    closeConnection();
                    break;
                }
                if (message.getFromUserID() != user.getUniqID()) {
                    SaveLogs.saveLog(LogType.WARNING, "Try to send message from other person. Connection info: " + socket.getRemoteSocketAddress() + " . User ID: " + user.getUniqID());
                    closeConnection();
                    break;
                }

                long id = message.getToUserID();

                if (Databank.isOnline(id)) {
                    Client client = Databank.getClient(id);
                    try {
                        client.sendMessage(message, user);
                        SaveLogs.saveLog(LogType.GETSEND_MESSAGE, "Send message from user: " + message.getFromUserID() + " . To user: " + message.getToUserID());
                    } catch (IOException e) {
                        Databank.saveMessage(message);
                        SaveLogs.saveLog(LogType.ADDQUEUE_MESSAGE, "Message from user: " + message.getFromUserID() + " . To user: " + message.getToUserID() + " . Was added to queue.");
                    }
                } else {
                    Databank.saveMessage(message);
                    SaveLogs.saveLog(LogType.ADDQUEUE_MESSAGE, "Message from user: " + message.getFromUserID() + " . To user: " + message.getToUserID() + " . Was added to queue.");
                }
            }
        }
    }

    public synchronized void closeConnection() {
        isConnection = false;
        if(user != null) Databank.dellClient(user.getUniqID());

        SaveLogs.saveLog(LogType.DISCONNECTION, "Closed connection: " + socket.getRemoteSocketAddress() + " . With user: " + user.getUniqID());

        try { socket.close(); }
        catch (IOException e) {}
    }
}
