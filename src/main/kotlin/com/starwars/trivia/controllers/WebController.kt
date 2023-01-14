package com.starwars.trivia.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class WebController {

    @RequestMapping("/index")
    fun index(): String {
        return "index"
    }
}