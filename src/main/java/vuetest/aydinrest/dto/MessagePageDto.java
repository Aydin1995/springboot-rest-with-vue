package vuetest.aydinrest.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vuetest.aydinrest.domain.Message;
import vuetest.aydinrest.domain.Views;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@JsonView(Views.FullMessage.class)
public class MessagePageDto {
    private List<Message> messages;
    private int currentPage;
    private int totalPages;


}
