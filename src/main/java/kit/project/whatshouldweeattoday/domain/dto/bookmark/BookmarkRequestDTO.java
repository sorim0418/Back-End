package kit.project.whatshouldweeattoday.domain.dto.bookmark;

import kit.project.whatshouldweeattoday.domain.entity.Bookmark;
import kit.project.whatshouldweeattoday.domain.entity.Member;
import kit.project.whatshouldweeattoday.domain.entity.Restaurant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class BookmarkRequestDTO {
    private Restaurant restaurant;

    public Bookmark toSaveEntity(Member member, Restaurant restaurant) {
        return Bookmark.builder()
                .member(member)
                .restaurant(restaurant)
                .build();
    }
}
