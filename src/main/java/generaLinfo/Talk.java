package generaLinfo;

import java.io.Serial;
import java.io.Serializable;

public class Talk implements Serializable {
    public final TypeOfPartConnection type;
    public final Object body;
    public final Object additionalInfo;

    public Talk(TypeOfPartConnection type, Object body, Object additionalInfo) {
        this.type = type;
        this.body = body;
        this.additionalInfo = additionalInfo;
    }
    @Serial
    private static final long serialVersionUID = 37087783987921275L;
}
