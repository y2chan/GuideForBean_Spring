package com.example.Gabean;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SugangController {

    @RequestMapping("/info_sugang")
    public ModelAndView infoSugang() {
        ModelAndView mv = new ModelAndView("info_sugang");
        return mv;
    }
}
