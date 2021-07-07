package com.example.PicturesToNumber.Controllers;


import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

@RestController
@RequestMapping("/api")
public class PictureController {

   @RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
    public @ResponseBody String uploadFile(@RequestPart("file") MultipartFile file, HttpServletRequest req)
            throws SQLException {

        // using file.
       if(file.equals(null))
           return "FILE EMPTY";

        return "success";
    }
    @GetMapping("/")
    public @ResponseBody String test()
            {

        // using file.

        return "TEST";
    }

}
