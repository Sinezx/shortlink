package com.sinezx.shortlink.controller;

import com.sinezx.shortlink.service.ShortLinkService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.Writer;

@RestController
public class RedirectController {

    @Autowired
    public ShortLinkService shortLinkService;

    @RequestMapping("/psl/{code}")
    public void parseShortLink(@PathVariable("code") String code, HttpServletResponse httpServletResponse){
        try {
            if(code.length() == 8) {
                String url = shortLinkService.getCallbackUrl(code);
                httpServletResponse.sendRedirect(url);
            }else{
                responseHandle(httpServletResponse, "invalid code");
            }
        } catch (IOException e) {
            responseHandle(httpServletResponse, "invalid url");
        }
    }

    private void responseHandle(HttpServletResponse httpServletResponse, String msg){
        try(Writer writer = httpServletResponse.getWriter()){
            writer.write(msg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
