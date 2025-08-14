/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package practico.com.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Data;

@Data
@Entity
@Table(name = "pelicula")
public class Pelicula implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pelicula")
    private Long idPelicula;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    private String titulo;
    private String director;
    private Integer duracionMin;
    private Integer costo;       
    private Integer existencias; 
    private boolean reservado;
    private String rutaImagen;

    public Pelicula() {}

    public Pelicula(String titulo, boolean reservado) {
        this.titulo = titulo;
        this.reservado = reservado;
    }
}
