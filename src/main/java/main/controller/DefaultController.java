package main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DefaultController {

  @RequestMapping("/")
  public String index() {
    return "index";
  }

  @GetMapping(value = {"/{regex:\\w+}", "/**/{regex:\\w+}", "/**/**/{regex:\\w+}",
      "/calendar/**/**"})
  public String forward404() {
    return "forward:/";
  }
}