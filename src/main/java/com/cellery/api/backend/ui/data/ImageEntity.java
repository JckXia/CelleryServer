package com.cellery.api.backend.ui.data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "images")
public class ImageEntity implements Serializable {
    private static final long serialVersionUID = -217932131231325124L;

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String image_id;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name =  "log_fk")
    private LogEntity logObject;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public LogEntity getLogObject() {
        return logObject;
    }

    public void setLogObject(LogEntity logObject) {
        this.logObject = logObject;
    }
}
