package crawler.playStore.crawler.entity.compositekey;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
public class VersionId implements Serializable {
    private String appName;
    private String version;

    public VersionId(String appName, String version) {
        this.appName = appName;
        this.version = version;
    }

    public VersionId() {
    }
}
