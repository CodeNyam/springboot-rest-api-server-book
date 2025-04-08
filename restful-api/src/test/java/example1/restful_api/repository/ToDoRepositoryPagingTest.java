package example1.restful_api.repository;

import example1.restful_api.entity.TodoEntity;
import org.aspectj.apache.bcel.classfile.SourceFile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// @Entity와 같이 데이터베이스 부분만 실행
@DataJpaTest

// 테스트할 때 자동 임베디드 DB(ex. h2)로 교체되는 것을 막음
// @AutoConfigureTestDatabase : 데이터베이스 조작을 테스트할 때 DB 설정
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

// @Transactional : 트랜잭션 실행 여부
// 여기서는 트랜잭션이 돌아가지 않음. -> 메서드 단위로 트랜잭션이 처리되지 않음.
// 트랜잭션 : DB의 작업 단위
@Transactional(propagation = Propagation.NOT_SUPPORTED)

public class ToDoRepositoryPagingTest {

    @Autowired
    private ToDoRepository todoRepository;

    @Test
    void testPaging() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());

        Page<TodoEntity> result = todoRepository.findAll(pageable);
        System.out.println(result.getTotalPages());
        System.out.println(result.getTotalElements());

        List<TodoEntity> todoEntityList = result.getContent();

        todoEntityList.forEach(todoEntity -> System.out.println(todoEntity));

    }
}
