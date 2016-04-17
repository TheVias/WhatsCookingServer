/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package whatscookingserver;

import com.application.vias.what_s_cooking.entity.Ingredient;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity-класс ингредиента
 */
@Entity
@Table(name="Ingredient")
public class IngredientHib implements Serializable{
    
    private int id;
    private String name;
    private int category;
   
    public IngredientHib() {
    }
    
    public IngredientHib(int id, String name, int category) {
        this.id = id;
        this.name = name;
        this.category = category;
    }

    @Column(name="category")
    public int getCategory() {
        return category; //To change body of generated methods, choose Tools | Templates.
    }

    @Column(name="name")
    public String getName() {
        return name; //To change body of generated methods, choose Tools | Templates.
    }

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="id")    
    public int getId() {
        return id; //To change body of generated methods, choose Tools | Templates.
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(int category) {
        this.category = category;
    }
    
    public Ingredient convertToIngredient() {
        return new Ingredient(id,name,category);
    }
    
}
