package vuetest.aydinrest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vuetest.aydinrest.domain.User;
import vuetest.aydinrest.domain.Views;
import vuetest.aydinrest.dto.MessagePageDto;

import vuetest.aydinrest.repo.UserDetailsRepo;
import vuetest.aydinrest.service.MessageService;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/")
public class MainController {

    private final UserDetailsRepo userDetailsRepo;
   private final MessageService messageService;


@Value("${spring.profiles.active:prod}")
private  String profile;
    private final ObjectWriter messageWriter;
    private final ObjectWriter profileWriter;

    @Autowired
    public MainController(UserDetailsRepo userDetailsRepo,MessageService messageService, ObjectMapper mapper) {

        this.userDetailsRepo = userDetailsRepo;
        this.messageService = messageService;

        this.messageWriter = mapper.setConfig(mapper.getDeserializationConfig()).writerWithView(Views.FullMessage.class);
        this.profileWriter = mapper.setConfig(mapper.getDeserializationConfig()).writerWithView(Views.FullProfile.class);
    }

    @GetMapping
    public String main(
            Model model,
            @AuthenticationPrincipal User user
    ) throws JsonProcessingException {
        HashMap<Object, Object> data = new HashMap<>();

        if (user != null) {
            User userfromdb = userDetailsRepo.findById(user.getId()).get();
            List<User> alluser = userDetailsRepo.findAll();

            model.addAttribute("profile",profileWriter.writeValueAsString(userfromdb));


            Sort sort = Sort.by(Sort.Direction.DESC, "id");
            PageRequest pageRequest = PageRequest.of(0, 3, sort);
            MessagePageDto messagePageDto = messageService.findForUser(pageRequest, user);

            String messages = messageWriter.writeValueAsString(messagePageDto.getMessages());
            String allusers = profileWriter.writeValueAsString(alluser);

            model.addAttribute("messages", messages);
            model.addAttribute("allusers",allusers);
            data.put("currentPage", messagePageDto.getCurrentPage());
            data.put("totalPages", messagePageDto.getTotalPages());
        }else {
            model.addAttribute("messages","[]");
            model.addAttribute("profile","null");
            model.addAttribute("allusers","[]");
        }

        model.addAttribute("frontendData", data);
        model.addAttribute("isDevMode", "dev".equals(profile));

        return "index";
    }
}
