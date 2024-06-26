package kit.project.whatshouldweeattoday.controller;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import kit.project.whatshouldweeattoday.domain.dto.MsgResponseDTO;
import kit.project.whatshouldweeattoday.domain.dto.member.JwtTokenDTO;
import kit.project.whatshouldweeattoday.domain.dto.member.MemberResponseDTO;
import kit.project.whatshouldweeattoday.domain.dto.member.login.LoginRequestDTO;
import kit.project.whatshouldweeattoday.domain.dto.member.signup.SignupRequestDTO;
import kit.project.whatshouldweeattoday.domain.dto.member.update.MemberUpdateRequestDTO;
import kit.project.whatshouldweeattoday.domain.dto.member.update.MemberUpdateResponseDTO;
import kit.project.whatshouldweeattoday.domain.dto.review.ReviewRequestDTO;
import kit.project.whatshouldweeattoday.domain.entity.*;
import kit.project.whatshouldweeattoday.domain.type.FriendshipStatus;
import kit.project.whatshouldweeattoday.domain.type.ReviewType;
import kit.project.whatshouldweeattoday.repository.*;
import kit.project.whatshouldweeattoday.security.util.SecurityUtil;
import kit.project.whatshouldweeattoday.service.MemberService;
import kit.project.whatshouldweeattoday.service.MemberStatusService;
import kit.project.whatshouldweeattoday.service.RestaurantService;
import kit.project.whatshouldweeattoday.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final MemberStatusService memberStatusService;
    private final MemberRepository memberRepository;
    private final FriendshipRepository friendshipRepository;
    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;
    private final FoodTypeRepository foodTypeRepository;
    private final ReviewService reviewService;

    /* 회원가입 */
    @PostMapping("/api/signup")
    public ResponseEntity<MsgResponseDTO> signup(@Valid @RequestBody SignupRequestDTO requestDTO) throws BadRequestException {
        memberService.createMember(requestDTO);
        return ResponseEntity.ok(new MsgResponseDTO("회원가입 완료", HttpStatus.OK.value()));
    }

    @GetMapping("/confirmLoginId/{loginId}")
    public ResponseEntity<String> confirmId(@PathVariable("loginId") String loginId) throws BadRequestException {
        if(memberService.confirmId(loginId)) {
            throw new BadRequestException("이미 사용중인 아이디입니다.");
        } else {
            return ResponseEntity.ok("사용 가능한 아이디 입니다.");
        }
    }

    @GetMapping("/confirmNickname/{nickname}")
    public ResponseEntity<String> confirmNickname(@PathVariable("nickname") String nickname) throws BadRequestException {
        if(memberService.confirmNickname(nickname)) {
            throw new BadRequestException("이미 사용중인 닉네임입니다.");
        } else {
            return ResponseEntity.ok("사용 가능한 닉네임 입니다.");
        }
    }

    /* 회원 정보 수정 */
    @GetMapping("/mypage/update")
    public ResponseEntity<MemberUpdateResponseDTO>  update() throws BadRequestException {
        Member member = memberService.findByLoginId(SecurityUtil.getLoginId()).orElseThrow(() -> new BadRequestException("존재하지 않는 사용자입니다."));
        MemberUpdateResponseDTO responseDTO = MemberUpdateResponseDTO.builder()
                .nickname(member.getNickname())
                .age(member.getAge())
                .gender(member.getGender())
                .build();

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PatchMapping("/api/user/update")
    public ResponseEntity<MsgResponseDTO> updateUser(@Valid @RequestBody MemberUpdateRequestDTO memberUpdateRequestDTO) throws BadRequestException {
        memberService.updateMember(memberUpdateRequestDTO, SecurityUtil.getLoginId());
        return ResponseEntity.ok(new MsgResponseDTO("회원 정보 수정 완료", HttpStatus.OK.value()));
    }

    /* 회원 탈퇴 */
    @DeleteMapping("/api/user/delete")
    public ResponseEntity<MsgResponseDTO> deleteUser(@Valid @RequestBody UserDeleteDTO userDeleteDto) throws Exception {
        memberService.deleteMember(userDeleteDto.checkPassword(), SecurityUtil.getLoginId());
        return ResponseEntity.ok(new MsgResponseDTO("회원 탈퇴 완료", HttpStatus.OK.value()));
    }

    public record UserDeleteDTO(@NotBlank(message = "비밀번호를 입력해주세요")
                                String checkPassword) {
    }

    @PostMapping("/api/signin")
    public JwtTokenDTO signIn(@RequestBody LoginRequestDTO requestDTO) {
        String username = requestDTO.getLoginId();
        String password = requestDTO.getLoginPw();
        JwtTokenDTO jwtToken = memberService.signIn(username, password);

        log.info("request username = {}, password = {}", username, password);
        log.info("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());

        memberStatusService.broadcastUserStatus(username, "LOGIN");

        return jwtToken;
    }

    @GetMapping("/api/memberInfo")
    public ResponseEntity<MemberResponseDTO> getMemberInfo() throws BadRequestException {
        String loginId = SecurityUtil.getLoginId();
        Member findMember = memberService.findByLoginId(loginId).orElseThrow(() -> new BadRequestException("존재하지 않는 사용자입니다."));
        MemberResponseDTO memberResponseDTO = MemberResponseDTO.builder()
                .id(findMember.getId())
                .loginId(findMember.getLoginId())
                .loginPw(findMember.getLoginPw())
                .nickname(findMember.getNickname())
                .gender(findMember.getGender())
                .age(findMember.getAge())
                .build();

        return ResponseEntity.ok(memberResponseDTO);
    }

    @GetMapping("/api/memberId")
    public ResponseEntity<Long> getMemberId() throws BadRequestException {
        String loginId = SecurityUtil.getLoginId();
        Member findMember = memberService.findByLoginId(loginId).orElseThrow(() -> new BadRequestException("존재하지 않는 사용자입니다."));

        return ResponseEntity.ok(findMember.getId());
    }

//    @PostConstruct
    public void initMemberData() throws BadRequestException {
        SignupRequestDTO member1 = SignupRequestDTO.builder()
                .loginId("hyun3478")
                .loginPw("a12345678")
                .verifiedLoginPw("a12345678")
                .nickname("이지현")
                .gender("FEMALE")
                .age(24)
                .build();
        SignupRequestDTO member2 = SignupRequestDTO.builder()
                .loginId("lim3478")
                .loginPw("a12345678")
                .verifiedLoginPw("a12345678")
                .nickname("임수연")
                .gender("FEMALE")
                .age(24)
                .build();
        SignupRequestDTO member3 = SignupRequestDTO.builder()
                .loginId("solim12")
                .loginPw("a12345678")
                .verifiedLoginPw("a12345678")
                .nickname("이소림")
                .gender("FEMALE")
                .age(24)
                .build();
        SignupRequestDTO member4 = SignupRequestDTO.builder()
                .loginId("lee12")
                .loginPw("a12345678")
                .verifiedLoginPw("a12345678")
                .nickname("이준현")
                .gender("MALE")
                .age(24)
                .build();
        SignupRequestDTO member5 = SignupRequestDTO.builder()
                .loginId("member5")
                .loginPw("a12345678")
                .verifiedLoginPw("a12345678")
                .nickname("이민형")
                .gender("MALE")
                .age(24)
                .build();
        SignupRequestDTO member6 = SignupRequestDTO.builder()
                .loginId("member6")
                .loginPw("a12345678")
                .verifiedLoginPw("a12345678")
                .nickname("이동혁")
                .gender("MALE")
                .age(24)
                .build();
        SignupRequestDTO member7 = SignupRequestDTO.builder()
                .loginId("member7")
                .loginPw("a12345678")
                .verifiedLoginPw("a12345678")
                .nickname("변우석")
                .gender("MALE")
                .age(24)
                .build();
        memberService.createMember(member1);
        memberService.createMember(member2);
        memberService.createMember(member3);
        memberService.createMember(member4);
        memberService.createMember(member5);
        memberService.createMember(member6);
        memberService.createMember(member7);
        initFriendshipData();
    }

    public void initFriendshipData() throws BadRequestException {
        Member fromMember = memberRepository.findByLoginId("hyun3478").orElseThrow(() -> new BadRequestException("회원 조회 실패"));
        Member toMember = memberRepository.findByLoginId("lim3478").orElseThrow(() -> new BadRequestException("회원 조회 실패"));

        Friendship friendshipFrom = Friendship.builder()
                .member(fromMember)
                .memberLoginId("hyun3478")
                .friendLoginId("lim3478")
                .status(FriendshipStatus.ACCEPT)
                .isFrom(true)
                .build();

        // 보내는 사람에게 저장될 친구 요청
        Friendship friendshipTo = Friendship.builder()
                .member(toMember)
                .memberLoginId("lim3478")
                .friendLoginId("hyun3478")
                .status(FriendshipStatus.ACCEPT)
                .isFrom(false)
                .build();

        friendshipRepository.save(friendshipTo);
        friendshipRepository.save(friendshipFrom);

        Member fromMember2 = memberRepository.findByLoginId("hyun3478").orElseThrow(() -> new BadRequestException("회원 조회 실패"));
        Member toMember2 = memberRepository.findByLoginId("solim12").orElseThrow(() -> new BadRequestException("회원 조회 실패"));

        Friendship friendshipFrom2 = Friendship.builder()
                .member(fromMember2)
                .memberLoginId("hyun3478")
                .friendLoginId("solim12")
                .status(FriendshipStatus.ACCEPT)
                .isFrom(true)
                .build();

        // 보내는 사람에게 저장될 친구 요청
        Friendship friendshipTo2 = Friendship.builder()
                .member(toMember2)
                .memberLoginId("solim12")
                .friendLoginId("hyun3478")
                .status(FriendshipStatus.ACCEPT)
                .isFrom(false)
                .build();

        friendshipRepository.save(friendshipTo2);
        friendshipRepository.save(friendshipFrom2);

        Member fromMember3 = memberRepository.findByLoginId("hyun3478").orElseThrow(() -> new BadRequestException("회원 조회 실패"));
        Member toMember3 = memberRepository.findByLoginId("lee12").orElseThrow(() -> new BadRequestException("회원 조회 실패"));

        Friendship friendshipFrom3 = Friendship.builder()
                .member(fromMember3)
                .memberLoginId("hyun3478")
                .friendLoginId("lee12")
                .status(FriendshipStatus.ACCEPT)
                .isFrom(true)
                .build();

        // 보내는 사람에게 저장될 친구 요청
        Friendship friendshipTo3 = Friendship.builder()
                .member(toMember3)
                .memberLoginId("lee12")
                .friendLoginId("hyun3478")
                .status(FriendshipStatus.ACCEPT)
                .isFrom(false)
                .build();

        friendshipRepository.save(friendshipTo3);
        friendshipRepository.save(friendshipFrom3);

        Member fromMember4 = memberRepository.findByLoginId("hyun3478").orElseThrow(() -> new BadRequestException("회원 조회 실패"));
        Member toMember4 = memberRepository.findByLoginId("member5").orElseThrow(() -> new BadRequestException("회원 조회 실패"));

        Friendship friendshipFrom4 = Friendship.builder()
                .member(fromMember4)
                .memberLoginId("hyun3478")
                .friendLoginId("member5")
                .status(FriendshipStatus.ACCEPT)
                .isFrom(true)
                .build();

        // 보내는 사람에게 저장될 친구 요청
        Friendship friendshipTo4 = Friendship.builder()
                .member(toMember4)
                .memberLoginId("member5")
                .friendLoginId("hyun3478")
                .status(FriendshipStatus.ACCEPT)
                .isFrom(false)
                .build();

        friendshipRepository.save(friendshipTo4);
        friendshipRepository.save(friendshipFrom4);

        Member fromMember5 = memberRepository.findByLoginId("hyun3478").orElseThrow(() -> new BadRequestException("회원 조회 실패"));
        Member toMember5 = memberRepository.findByLoginId("member6").orElseThrow(() -> new BadRequestException("회원 조회 실패"));

        Friendship friendshipFrom5 = Friendship.builder()
                .member(fromMember5)
                .memberLoginId("hyun3478")
                .friendLoginId("member6")
                .status(FriendshipStatus.ACCEPT)
                .isFrom(true)
                .build();

        // 보내는 사람에게 저장될 친구 요청
        Friendship friendshipTo5 = Friendship.builder()
                .member(toMember5)
                .memberLoginId("member6")
                .friendLoginId("hyun3478")
                .status(FriendshipStatus.ACCEPT)
                .isFrom(false)
                .build();

        friendshipRepository.save(friendshipTo5);
        friendshipRepository.save(friendshipFrom5);

        Member fromMember6 = memberRepository.findByLoginId("member7").orElseThrow(() -> new BadRequestException("회원 조회 실패"));
        Member toMember6 = memberRepository.findByLoginId("hyun3478").orElseThrow(() -> new BadRequestException("회원 조회 실패"));

        Friendship friendshipFrom6 = Friendship.builder()
                .member(fromMember6)
                .memberLoginId("member7")
                .friendLoginId("hyun3478")
                .status(FriendshipStatus.WAITING)
                .isFrom(true)
                .build();

        // 보내는 사람에게 저장될 친구 요청
        Friendship friendshipTo6 = Friendship.builder()
                .member(toMember6)
                .memberLoginId("hyun3478")
                .friendLoginId("member7")
                .status(FriendshipStatus.WAITING)
                .isFrom(false)
                .build();

        friendshipRepository.save(friendshipTo6);
        friendshipRepository.save(friendshipFrom6);
    }
}