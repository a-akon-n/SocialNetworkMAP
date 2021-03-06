package ro.ubbcluj.map.domain;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class Friendship extends Entity<Long>{
    private Long user1;
    private Long user2;
    private Date date;

    public Date getDate() {
        return date;
    }

    public Friendship(Long aLong, Long user1, Long user2, Date date) {
        super(aLong);
        this.user1 = user1;
        this.user2 = user2;
        this.date = date;
    }

    public Long getUser1() {
        return user1;
    }

    public void setUser1(Long user1) {
        this.user1 = user1;
    }

    public Long getUser2() {
        return user2;
    }

    public void setUser2(Long user2) {
        this.user2 = user2;
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "id= '"+ getId() + '\'' +
                ", user1=" + user1 +
                ", user2=" + user2 +
                "} ";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Friendship that)) return false;
        return (Objects.equals(getUser1(), that.getUser1()) && Objects.equals(getUser2(), that.getUser2())) ||
                (Objects.equals(getUser1(), that.getUser2()) && Objects.equals(getUser2(), that.getUser1()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUser1(), getUser2());
    }
}
