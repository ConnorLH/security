package cn.corner.web.wiremock;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class MockServer {

    public static void main(String[] args) throws IOException {
        configureFor("localhost",8062);
        removeAllMappings();

        mock("/order/1","01");
    }

    private static void mock(String url, String file) throws IOException {
        ClassPathResource resource = new ClassPathResource("mock/response/"+file+".txt");
        String content = FileUtils.readFileToString(resource.getFile(),"utf-8");
        stubFor(get(urlPathEqualTo(url)).willReturn(aResponse().withBody(content).withStatus(200)));
    }
}
