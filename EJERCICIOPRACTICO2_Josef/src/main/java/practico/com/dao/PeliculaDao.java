/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package practico.com.dao;


import practico.com.domain.Pelicula;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PeliculaDao extends JpaRepository<Pelicula, Long> {

    List<Pelicula> findByCostoBetweenOrderByTitulo(double costoInf, double costoSup);

    @Query("SELECT p FROM Pelicula p WHERE p.costo BETWEEN :costoInf AND :costoSup ORDER BY p.titulo ASC")
    List<Pelicula> metodoJPQL(@Param("costoInf") double costoInf, @Param("costoSup") double costoSup);

    @Query(nativeQuery = true,
           value = "SELECT * FROM pelicula WHERE costo BETWEEN :costoInf AND :costoSup ORDER BY titulo ASC")
    List<Pelicula> metodoNativo(@Param("costoInf") double costoInf, @Param("costoSup") double costoSup);

}
