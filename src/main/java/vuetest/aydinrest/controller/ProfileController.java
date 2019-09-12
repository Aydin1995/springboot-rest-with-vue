package vuetest.aydinrest.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vuetest.aydinrest.domain.User;
import vuetest.aydinrest.domain.UserSub;
import vuetest.aydinrest.domain.Views;
import vuetest.aydinrest.repo.UserSubRepo;
import vuetest.aydinrest.service.ProfileService;

import java.util.List;

@RestController
@RequestMapping("profile")
public class ProfileController {
    private final ProfileService profileService;
private final UserSubRepo userSubRepo;

    @Autowired
    public ProfileController(ProfileService profileService, UserSubRepo userSubRepo) {
        this.profileService = profileService;
        this.userSubRepo = userSubRepo;
    }

    @GetMapping("{id}")
    @JsonView(Views.FullProfile.class)
        public User get(@PathVariable("id") User user){
return user;
        }

        @PostMapping("change-subscription/{channelid}")
        @JsonView(Views.FullProfile.class)
    public User changeSubscription(@AuthenticationPrincipal User subscriber,
                                   @PathVariable("channelid") User channel){
    if(subscriber.equals(channel)){
     return channel;
          }else {
        return profileService.changeSubscription(channel, subscriber);
       }
        }

    @GetMapping("get-subscribers/{channelId}")
    @JsonView(Views.IdName.class)
    public List<UserSub> subscribers(
            @PathVariable("channelId") User channel
    ) {
        return profileService.getSubscribers(channel);
    }

    @PostMapping("change-status/{subscriberId}")
    @JsonView(Views.IdName.class)
    public UserSub changeSubscriptionStatus(
            @AuthenticationPrincipal User channel,
            @PathVariable("subscriberId") User subscriber
    ) {
        return profileService.changeSubscriptionStatus(channel, subscriber);
    }
}
