package com.boot.board_240718.validator;

import com.boot.board_240718.model.Board;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@Slf4j
public class BoardValidator implements Validator{
    @Override
    public boolean supports(Class clazz) {
//        return false;  오버라이딩 defualt 리턴값
//        return Board.class.isAssignableFrom(clazz);
        return Board.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors e) {
        log.info("@# valiate()");

        Board b = (Board) obj;
        log.info("@# b");

        if(b.getContent().equals("")) {//내용이 빈값이면
            e.rejectValue("content","key","내용을 입력하세요.");
        }

       /* ValidationUtils.rejectIfEmpty(e, "name", "name.empty");
        Person p = (Person) obj;
        if (p.getAge() < 0) {
            e.rejectValue("age", "negativevalue");
        } else if (p.getAge() > 110) {
            e.rejectValue("age", "too.darn.old");
        }
*/

    }
}
