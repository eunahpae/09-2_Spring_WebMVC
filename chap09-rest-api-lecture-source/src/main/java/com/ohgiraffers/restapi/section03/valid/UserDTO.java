package com.ohgiraffers.restapi.section03.valid;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.util.Date;

public class UserDTO {

    /*
     *  유효성 체크 어노테이션)
     *  - @Null        : 값이 반드시 null 이어야 함
     *  - @NotNull     : null이 아닌 값을 허용 ("" 빈 문자열은 허용됨)
     *  - @NotEmpty    : null, "" 모두 허용하지 않음 (" "는 허용)
     *  - @NotBlank    : null, "", " " 모두 허용하지 않음 (문자열 공백도 허용하지 않음)
     *
     *  - @Size        : 문자열, 컬렉션 등의 크기 제한 (min, max 지정 가능)
     *  - @Email       : 이메일 형식 검사
     *  - @Past        : 과거 날짜만 허용
     *  - @Future      : 미래 날짜만 허용
     */

    private int no;

    @NotNull(message = "아이디는 반드시 입력되어야 합니다.")
    @NotBlank(message = "아이디는 공백일 수 없습니다.")
    private String id;

    private String pwd;

    @NotNull(message = "이름은 반드시 입력되어야 합니다.")
    @Size(min = 2, message = "이름은 2글자 이상 입력해야 합니다.")
    private String name;

    @Past
    private Date enrollDate;

    public UserDTO() {}

    public UserDTO(int no, String id, String pwd, String name, Date enrollDate) {
        this.no = no;
        this.id = id;
        this.pwd = pwd;
        this.name = name;
        this.enrollDate = enrollDate;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getEnrollDate() {
        return enrollDate;
    }

    public void setEnrollDate(Date enrollDate) {
        this.enrollDate = enrollDate;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "no=" + no +
                ", id='" + id + '\'' +
                ", pwd='" + pwd + '\'' +
                ", name='" + name + '\'' +
                ", enrollDate=" + enrollDate +
                '}';
    }
}
