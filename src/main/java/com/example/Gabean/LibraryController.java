package com.example.Gabean;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LibraryController {
    @RequestMapping("/info_library")
    public ModelAndView infoLibrary() {
        ModelAndView mv = new ModelAndView("info_library");
        return mv;
    }
}
