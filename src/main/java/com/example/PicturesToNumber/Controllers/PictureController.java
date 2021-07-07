package com.example.PicturesToNumber.Controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

@RestController
@RequestMapping("/api")
public class PictureController {

   @RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
    public @ResponseBody String uploadFile(MultipartFile file, HttpServletRequest req)
            throws SQLException {

        // using file.

        return "success";
    }
    @GetMapping("/")
    public @ResponseBody String test()
            {

        // using file.

        return "TEST";
    }

}
