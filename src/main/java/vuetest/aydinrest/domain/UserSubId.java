package vuetest.aydinrest.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSubId implements Serializable {
    @JsonView(Views.Id.class)
    private String channelId;
    @JsonView(Views.Id.class)
    private String subscriberId;
}
