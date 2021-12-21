package com.example.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class Message extends Entity<Long> {
    private final User from;
    private List<User> to;
    private String message;
    private LocalDateTime date;
    private Message replyTo;

    public Message(Long id, User from, List<User> to, String message, LocalDateTime date, Message replyTo){
        super(id);
        this.from = from;
        this.to = to;
        this.message = message;
        this.date = date;
        this.replyTo = replyTo;
    }

    public User getFrom() {
        return from;
    }

    public List<User> getTo() {
        return to;
    }

    public void setTo(List<User> to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Message getReplyTo() {
        return replyTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return Objects.equals(from, message1.from) && Objects.equals(to, message1.to) && Objects.equals(message, message1.message) && Objects.equals(date, message1.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, message, date);
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + getId() +
                ", from=" + from +
                ", to=" + to +
                ", message='" + message +
                ", date=" + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) +
                (replyTo != null ?
                        ", replyTo=[id=" + replyTo.getId() + " ,message=" + replyTo.getMessage() + "]" : "") +
                '}';
    }

    public String toStringSimplified(){
        return getMessage() + " [sent by: " +
                getFrom().getFirstName() + " " + getFrom().getLastName() +
                ", on: " + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) +
                (replyTo != null ? ", replyTo:" + replyTo.toStringSimplified() + "]" : "]");
    }
}

