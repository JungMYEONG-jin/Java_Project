package crawler.playStore.crawler.entity;

import crawler.playStore.crawler.entity.compositekey.VersionId;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
public class VersionInfo implements Serializable {


    @EmbeddedId
    private VersionId versionId;

    @Column(name = "UpdatedDate")
    private String updatedDate;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public void setCreatedDate(){
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        modifiedDate = now;
    }

    public VersionInfo() {
    }

    public VersionInfo(VersionId versionId, String updatedDate) {
        this.versionId = versionId;
        this.updatedDate = updatedDate;
    }
}
