
package com.example.Gabean;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class GraduateController {

    @RequestMapping("/info_graduate")
    public ModelAndView infoGraduate() {
        ModelAndView mv = new ModelAndView("info_graduate");
        return mv;
    }

    @RequestMapping("/m/m_info_graduate")
    public ModelAndView mobileinfoGraduate() {
        ModelAndView mv = new ModelAndView("mobile/m_info_graduate");
        return mv;
    }
}