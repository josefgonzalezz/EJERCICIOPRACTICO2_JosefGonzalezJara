/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package practico.com.service;

import practico.com.domain.Categoria;
import java.util.List;

public interface CategoriaService {

    public List<Categoria> getCategorias(boolean activo);

   
    public Categoria getCategoria(Categoria categoria);
    
   
    public void save(Categoria categoria);
    
    
    public void delete(Categoria categoria);
}
 