package ro.ubbcluj.map.service;

import ro.ubbcluj.map.domain.Message;
import ro.ubbcluj.map.repository.Repository;

import java.util.Objects;

public class MessageService extends AbstractService<Long, Message>{
    public MessageService(Repository<Long, Message> repo){ repository = repo; }
}
