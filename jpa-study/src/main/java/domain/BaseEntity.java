package domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

//공통되는 칼럼을 추가할때 편리함
@MappedSuperclass
public abstract class BaseEntity {
    @Column(name="등록일")
    private LocalDateTime createdDate;
    @Column(name="수정일")
    private LocalDateTime lastModifiedDate;


    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
