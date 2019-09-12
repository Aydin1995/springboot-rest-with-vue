package vuetest.aydinrest.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import vuetest.aydinrest.domain.User;
import vuetest.aydinrest.domain.UserSub;
import vuetest.aydinrest.domain.UserSubId;

import java.util.List;

public interface UserSubRepo extends JpaRepository<UserSub, UserSubId> {
    List<UserSub>findBySubscriber(User user);

    List<UserSub> findByChannel(User channel);

   UserSub findByChannelAndSubscriber(User channel, User subscriber);
}
