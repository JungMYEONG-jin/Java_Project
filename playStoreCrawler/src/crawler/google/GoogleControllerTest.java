package crawler.google;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GoogleControllerTest {

    GoogleController controller = new GoogleController();

    @Test
    void mappingTest() {
        Map<String, String> clientInfo = controller.getClientInfo();
        for (String s : clientInfo.keySet()) {
            System.out.println("key = "+s+" value = "+clientInfo.get(s));
        }
    }

    @Test
    void getTokenTest() {
        String accessToken = controller.getAccessToken();
        System.out.println("accessToken = " + accessToken);
    }

    @Test
    void getReviewTest() {
        String reviewDetails = controller.getReviewDetails("com.shinhan.o2o", "ya29.a0AVA9y1vm0Z8hVMAt9tnRX8ErKoTSTDLqAR1EgMbtdho_x0jAU33O9evIL0pV7P2a4Y4N6c_jRvrhGIjbKvS_XoiKQY65t079cZminKhEWNJeKcdZf16Bfu2mAvTvVTg5HKeLy0_aZ796OvGID80GLsa6y6RGaCgYKATASAQASFQE65dr8V2DRwTHs5koBPlr9rKE8Iw0163");
        System.out.println("reviewDetails = " + reviewDetails);
    }
}

