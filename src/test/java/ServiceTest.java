import org.example.service.Service;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ServiceTest {
    Service service = new Service();

    @Test
    void testFindTheNumber(){
        List<String> actual = service.textAnalyzer("Notification has been sent 150 times to +4(351) " +
                "455 22 44 successfully. +1(431) 542 56 12 is unreachable");
        List<String> expected = Arrays.asList("43514552244", "14315425612");
        assertEquals(actual, expected);
    }

}
