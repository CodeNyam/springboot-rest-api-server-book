package example1.restful_api.repository;

import example1.restful_api.entity.TodoEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

// @Entity와 같이 데이터베이스 부분만 실행
@DataJpaTest

// 테스트할 때 자동 임베디드 DB(ex. h2)로 교체되는 것을 막음
// @AutoConfigureTestDatabase : 데이터베이스 조작을 테스트할 때 DB 설정
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

// @Transactional : 트랜잭션 실행 여부
// 여기서는 트랜잭션이 돌아가지 않음. -> 메서드 단위로 트랜잭션이 처리되지 않음.
// 트랜잭션 : DB의 작업 단위
@Transactional(propagation = Propagation.NOT_SUPPORTED)

public class ToDoRepositoryTest {

    @Autowired
    private ToDoRepository todoRepository;

    // 데이터 저장
    @Test
    public void testInsert() {

        TodoEntity todoEntity = TodoEntity.builder()
                .title("Finish boot")
                .writer("user00")
                .dueDate(LocalDate.of(2025, 12, 23))
                .build();

        todoRepository.save(todoEntity);
        System.out.println("New TodoEntity MNO : " + todoEntity.getMno());
    }


    // 100개의 더미데이터 추가 -> DB에 저장
    @Test
    public void testInsertDummies() {
        for (int i = 0; i<100; i++) {
            TodoEntity todoEntity = TodoEntity.builder()
                    .title("Finish boot")
                    .writer("user00")
                    .dueDate(LocalDate.of(2025, 12, 23))
                    .build();

            todoRepository.save(todoEntity);
            System.out.println("New TodoEntity MNO : " + todoEntity.getMno());

        }

    }


    // 데이터 조회
    @Test
    public void testRead() {
        Long mno = 58L;

        Optional<TodoEntity> result = todoRepository.findById(mno);

        result.ifPresent(todoEntity -> System.out.println(todoEntity));
    }

    // 트랜잭션 기능 적용X
    @Test
    public void testRead2() {
        Long mno = 55L;

        Optional<TodoEntity> result = todoRepository.findById(mno);

        result.ifPresent(todoEntity -> System.out.println(todoEntity));

        Optional<TodoEntity> result2= todoRepository.findById(mno);

        result2.ifPresent(todoEntity -> System.out.println(todoEntity));

    }

    // 트랜잭션 적용
    @Test
    @Transactional
    public void testRead3() {
        Long mno = 58L;

        Optional<TodoEntity> result = todoRepository.findById(mno);

        result.ifPresent(todoEntity -> System.out.println(todoEntity));

        Optional<TodoEntity> result2= todoRepository.findById(mno);
        // -> 더티체킹 작동X
        // 즉, 이전 데이터와 내용이 같아서 SELECT 하지 않음.

        result2.ifPresent(todoEntity -> System.out.println(todoEntity));
        // 첫 번째 Select만 작동 후 toDoEntity가 2번 출력

    }
    // 트랜잭션이 활성화된 동안, 영속성이 유지되어 SELECT한 결과로 보관된 엔티티를 그대로 사용함
    // 즉, 1차 캐싱되어 있는 엔티티를 그대로 불러와 사용


    @Test
    @Transactional
    @Commit
    void testUpdateDirtyCheck() {
        Long mno = 58L;
        // 실제 DB에서도 58번째 데이터가 변경

        Optional<TodoEntity> result = todoRepository.findById(mno);
        TodoEntity todoEntity = result.get();
        System.out.println("OLD : " +todoEntity);

        todoEntity.changeTitle("Change Title..." + Math.random());
        todoEntity.changeWriter("Change Writer..." + Math.random());

        System.out.println("Changed : " + todoEntity);


        // 만일 업데이트 메서드에 @Transactional이 없다면?
        // 영속성이 제거된 상태이므로, 엔티티를 조작해도 DB에 반영되지 않는다.
        // 이때 SQL문이 3번 출력되는데...

        //        Hibernate:
//      1.   select ...
//      2.   OLD : TodoEntity(mno=58, title=Change Title...0.005241012528268918, writer=Change Writer...0.11930770128351176, dueDate=2025-12-23)
//      3.   Changed : TodoEntity(mno=58, title=Change Title...0.07425739045941404, writer=Change Writer...0.282592339228382, dueDate=2025-12-23)


        // 1번째) findById()에 의해 SELECT문 실행
        // 2, 3번째) changeTitle(), changeWriter() 메서드가 호출되고, 이후 save 호출시 실행
        // JPA 입장에선 PK가 있어 해당 데이터를 갱신해야 하는지 삽입해야 하는지 판단해야하므로 SELECT문이 싱행된다.
        // 이때 해당 객체에서 변경 사항이 있으면 update
        //
    }


    // 데이터 삭제
    @Test
    @Transactional
    @Commit
    void testDelete() {
        // 실제 DB에서 101번째 데이터 삭제
        Long mno = 101L;

        Optional<TodoEntity> result = todoRepository.findById(mno);

        result.ifPresent(todoEntity -> todoRepository.delete(todoEntity));
    }

    @Test
    @Transactional
    @Commit
    void testDelete2() {
        // 실제 DB에서 101번째 데이터 삭제
        Long mno = 101L;

        Optional<TodoEntity> result = todoRepository.findById(mno);
        System.out.println(result);
        todoRepository.deleteById(mno);
    }

}


// Note. 트랜잭션 VS 세션 명확한 차이!
// 트랜잭션 : DB 작업 단위
// 세션 : DB와 연결성 표현
// 한 세션 내에서 여러 트랜잭션이 가동 가능!

// 영속성 컨텍스트 Presistence Context : DB에서 엔티티와 트랜잭션, 연결 상태를 나타냄
// 영속적 : 트랜잭션이 활성화되어있어 DB 조작 가능
// 준영속적 : 기존에 하던 작업을 커밋하고, 트랜잭션을 종료함. 이후 작업은 저장되지 않음
// 비영속적 : DB와 아무 관련없는 상태

// 1차 캐시 : 영속 컨텍스트가 엔티티를 보관하는 곳

// 더티 체킹 : 영속적일때 엔티티를 수정, 삭제할때 DB는 이를 반영하기 위해 변경을 감지함. 이를 dirty checking 이라고 한다.
