package ru.mineeva.parser.exception;

import org.apache.tomcat.util.http.fileupload.impl.SizeException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(FileStorageException.class)
    public ModelAndView handleException(FileStorageException exception) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message", exception.getMsg());
        modelAndView.setViewName("error");
        return modelAndView;
    }

    @ExceptionHandler(SizeException.class)
    public ModelAndView handleException(SizeException exception) {
        System.out.println(exception.toString());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message", "Размер загружаемого файла не должен превышать 1 МБ. " + exception.getMessage());
        modelAndView.setViewName("error");
        return modelAndView;
    }

}