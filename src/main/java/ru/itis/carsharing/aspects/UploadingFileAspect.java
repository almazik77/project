package ru.itis.carsharing.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itis.carsharing.services.MailService;


import java.util.Arrays;


@Slf4j
@Aspect
@Component
public class UploadingFileAspect {

    @Autowired
    private MailService mailService;

    // TODO
//    @AfterReturning(pointcut = "execution(* ru.itis.services.FileService.save(..))", returning = "returnValue")
    public void afterReturning(Object returnValue) {
  //      FileInfoDto fileInfoDto = (FileInfoDto) returnValue;
    //    log.info("notifying " + fileInfoDto);
      //  mailService.uploadNotify(fileInfoDto);
    }

}
