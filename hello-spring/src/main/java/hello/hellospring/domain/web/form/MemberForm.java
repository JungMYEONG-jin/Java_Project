package hello.hellospring.domain.web.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MemberForm {


    @NotEmpty(message = "이름은 NULL이 될 수 없습니다..")
    private String name;
    private String city;
    private String street;
    private String zipcode;
}
