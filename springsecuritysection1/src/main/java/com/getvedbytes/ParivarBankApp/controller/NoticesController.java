package com.getvedbytes.ParivarBankApp.controller;

import com.getvedbytes.ParivarBankApp.model.Notice;
import com.getvedbytes.ParivarBankApp.repository.NoticeRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@AllArgsConstructor
public class NoticesController {

    /*@GetMapping("/myNotices")
    public String getNoticeDetails(){
        return "Here are the Notice details from DB";
    }*/
    private final NoticeRepository noticeRepository;
    @GetMapping("/notices")
    public ResponseEntity<List<Notice>> getNoticeDetails(){
        List<Notice> notices=noticeRepository.findAllActiveNotices();
        if(notices!=null) {
            return ResponseEntity.ok().cacheControl(CacheControl.
                    maxAge(60, TimeUnit.SECONDS)).
                    body(notices);
        }else{
            return null;
        }
    }
}
