/**
 * 借书证实体类
 */
package com.library.model;

public class Card {
    private String cardId;
    private String name;
    private String department;
    private String type;

    public Card() {
    }

    public Card(String cardId, String name, String department, String type) {
        this.cardId = cardId;
        this.name = name;
        this.department = department;
        this.type = type;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}