package com.example.Gabean;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CampusmapController {

    @RequestMapping("/campusmap")
    public ModelAndView Campusmap() {
        ModelAndView mv = new ModelAndView("campusmap");
        return mv;
    }

    @RequestMapping("/m/m_campusmap")
    public ModelAndView mobileCampusmap() {
        ModelAndView mv = new ModelAndView("mobile/m_campusmap");
        return mv;
    }
}