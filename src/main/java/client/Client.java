package client;

import generaLinfo.Message;
import generaLinfo.Talk;
import generaLinfo.TypeOfPartConnection;
import generaLinfo.User;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class Client {
    private final long uniqueID;
    private final ObjectOutputStream output;

    public Client(long uniqueID, ObjectOutputStream output) {
        this.uniqueID = uniqueID;
        this.output = output;
    }

    public long getUniqueID() { return uniqueID; }

    public synchronized void sendMessage(Message message, User user) throws IOException {
        output.writeObject(new Talk(TypeOfPartConnection.MESSAGE, message, user));
    }
}
