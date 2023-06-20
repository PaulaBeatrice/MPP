package triathlon.model;

import java.io.Serializable;

public interface Entity<ID> extends Serializable {

    public ID getId();

    public void setId(ID id);

}
