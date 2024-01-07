package com.example.Gabean;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CampusmapDetailController {

    @RequestMapping("/campusmap_detail")
    public ModelAndView CampusmapDetail() {
        ModelAndView mv = new ModelAndView("campusmap_detail");
        return mv;
    }

    @RequestMapping("/m/m_campusmap_detail")
    public ModelAndView mobileCampusmapDetail() {
        ModelAndView mv = new ModelAndView("mobile/m_campusmap_detail");
        return mv;
    }
}