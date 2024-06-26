package kit.project.whatshouldweeattoday.domain.dto.restaurant;


import kit.project.whatshouldweeattoday.domain.dto.review.ReviewResponseDTO;
import kit.project.whatshouldweeattoday.domain.entity.FoodType;
import kit.project.whatshouldweeattoday.domain.entity.Restaurant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@NoArgsConstructor
public class RestaurantResponseDTO {
    private Long id;
    private String name;
    private String restaurantType;
    private Double degree;
    private String addressRoad;
    private String addressNumber;
    private String tel;
    private String menus;

    private int totalReviews;
    private int totalTaste;
    private int totalCost;
    private int totalKind;
    private int totalMood;
    private int totalPark;

    private Double latitude;
    private Double longitude;
    private Double distance;
    private Integer pathTime;
    private Page<ReviewResponseDTO> reviewList;

    private int count;
    private int rank;
    private FoodType foodType;

    // 음식점 상세보기
    public RestaurantResponseDTO(Long id, String name, String restaurantType, Double degree, String addressRoad, String addressNumber, String tel, String menus, int totalReviews, int totalTaste, int totalCost, int totalKind, int totalMood, int totalPark, Page<ReviewResponseDTO> reviewList,FoodType foodType) {
        this.id = id;
        this.name = name;
        this.restaurantType = restaurantType;
        this.degree = degree;
        this.addressRoad = addressRoad;
        this.addressNumber = addressNumber;
        this.tel = tel;
        this.menus = menus;
        this.totalReviews = totalReviews;
        this.totalTaste = totalTaste;
        this.totalCost = totalCost;
        this.totalKind = totalKind;
        this.totalMood = totalMood;
        this.totalPark = totalPark;
        this.reviewList = reviewList;
        this.foodType = foodType;
    }

    // 리뷰폼 안의 음식점 상세
    public RestaurantResponseDTO(Long id, String name, String restaurantType, Double degree, String addressRoad, String addressNumber, String tel, String menus, int totalReviews, int totalTaste, int totalCost, int totalKind, int totalMood, int totalPark) {
        this.id = id;
        this.name = name;
        this.restaurantType = restaurantType;
        this.degree = degree;
        this.addressRoad = addressRoad;
        this.addressNumber = addressNumber;
        this.tel = tel;
        this.menus = menus;
        this.totalReviews = totalReviews;
        this.totalTaste = totalTaste;
        this.totalCost = totalCost;
        this.totalKind = totalKind;
        this.totalMood = totalMood;
        this.totalPark = totalPark;
    }

    // Entity -> DTO
    public RestaurantResponseDTO(Restaurant restaurant) {
        this.id = restaurant.getId();
        this.name = restaurant.getName();
        this.restaurantType = restaurant.getRestaurantType();
        this.degree = restaurant.getDegree();
        this.addressRoad = restaurant.getAddressRoad();
        this.addressNumber = restaurant.getAddressNumber();
        this.tel = restaurant.getTel();
        this.menus = restaurant.getMenus();
        this.totalReviews = restaurant.getTotalReviews();
        this.totalTaste = restaurant.getTotalTaste();
        this.totalCost = restaurant.getTotalCost();
        this.totalKind = restaurant.getTotalKind();
        this.totalMood = restaurant.getTotalMood();
        this.totalPark = restaurant.getTotalPark();
        this.longitude = restaurant.getLongitude();
        this.latitude = restaurant.getLatitude();
        this.distance = restaurant.getDistance();
        this.pathTime = restaurant.getPathTime();
        this.count= Math.toIntExact(restaurant.getCount());
    }

    // 주간 순위 -> 음식 종류만 반환
    public RestaurantResponseDTO(Long id, String name, int count, String restaurantType, int rank) {
        this.id = id;
        this.name = name;
        this.count = count;
        this.restaurantType = restaurantType;
        this.rank = rank;
    }

    //음식점과 리뷰리스트 같이 반환
    public RestaurantResponseDTO(Restaurant restaurant, Page<ReviewResponseDTO> reviewList) {
        this.id = restaurant.getId();
        this.name = restaurant.getName();
        this.restaurantType = restaurant.getRestaurantType();
        this.degree = restaurant.getDegree();
        this.addressRoad = restaurant.getAddressRoad();
        this.addressNumber = restaurant.getAddressNumber();
        this.tel = restaurant.getTel();
        this.menus = restaurant.getMenus();
        this.totalReviews = restaurant.getTotalReviews();
        this.totalTaste = restaurant.getTotalTaste();
        this.totalCost = restaurant.getTotalCost();
        this.totalKind = restaurant.getTotalKind();
        this.totalMood = restaurant.getTotalMood();
        this.totalPark = restaurant.getTotalPark();
        this.latitude = restaurant.getLatitude();
        this.longitude = restaurant.getLongitude();
        this.distance = restaurant.getDistance();
        this.pathTime = restaurant.getPathTime();
        this.reviewList = reviewList;
    }
}