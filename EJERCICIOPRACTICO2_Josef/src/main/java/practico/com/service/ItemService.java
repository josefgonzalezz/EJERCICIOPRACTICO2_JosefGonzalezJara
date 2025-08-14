/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package practico.com.service;


import  practico.com.domain.Item;
import java.util.List;

public interface ItemService {

    
    public List<Item> gets();

  
    public Item get(Item item);

   
    public void delete(Item item);

    
    public void save(Item item);

    public void update(Item item);

    public void facturar();

    public double getTotal();
}
