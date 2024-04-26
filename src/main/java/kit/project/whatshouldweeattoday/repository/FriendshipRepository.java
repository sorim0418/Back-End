package kit.project.whatshouldweeattoday.repository;

import kit.project.whatshouldweeattoday.domain.dto.friend.FriendListResponseDTO;
import kit.project.whatshouldweeattoday.domain.entity.Friendship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    Page<FriendListResponseDTO> findByFriendLoginIdContaining(String keyword, Pageable pageable);

}