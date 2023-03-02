package generaLinfo;

import java.io.Serializable;

public enum TypeOfPartConnection implements Serializable {
    AUTHENTICATION, // Boolean
    REGISTRATION, // Class: User

    MESSAGE,  // Class: Message
    ALL_MESSAGES, // Class: Message[]

    USER_INFO, // Class: User

    SERVER_ANSWER, // Integer: code info

    GET_ONLINE_INFO; // Boolean


    private static final long serialVersionUID = -6814364098954353765L;
}
