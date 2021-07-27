package com.example.PicturesToNumber.сontrollers;


import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * this controller is required
 * to handle post, get and other requests from the client
 */
@RestController
@RequestMapping("/api")
public class PictureController {
    /**
     * This method upload file and check if it is empty or not
     *
     * @param file is file that need to be upload ( can be any type)
     * @param req
     * @return string message of success or not
     */
    @RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
    public @ResponseBody
    String uploadFile(@RequestPart("file") MultipartFile file, HttpServletRequest req) {

        if (file.isEmpty())
            return "FILE EMPTY";

        return "success";
    }


}
