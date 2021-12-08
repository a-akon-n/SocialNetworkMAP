package ro.ubbcluj.map.service;

import ro.ubbcluj.map.domain.Message;
import ro.ubbcluj.map.repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MessageService extends AbstractService<Long, Message>{
    public MessageService(Repository<Long, Message> repo){ repository = repo; }

    public List<Message> getConversation(Long id1, Long id2){
        ArrayList<Message> all = (ArrayList<Message>) findAll();
        ArrayList<Message> conversation = new ArrayList<>();

        //Find all messages where user1 and user2 are the only participants
        all.forEach(message -> { if (
                (Objects.equals(message.getFrom().getId(), id1) &&
                message.getTo().size() == 1 &&
                Objects.equals(message.getTo().get(0).getId(), id2))
                ||
                (Objects.equals(message.getFrom().getId(), id2) &&
                message.getTo().size() == 1 &&
                Objects.equals(message.getTo().get(0).getId(), id1))
        ) {
            conversation.add(message);
        }
        });

        //Sort messages by date


        return conversation.stream()
                .sorted((m1, m2) -> (m1.getDate().isAfter(m2.getDate())) ? 1 : 0)
                .collect(Collectors.toList());
    }
}
