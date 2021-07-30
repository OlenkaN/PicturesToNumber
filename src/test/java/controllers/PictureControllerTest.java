package controllers;


import com.example.PicturesToNumber.nn.Initialize;
import com.example.PicturesToNumber.nn.NeuralNetwork;
import com.example.PicturesToNumber.сontrollers.PictureController;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PictureController.class)
public class PictureControllerTest {

    @MockBean
    private Initialize initialize;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAllEmployeesAPI() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/fileUpload")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }


}
