package vuetest.aydinrest.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import vuetest.aydinrest.domain.Comment;
import vuetest.aydinrest.domain.Message;

import javax.transaction.Transactional;
import java.util.List;

public interface CommentRepo extends JpaRepository<Comment,Long> {

    List<Comment> findCommentsByMessage(Message message);

    @Transactional
    void deleteByMessage(Message message);
}
