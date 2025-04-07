package example1.restful_api.sample;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.Arrays;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Log4j2
public class SampleControllerTest {

    @LocalServerPort
    private int port;


    @Autowired(required = false)
    private TestRestTemplate testRestTemplate;

    @Test
    public void testHello() {
        String url = "http://localhost:" + port + "/api/v1/sample/hello";
        String[] result = testRestTemplate.getForObject(url, String[].class);
        log.info(Arrays.toString(result));
    }

}
