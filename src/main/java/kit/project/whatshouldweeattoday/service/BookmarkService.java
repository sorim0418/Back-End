package kit.project.whatshouldweeattoday.service;

import kit.project.whatshouldweeattoday.domain.dto.bookmark.BookmarkRequestDTO;
import kit.project.whatshouldweeattoday.domain.dto.bookmark.BookmarkResponseDTO;
import kit.project.whatshouldweeattoday.domain.dto.review.MsgResponseDTO;
import kit.project.whatshouldweeattoday.domain.entity.Bookmark;
import kit.project.whatshouldweeattoday.domain.entity.Member;
import kit.project.whatshouldweeattoday.domain.entity.Restaurant;
import kit.project.whatshouldweeattoday.repository.BookmarkRepository;
import kit.project.whatshouldweeattoday.repository.MemberRepository;
import kit.project.whatshouldweeattoday.repository.RestaurantRepository;
import kit.project.whatshouldweeattoday.security.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final RestaurantRepository restaurantRepository;
    private final MemberRepository memberRepository;

    // 즐겨찾기 등록
    @Transactional
    public void save(Long restaurantId, BookmarkRequestDTO bookmarkRequestDTO) {
        String loginId = SecurityUtil.getLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원입니다."));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 음식점입니다."));

        // 중복 즐겨찾기 확인
        if (bookmarkRepository.existsByMemberAndRestaurant(member, restaurant)) {
            throw new IllegalArgumentException("이미 즐겨찾기한 음식점입니다.");
        }

        Bookmark bookmark = bookmarkRequestDTO.toSaveEntity(member, restaurant);

        bookmarkRepository.save(bookmark);
    }
    // 즐겨찾기 삭제
    @Transactional
    public MsgResponseDTO delete(Long restaurantId, Long bookmarkId) {
        bookmarkRepository.deleteById(bookmarkId);
        return new MsgResponseDTO("즐겨찾기 취소", 200);
    }

    //즐겨찾기 조회
    @Transactional
    public Page<BookmarkResponseDTO> findAllBookmarks(Pageable pageable) {
        Member member = getCurrentMember();

        Page<Bookmark> bookmarkPage = bookmarkRepository.findAllByMemberId(member.getId(), pageable);
        return bookmarkPage.map(BookmarkResponseDTO::new);
    }

    private Member getCurrentMember() {
        String loginId = SecurityUtil.getLoginId();
        return memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원입니다."));
    }

}
