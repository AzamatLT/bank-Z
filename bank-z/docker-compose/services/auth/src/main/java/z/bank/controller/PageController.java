package z.bank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/")
    public String redirectToIndex() {
        return "redirect:/static/index.html";
    }
}
