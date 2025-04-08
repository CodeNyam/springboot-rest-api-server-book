package example1.restful_api.repository;

import org.springframework.data.domain.Page;
import example1.restful_api.entity.TodoEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ToDoRepository extends JpaRepository<TodoEntity, Long> {
    // Spring Data JPA의 JpaRepository를 상속받음
    // => 기본적 등록, 수정, 삭제, 조회, 페이징 기능 등을 정의없이 사용 가능!

    // @Query 이용
    @Query("SELECT t FROM TodoEntity t ")
    Page<TodoEntity> listAll(Pageable pageable);
}
