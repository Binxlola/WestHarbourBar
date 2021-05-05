package main.java.com.app.entities;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "App")
public class AppData {

    public AppData() {}

    @Id
    private long Id;
    public long getId() {return Id;}
    public void setId(long id) {this.Id = id;}

    @Column(name = "version", nullable = false, columnDefinition = "varchar(8) default '1.0.0'")
    private String currentVersion;
    public void setCurrentVersion(String version) {this.currentVersion = version;}
    public String getCurrentVersion() {return currentVersion;}

    @Column(name = "lastUpdated", nullable = false)
    @CreationTimestamp
    private Date lastUpdated;
    public void setLastUpdated(Date lastUpdated) {this.lastUpdated = lastUpdated;}
    public Date getLastUpdated() {return lastUpdated;}


}
