package kit.project.whatshouldweeattoday.domain.dto.review;

import kit.project.whatshouldweeattoday.domain.entity.Review;
import kit.project.whatshouldweeattoday.domain.type.ReviewType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReviewRequestDTO {
    private long restaurant_id;
    private Long totalLikes;
    private int taste;
    private int mood;
    private int park;
    private int kind;
    private int cost;
    private ReviewType reviewType;
    private double stars;

    //리뷰 등록
    @Builder
    public ReviewRequestDTO(int taste, int mood, int park, int kind, int cost, ReviewType reviewType, double stars, Long totalLikes) {
        this.taste = taste;
        this.mood = mood;
        this.park = park;
        this.kind = kind;
        this.cost = cost;
        this.reviewType = reviewType;
        this.stars = stars;
        this.totalLikes = totalLikes;
    }

    //DTO to Entity
    public Review toEntity(){
            return Review.builder()
                    .taste(taste)
                    .cost(cost)
                    .mood(mood)
                    .kind(kind)
                    .park(park)
                    .reviewType(reviewType)
                    .stars(stars)
                    .totalLikes(totalLikes)
                    .build();
    }
}
