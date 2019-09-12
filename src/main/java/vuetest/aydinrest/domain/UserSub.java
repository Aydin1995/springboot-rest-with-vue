package vuetest.aydinrest.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Entity
@Data
@EqualsAndHashCode(of="id")
@NoArgsConstructor
@ToString(of="id")
public class UserSub {
    @EmbeddedId
    @JsonIgnore
    private UserSubId id;

    @MapsId("channelId")
    @ManyToOne
    @JsonView(Views.IdName.class)
    @JsonIdentityReference
    @JsonIdentityInfo(property = "id",
            generator = ObjectIdGenerators.PropertyGenerator.class)
private User channel;

    @MapsId("subscriberId")
    @ManyToOne
    @JsonView(Views.IdName.class)
    @JsonIdentityReference
    @JsonIdentityInfo(property = "id",
            generator = ObjectIdGenerators.PropertyGenerator.class)
private User subscriber;


    @JsonView(Views.Id.class)
    private boolean active;

    public UserSub(User channel, User subscriber){
        this.channel=channel;
        this.subscriber=subscriber;
        this.id=new UserSubId(channel.getId(),subscriber.getId());
    }

}
