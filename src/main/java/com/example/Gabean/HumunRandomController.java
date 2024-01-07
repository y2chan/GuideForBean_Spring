package com.example.Gabean;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HumunRandomController {

    @GetMapping("/humun_random")
    public ModelAndView humunRandom() {
        return new ModelAndView("humun_random");
    }

    @GetMapping("/m/m_humun_random")
    public ModelAndView mobilehumunRandom() {
        return new ModelAndView("mobile/m_humun_random");
    }
}
