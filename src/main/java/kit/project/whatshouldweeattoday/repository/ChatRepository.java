package kit.project.whatshouldweeattoday.repository;

import kit.project.whatshouldweeattoday.domain.entity.Chat;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ChatRepository extends CrudRepository<Chat, Long> {
    List<Chat> findAllByRoomId(Long roomId);
    Chat findOneByRoomId(Long roomId);
}
