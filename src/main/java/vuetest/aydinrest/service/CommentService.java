package vuetest.aydinrest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vuetest.aydinrest.domain.Comment;
import vuetest.aydinrest.domain.User;
import vuetest.aydinrest.domain.Views;
import vuetest.aydinrest.dto.EventType;
import vuetest.aydinrest.dto.ObjectType;
import vuetest.aydinrest.repo.CommentRepo;
import vuetest.aydinrest.util.WsSender;

import java.util.function.BiConsumer;

@Service
public class CommentService {
    private final CommentRepo commentRepo;
    private final BiConsumer<EventType, Comment> wsSender;

    @Autowired
    public CommentService(CommentRepo commentRepo, WsSender wsSender) {
        this.commentRepo = commentRepo;
        this.wsSender = wsSender.getSender(ObjectType.COMMENT, Views.FullComment.class);
    }
    public Comment create(Comment comment, User user){
        comment.setAuthor(user);
        Comment commentFromDB = commentRepo.save(comment);

        wsSender.accept(EventType.CREATE,commentFromDB);
        return commentFromDB;
    }
}
