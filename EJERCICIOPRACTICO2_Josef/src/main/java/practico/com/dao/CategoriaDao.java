/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package practico.com.dao;

import practico.com.domain.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CategoriaDao extends JpaRepository <Categoria,Long> {
    
}
 
