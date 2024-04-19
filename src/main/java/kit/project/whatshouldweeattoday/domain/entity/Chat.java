package kit.project.whatshouldweeattoday.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Chat {
    @Id
    @GeneratedValue
    @Column(name = "CHAT_ID")
    private Long id;
    private String chatRoomName;
    private String message;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "meet_id")
    private Meet meet;

    @OneToMany(mappedBy = "chat", orphanRemoval = true)
    private List<Vote> votes = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "notice_id")
    private Notice notice;
}