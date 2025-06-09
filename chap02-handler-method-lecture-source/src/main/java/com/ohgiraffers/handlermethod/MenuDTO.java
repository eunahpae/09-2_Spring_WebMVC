package com.ohgiraffers.handlermethod;

/* DTO를 작성할 때 커맨드 객체로 이용하기 위해서는 form의 name 속성과 DTO의 필드명이 일치해야 한다.
 * → Spring에서는 요청 파라미터를 자동으로 매핑할 때 Setter 메서드를 호출하므로, 필드 이름과 form의 name 속성이 같아야 한다.
 * 예: <input type="text" name="name"> → setName() 호출됨
 */
public class MenuDTO {

    private String name;
    private int price;
    private int categoryCode;
    private String orderableStatus;

    /* 커맨드 객체는 DispatcherServlet이 기본 생성자를 통해 객체를 생성하고,
     * 이후 Setter를 이용해 요청 파라미터 값을 주입하므로 반드시 기본 생성자가 필요하다.
     */
    public MenuDTO() {
    }

    public MenuDTO(String name, int price, int categoryCode, String orderableStatus) {
        this.name = name;
        this.price = price;
        this.categoryCode = categoryCode;
        this.orderableStatus = orderableStatus;
    }

    /* 요청 파라미터의 name 속성과 일치하는 필드에 값을 바인딩할 때,
     * 내부적으로 setter 메서드(setXxx())를 호출하여 값이 주입된다.
     * 따라서 반드시 JavaBeans 규약(setXxx 형식)을 따르는 setter 메서드가 있어야 자동 바인딩이 가능하다.
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(int categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getOrderableStatus() {
        return orderableStatus;
    }

    public void setOrderableStatus(String orderableStatus) {
        this.orderableStatus = orderableStatus;
    }

    @Override
    public String toString() {
        return "MenuDTO{" +
            "name='" + name + '\'' +
            ", price=" + price +
            ", categoryCode=" + categoryCode +
            ", orderableStatus='" + orderableStatus + '\'' +
            '}';
    }
}
