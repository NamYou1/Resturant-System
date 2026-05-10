package com.saranaresturantsystem.common;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.multipart.MultipartFile;
import java.beans.PropertyEditorSupport;

@ControllerAdvice
public class GlobalBindingAdvice {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(MultipartFile.class, new PropertyEditorSupport() {
            @Override
            public void setValue(Object value) {
                if (value instanceof MultipartFile) {
                    super.setValue(value);
                } else {
                    super.setValue(null);
                }
            }
        });
    }
}