/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package practico.com.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Item extends Pelicula {
    private int cantidad;

    public Item() {
    }

    public Item(Pelicula pelicula) {
        super.setIdPelicula(pelicula.getIdPelicula());
        super.setCategoria(pelicula.getCategoria());
        super.setTitulo(pelicula.getTitulo());
        super.setDirector(pelicula.getDirector());
        super.setDuracionMin(pelicula.getDuracionMin());
        super.setCosto(pelicula.getCosto());
        super.setReservado(pelicula.isReservado());
        super.setRutaImagen(pelicula.getRutaImagen());
        this.cantidad = 0;
    }

    public Integer getPrecio() {
        return this.getCosto();
    }

    public void setId(Long id) {
        this.setIdPelicula(id);
    }
}
