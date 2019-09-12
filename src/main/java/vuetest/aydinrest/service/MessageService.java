package vuetest.aydinrest.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vuetest.aydinrest.domain.Message;
import vuetest.aydinrest.domain.User;
import vuetest.aydinrest.domain.UserSub;
import vuetest.aydinrest.dto.MessagePageDto;
import vuetest.aydinrest.repo.MessageRepo;
import vuetest.aydinrest.repo.UserDetailsRepo;
import vuetest.aydinrest.repo.UserSubRepo;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

private final UserSubRepo userSubRepo;
private final MessageRepo messageRepo;
    private final UserDetailsRepo userDetailsRepo;

    public MessageService(UserSubRepo userSubRepo, MessageRepo messageRepo, UserDetailsRepo userDetailsRepo) {
        this.userSubRepo = userSubRepo;
        this.messageRepo = messageRepo;
        this.userDetailsRepo = userDetailsRepo;
    }


    public MessagePageDto findForUser(Pageable pageable, User user) {
        List<User> channels = userSubRepo.findBySubscriber(user)
                .stream().filter(UserSub::isActive)
                .map(UserSub::getChannel)
                .collect(Collectors.toList());

        channels.add(user);
      User adminuser=  userDetailsRepo.findById("105614523492445019979").get();
        if(!user.getId().equals(adminuser.getId())) {
            channels.add(userDetailsRepo.findById("105614523492445019979").get());
        }
        Page<Message> page = messageRepo.findByAuthorIn(channels, pageable);


        return new MessagePageDto(
                page.getContent(),
                pageable.getPageNumber(),
                page.getTotalPages()
        );
    }
}
