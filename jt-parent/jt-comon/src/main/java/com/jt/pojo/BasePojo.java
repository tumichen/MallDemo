package com.jt.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class BasePojo implements Serializable {
    private static final long serialVersionUID = 5732336033430986767L;
    private Date created;
    private Date updated;
}
