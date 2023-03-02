package generaLinfo;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.TimeZone;

public class Message implements Serializable, Comparable<Message> {
    private final long fromUserID;
    private final long toUserID;
    private final String message;
    private final long timeUNIX;
    private final LocalDateTime dateTime;

    private static final long serialVersionUID = -7117113348408857601L;

    public Message(long fromUserID, long toUserID, String message) {
        this.fromUserID = fromUserID;
        this.toUserID = toUserID;
        this.message = message;
        this.timeUNIX = System.currentTimeMillis();
        dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(this.timeUNIX), TimeZone.getDefault().toZoneId());
    }
    public Message(long fromUserID, long toUserID, String message, long timeUNIX) {
        this.fromUserID = fromUserID;
        this.toUserID = toUserID;
        this.message = message;
        this.timeUNIX = timeUNIX;
        dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(this.timeUNIX), TimeZone.getDefault().toZoneId());
    }

    public long getFromUserID() {
        return fromUserID;
    }

    public long getToUserID() {
        return toUserID;
    }

    public String getMessage() {
        return message;
    }

    public long getTimeUNIX() {
        return timeUNIX;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public int compareTo(Message o) {
        return Long.compare(this.timeUNIX, o.timeUNIX);
    }

    @Override
    public String toString() {
        return "From: " + fromUserID + " to: " + toUserID + ".\nMessage: " + message + "\nTime: " + timeUNIX;
    }

    public static class SortByTime implements Comparator<Message> {
        @Override
        public int compare(Message o1, Message o2) {
            return o1.compareTo(o2);
        }
    }


}
