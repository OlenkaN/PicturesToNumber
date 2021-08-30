package controllerTest;

import com.example.ptn.PicturesToNumberApplication;
import com.example.ptn.nn.NeuralNetwork;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = PicturesToNumberApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application.properties")
public class PictureControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Configuration
    static class TestConfig {
        @MockBean
        NeuralNetwork neuralNetworkTest;
    }

    @Test
    public void whenFileUploaded_thenPredict()
            throws Exception {
        FileInputStream file = new FileInputStream("src/test/resources/0.jpg");
        MockMultipartFile multipartFile = new MockMultipartFile("file", "0.jpg", String.valueOf(MediaType.MULTIPART_FORM_DATA), file);

        this.mockMvc.perform(multipart("/api/fileUpload/predict")
                .file(multipartFile)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());
    }

    @Test
    public void whenFileUploaded_thenTrain()
            throws Exception {
        FileInputStream file = new FileInputStream("src/test/resources/0.jpg");
        MockMultipartFile multipartFile = new MockMultipartFile("file", "0.jpg", String.valueOf(MediaType.MULTIPART_FORM_DATA), file);

        this.mockMvc.perform(multipart("/api/fileUpload/train")
                .file(multipartFile)
                .file(new MockMultipartFile("label", "label", String.valueOf(MediaType.TEXT_PLAIN), "0".getBytes(StandardCharsets.UTF_8))))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));
    }
}
