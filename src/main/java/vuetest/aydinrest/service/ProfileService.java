package vuetest.aydinrest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vuetest.aydinrest.domain.User;
import vuetest.aydinrest.domain.UserSub;
import vuetest.aydinrest.repo.UserDetailsRepo;
import vuetest.aydinrest.repo.UserSubRepo;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfileService {
    private final UserDetailsRepo userDetailsRepo;
    private final UserSubRepo userSubRepo;

    @Autowired
    public ProfileService(UserDetailsRepo userDetailsRepo, UserSubRepo userSubRepo) {
        this.userDetailsRepo = userDetailsRepo;
        this.userSubRepo = userSubRepo;
    }

    public User changeSubscription(User channel, User subscriber) {
       List<UserSub>subscriptions=channel.getSubscribers()
               .stream().filter(subscription->subscription.getSubscriber().equals(subscriber)).
               collect(Collectors.toList());
        if (subscriptions.isEmpty()){
            UserSub userSubscription = new UserSub(channel, subscriber);
            channel.getSubscribers().add(userSubscription);

        }else {
    channel.getSubscribers().removeAll(subscriptions);
}
        return  userDetailsRepo.save(channel);
    }

    public List<UserSub> getSubscribers(User channel) {
        return userSubRepo.findByChannel(channel);
    }

    public UserSub changeSubscriptionStatus(User channel, User subscriber) {
        UserSub subscription = userSubRepo.findByChannelAndSubscriber(channel, subscriber);
        subscription.setActive(!subscription.isActive());

        return userSubRepo.save(subscription);
    }
}
