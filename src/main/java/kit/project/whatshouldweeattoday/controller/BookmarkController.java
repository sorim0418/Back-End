package kit.project.whatshouldweeattoday.controller;

import kit.project.whatshouldweeattoday.domain.dto.bookmark.BookmarkRequestDTO;
import kit.project.whatshouldweeattoday.domain.dto.bookmark.BookmarkResponseDTO;
import kit.project.whatshouldweeattoday.domain.dto.likes.LikesRequestDTO;
import kit.project.whatshouldweeattoday.domain.dto.restaurant.RestaurantResponseDTO;
import kit.project.whatshouldweeattoday.domain.dto.review.MsgResponseDTO;
import kit.project.whatshouldweeattoday.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    //맛집 즐겨찾기
    @PostMapping("/api/restaurant/{restaurantId}/bookmark")
    public Long bookmark(@PathVariable Long restaurantId) {
        BookmarkRequestDTO bookmarkRequestDTO = new BookmarkRequestDTO();
        bookmarkService.save(restaurantId, bookmarkRequestDTO);
        return restaurantId;
    }

    //즐겨찾기 취소
    @DeleteMapping("/api/restaurant/{restaurantId}/bookmark/{bookmarkId}")
    public ResponseEntity<MsgResponseDTO> deleteBookmark(@PathVariable Long restaurantId, @PathVariable Long bookmarkId) {
        return new ResponseEntity<>(bookmarkService.delete(restaurantId,bookmarkId), HttpStatus.OK);
    }

    //즐겨찾기 조회
    @GetMapping("/restaurant/bookmark")
    public ResponseEntity<Page<BookmarkResponseDTO>> findAll(@PageableDefault(sort = "id", direction = Sort.Direction.ASC, size = 10) Pageable pageable) {
        Page<BookmarkResponseDTO> page = bookmarkService.findAllBookmarks(pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
