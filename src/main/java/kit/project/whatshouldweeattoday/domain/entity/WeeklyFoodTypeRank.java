package kit.project.whatshouldweeattoday.domain.entity;

import jakarta.persistence.*;
import kit.project.whatshouldweeattoday.domain.dto.foodType.FoodTypeResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class WeeklyFoodTypeRank extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FOOD_TYPE_RANK_ID")
    private Long id;
    private String date; // 한주가 지나면 초기화되어야함

    //Transient : 실제 데이터베이스에 저장되지 않고 티티 객체의 라이프사이클 동안 메모리에서만 유지
    @Transient
    private List<FoodTypeResponseDTO> foodTypes = new ArrayList<>();

    public WeeklyFoodTypeRank(int rank, String date) {
        this.date = date;
    }
}
