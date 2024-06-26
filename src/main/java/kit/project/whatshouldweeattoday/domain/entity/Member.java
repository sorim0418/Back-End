package kit.project.whatshouldweeattoday.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import kit.project.whatshouldweeattoday.domain.type.RoleType;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false)
    private String loginPw;
    @Column(nullable = false)
    private String verifiedLoginPw;

    @Column(nullable = false, unique = true)
    private String nickname;
    private String gender;
    private int age;

    @Enumerated(STRING)
    @Column(nullable = false, length = 30)
    private RoleType role; //권한 -> USER, ADMIN

    /* 회원 탈퇴 => 친구, 북마크, 리뷰 모두 삭제 */
    @OneToMany( mappedBy = "member")
    private List<Friendship> friendshipList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChatRoomMember> chatRoomMembers = new HashSet<>();

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Bookmark> bookmarkList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviewList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Likes> likesList = new ArrayList<>();

    public Member(String loginId, String loginPw, String nickname, String gender, int age) {
        this.loginId = loginId;
        this.loginPw = loginPw;
        this.nickname = nickname;
        this.gender = gender;
        this.age = age;
    }

    /* 리뷰 등록 */
    public void addReview(Review review){
        reviewList.add(review);
    }

    /* 회원 정보 수정 */
    public void updateNickname(String nickname){
        this.nickname = nickname;
    }
    public void updateGender(String gender){
        this.gender = gender;
    }
    public void updateAge(int age){
        this.age = age;
    }

    /* 패스워드 암호화 관련 */
    public void encodePassword(PasswordEncoder passwordEncoder){
        this.loginPw = passwordEncoder.encode(loginPw);
    }
    public boolean matchPassword(PasswordEncoder passwordEncoder, String checkPW) {
        return passwordEncoder.matches(checkPW, getLoginPw());
    }

    //== 권한 부여 ==//
    public void addUserAuthority() {
        this.role = RoleType.USER;
    }
}