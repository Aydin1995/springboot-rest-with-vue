package vuetest.aydinrest.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vuetest.aydinrest.domain.Message;
import vuetest.aydinrest.domain.User;
import vuetest.aydinrest.domain.Views;
import vuetest.aydinrest.dto.EventType;
import vuetest.aydinrest.dto.MessagePageDto;
import vuetest.aydinrest.dto.MetaDto;
import vuetest.aydinrest.dto.ObjectType;
import vuetest.aydinrest.repo.CommentRepo;
import vuetest.aydinrest.repo.MessageRepo;
import vuetest.aydinrest.repo.UserSubRepo;
import vuetest.aydinrest.service.MessageService;
import vuetest.aydinrest.util.WsSender;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("message")
public class MessageController {

    private static String URL_PATTERN = "https?:\\/\\/?[\\w\\d\\._\\-%\\/\\?=&#]+";
    private static String IMAGE_PATTERN = "\\.(jpeg|jpg|gif|png)$";
    private static Pattern URL_REGEX=Pattern.compile(URL_PATTERN,Pattern.CASE_INSENSITIVE);
    private static Pattern IMG_REGEX=Pattern.compile(IMAGE_PATTERN,Pattern.CASE_INSENSITIVE);

   private final MessageRepo messageRepo;
   private final CommentRepo commentRepo;
   private final MessageService messageService;
   private final BiConsumer<EventType,Message> wsSender;
   private final UserSubRepo userSubRepo;
@Autowired
    public MessageController(MessageRepo messageRepo, CommentRepo commentRepo, MessageService messageService, WsSender wsSender, UserSubRepo userSubRepo) {
        this.messageRepo = messageRepo;
    this.commentRepo = commentRepo;
    this.messageService = messageService;
    this.wsSender = wsSender.getSender(ObjectType.MESSAGE,Views.FullMessage.class);
    this.userSubRepo = userSubRepo;
}

    @GetMapping
    @JsonView(Views.FullMessage.class)
    public MessagePageDto list(
            @AuthenticationPrincipal User user,
            @PageableDefault(size = 3, sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable
    ) {

        return messageService.findForUser(pageable, user);
    }

    @GetMapping("{id}")
    @JsonView(Views.FullMessage.class)
    public Message getOne(@PathVariable("id") Message message){

        return message;
    }




    @PostMapping
    @JsonView(Views.FullMessage.class)
    public Message create(@RequestBody Message message, @AuthenticationPrincipal User user) throws IOException {

message.setCreationDate(LocalDateTime.now());
fillMeta(message);
message.setAuthor(user);
        Message updatedMessage = messageRepo.save(message);
        wsSender.accept(EventType.CREATE,updatedMessage);
        System.out.println("BEZDIM");

        return updatedMessage;
    }

    @PutMapping("{id}")
    @JsonView(Views.FullMessage.class)
    public Message update(@PathVariable("id") Message messageFromDb, @RequestBody Message message) throws IOException {

    messageFromDb.setText(message.getText());

        fillMeta(messageFromDb);
        Message updatedMessage = messageRepo.save(messageFromDb);

        wsSender.accept(EventType.UPDATE,updatedMessage);
        return updatedMessage;

//
//
//        BeanUtils.copyProperties(message,messageFromDb,"id");
//fillMeta(messageFromDb);
//
//
//        Message updatedMessage = messageRepo.save(messageFromDb);
//        wsSender.accept(EventType.UPDATE,updatedMessage);
//        System.out.println("updated server");
//

//        return updatedMessage;

}

    @DeleteMapping("{id}")
    public void delete(@PathVariable(value = "id") Message message) {

if(message!=null) {
    messageRepo.delete(message);
}

wsSender.accept(EventType.REMOVE,message);

    }

//    @MessageMapping("/changeMessage")
//    @SendTo("/topic/activity")
//    public Message change(Message message){
//
//        System.out.println("websocket");
//    return messageRepo.save(message);
//    };


    private void fillMeta(Message message) throws IOException {
        String text = message.getText();
        Matcher matcher = URL_REGEX.matcher(text);

        if(matcher.find()){
            String url = text.substring(matcher.start(), matcher.end());
            if(url.contains("youtube")) {
                String subs = (url.substring(url.indexOf("=") + 1));
                url = "https://youtu.be/" + subs;
                System.out.println(url);
            }
            matcher = IMG_REGEX.matcher(url);

            message.setLink(url);
             if(matcher.find()){

              message.setLinkCover(url);
             }
             else if(!url.contains("youtu")||!url.contains("youtube")){
                 System.out.println("rabotaet");
                 MetaDto meta = getMeta(url);

                 message.setLinkCover(meta.getCover());
                 message.setLinkTitle(meta.getTitle());
                 message.setLinkDescription(meta.getDescription());
             }
        }
    }

    private MetaDto getMeta(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();

        Elements title = doc.select("meta[name$=title],meta[property$=title]");
        Elements description = doc.select("meta[name$=description],meta[property$=description]");
        Elements cover = doc.select("meta[name$=image],meta[property$=image]");

        return new MetaDto(
                getContent(title.first()),
                getContent(description.first()),
                getContent(cover.first())
        );
    }

    private String getContent(Element element) {
        return element==null?"":element.attr("content");
    }
}

