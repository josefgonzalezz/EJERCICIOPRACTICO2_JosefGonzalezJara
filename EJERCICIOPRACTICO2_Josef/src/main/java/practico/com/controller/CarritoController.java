/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package practico.com.controller;

import practico.com.domain.Item;
import practico.com.domain.Pelicula;
import practico.com.service.ItemService;
import practico.com.service.PeliculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CarritoController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private PeliculaService peliculaService;

    
    @GetMapping("/carrito/listado")
    public String inicio(Model model) {
        List<Item> items = itemService.gets();
        if (items == null) {
            items = new ArrayList<>(); 
        }
        model.addAttribute("items", items);

        int carritoTotalVenta = 0;
        for (Item i : items) {
            carritoTotalVenta += (i.getCantidad() * i.getPrecio());
        }
        model.addAttribute("carritoTotal", carritoTotalVenta);
        return "/carrito/listado";
    }   
    @GetMapping("/carrito/agregar/{idPelicula}")
    public ModelAndView agregarItem(Model model, @PathVariable Long idPelicula) {
        Item item = new Item();
        item.setId(idPelicula);

        Item item2 = itemService.get(item);
        if (item2 == null) {
            Pelicula pelicula = peliculaService.getPelicula(item);
            if (pelicula == null) {
                // Manejo si la película no existe
                return new ModelAndView("redirect:/");
            }
            item2 = new Item(pelicula);
        }

        itemService.save(item2);

        List<Item> lista = itemService.gets();
        if (lista == null) {
            lista = new ArrayList<>();
        }

        int totalCarritos = 0;
        int carritoTotalVenta = 0;
        for (Item i : lista) {
            totalCarritos += i.getCantidad();
            carritoTotalVenta += (i.getCantidad() * i.getPrecio());
        }

        model.addAttribute("listaItems", lista);
        model.addAttribute("listaTotal", totalCarritos);
        model.addAttribute("carritoTotal", carritoTotalVenta);

        return new ModelAndView("/carrito/fragmentos :: verCarrito");
    }

    
    @GetMapping("/carrito/modificar/{idPelicula}")
    public String modificarItem(Item item, Model model) {
        Item existingItem = itemService.get(item);
        if (existingItem == null) {
          
            return "redirect:/carrito/listado";
        }
        model.addAttribute("item", existingItem);
        return "/carrito/modifica";
    }

    
    @GetMapping("/carrito/eliminar/{idPelicula}")
    public String eliminarItem(Item item) {
        Item existingItem = itemService.get(item);
        if (existingItem != null) {
            itemService.delete(existingItem);
        }
        return "redirect:/carrito/listado";
    }

   
    @PostMapping("/carrito/guardar")
    public String guardarItem(Item item) {
        Item existingItem = itemService.get(item);
        if (existingItem != null) {
            existingItem.setCantidad(item.getCantidad());
            itemService.update(existingItem);
        }
        return "redirect:/carrito/listado";
    }

  
    @GetMapping("/facturar/carrito")
    public String facturarCarrito() {
        itemService.facturar();
        return "redirect:/";
    }
}
