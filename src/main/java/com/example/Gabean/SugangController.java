package com.example.Gabean;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SugangController {

    @RequestMapping("/info_sugang")
    public ModelAndView infoSugang() {
        ModelAndView mv = new ModelAndView("info_sugang");
        return mv;
    }

    @RequestMapping("/m/m_info_sugang")
    public ModelAndView mobileinfoSugang() {
        ModelAndView mv = new ModelAndView("mobile/m_info_sugang");
        return mv;
    }
}
