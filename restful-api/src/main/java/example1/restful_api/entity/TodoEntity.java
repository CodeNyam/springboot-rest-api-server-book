package example1.restful_api.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

// 클래스가 엔티티 파일임을 명시
@Entity
@Getter
@ToString

// 모든 필드를 매개변수로 갖는 생성자 제작
@AllArgsConstructor
// 필드를 매개값으로 받지 않는 생성자 제작
@NoArgsConstructor

// 해당 엔티티 클래스와 실제 DB상의 테이블을 매칭하는 정보
@Table(name="tb1_todos")

// 빌더 패턴을 위한 코드
@Builder
public class TodoEntity {

    // DB에서 기본키 지정
    @Id
    // 해당 열은 자동으로 생성됨
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mno;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(length = 500, nullable = false)
    private String writer;

    private LocalDate dueDate;

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeWriter(String writer) {
        this.writer = writer;
    }

    public void changeDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }


}
