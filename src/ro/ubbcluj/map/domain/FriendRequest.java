package ro.ubbcluj.map.domain;

import java.util.Objects;

public class FriendRequest extends Entity<Long>{
    private Long id_from;
    private Long id_to;
    private String status;

    public FriendRequest(Long aLong, Long id_from, Long id_to, String status) {
        super(aLong);
        this.id_from = id_from;
        this.id_to = id_to;
        this.status = status;
    }

    public Long getId_from() {
        return id_from;
    }

    public void setId_from(Long id_from) {
        this.id_from = id_from;
    }

    public Long getId_to() {
        return id_to;
    }

    public void setId_to(Long id_to) {
        this.id_to = id_to;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendRequest that = (FriendRequest) o;
        return id_from.equals(that.id_from) && id_to.equals(that.id_to) && status.equals(that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_from, id_to, status);
    }

    @Override
    public String toString() {
        return "FriendRequest{" +
                "id_from=" + id_from +
                ", id_to=" + id_to +
                ", status='" + status + '\'' +
                '}';
    }
}
