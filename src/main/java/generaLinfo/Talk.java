package generaLinfo;

import java.io.Serial;
import java.io.Serializable;

public record Talk(TypeOfPartConnection type, Object body, Object additionalInfo) implements Serializable {
    @Serial
    private static final long serialVersionUID = -949409653167363898L;
}
