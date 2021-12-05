package ro.ubbcluj.map.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class ReplyMessage extends Message{
    private Message replyTo;

    public ReplyMessage(Long id, User from, List<User> to,
           String message, LocalDateTime date, Message replyTo) {

        super(id, from, to, message, date);
        this.replyTo = replyTo;
    }

    public Message getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(Message replyTo) {
        this.replyTo = replyTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ReplyMessage that = (ReplyMessage) o;
        return replyTo.equals(that.replyTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), replyTo);
    }
}
