package example1.restful_api.sample.controller;

import example1.restful_api.sample.service.SampleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2   // 책에서는 Log4j를 사용
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/sample")
public class SampleController {

    // SampleService를 생성자 주입으로 DI
    private final SampleService sampleService;
    // 터미널 확인시
//    Creating shared instance of singleton bean 'sampleService'
//    Autowiring by type from bean name 'sampleController' via constructor to bean named 'sampleService'



    @RequestMapping("/hello")
    public String[] hello() {
        return new String[] {"hello", "spring!"};
    }
    // api/v1/sample/hello로 이동 시 결과
    // ["hello","spring!"]
    // 네트워크 -> 헤더 확인시 Content-Type : application/json 임을 확인!


}
