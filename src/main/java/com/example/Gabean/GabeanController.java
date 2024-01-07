package com.example.Gabean;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class GabeanController {

    @RequestMapping("/info_gabean")
    public ModelAndView infoGabean() {
        ModelAndView mv = new ModelAndView("info_gabean");
        return mv;
    }

    @RequestMapping("/m/m_info_gabean")
    public ModelAndView mobileinfoGabean() {
        ModelAndView mv = new ModelAndView("mobile/m_info_gabean");
        return mv;
    }
}
