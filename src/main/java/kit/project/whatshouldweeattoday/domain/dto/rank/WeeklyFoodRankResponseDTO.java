package kit.project.whatshouldweeattoday.domain.dto.rank;

import kit.project.whatshouldweeattoday.domain.dto.food.FoodResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class WeeklyFoodRankResponseDTO {
    private Long id;
    private int ranks;
    private String foodTypeName;
    private String date;
    private List<FoodResponseDTO> topFoods;

    public WeeklyFoodRankResponseDTO(String date, List<FoodResponseDTO> topFoods) {
        this.date = date;
        this.topFoods = topFoods;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public void setTopFood(List<FoodResponseDTO> topFoods) {
        this.topFoods = topFoods;
    }
}
