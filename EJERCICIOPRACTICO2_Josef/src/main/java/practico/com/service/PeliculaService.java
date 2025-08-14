/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package practico.com.service;

import practico.com.domain.Pelicula;
import java.util.List;

public interface PeliculaService {

    List<Pelicula> getPeliculas(boolean reservado);

    Pelicula getPelicula(Pelicula pelicula);

    void save(Pelicula pelicula);

    void delete(Pelicula pelicula);

    
    Pelicula buscarPorId(Long id);
}

