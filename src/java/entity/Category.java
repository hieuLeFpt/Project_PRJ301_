/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Trung Hieu
 */
public class Category {
    private String categoryID;
    private String categoryName;
    private String describe;

    public Category() {
    }

    public Category(String categoryID, String categoryName, String describe) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.describe = describe;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    
}
