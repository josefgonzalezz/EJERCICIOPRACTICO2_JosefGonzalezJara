/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package practico.com.controller;

import practico.com.domain.Pelicula;
import practico.com.service.CategoriaService;
import practico.com.service.PeliculaService;
import practico.com.service.impl.FireBaseStorageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/cine")
public class PeliculaController {

    @Autowired
    private PeliculaService peliculaService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private FireBaseStorageServiceImpl firebaseStorageService;

    @GetMapping("/listado")
    public String listado(Model model) {
        var peliculas = peliculaService.getPeliculas(false);
        model.addAttribute("peliculas", peliculas);

        var categorias = categoriaService.getCategorias(false);
        model.addAttribute("categorias", categorias);

        model.addAttribute("pelicula", new Pelicula());
        model.addAttribute("totalPeliculas", peliculas.size());
        return "/cine/listado";
    }

    @GetMapping("/nuevo")
    public String peliculaNuevo(Pelicula pelicula) {
        return "/cine/modifica";
    }

    @PostMapping("/guardar")
    public String peliculaGuardar(Pelicula pelicula,
                                 @RequestParam("imagenFile") MultipartFile imagenFile) {
        if (!imagenFile.isEmpty()) {
            peliculaService.save(pelicula); // guardar para obtener id
            pelicula.setRutaImagen(
                firebaseStorageService.cargarImagen(
                    imagenFile,
                    "pelicula",
                    pelicula.getIdPelicula())
            );
        }
        peliculaService.save(pelicula); // guardar con imagen
        return "redirect:/cine/listado";
    }

    @GetMapping("/eliminar/{idPelicula}")
    public String peliculaEliminar(Pelicula pelicula) {
        peliculaService.delete(pelicula);
        return "redirect:/cine/listado";
    }

    @GetMapping("/modificar/{idPelicula}")
    public String peliculaModificar(@PathVariable Long idPelicula, Model model) {
        Pelicula pelicula = peliculaService.buscarPorId(idPelicula);
        model.addAttribute("pelicula", pelicula);

        var categorias = categoriaService.getCategorias(false);
        model.addAttribute("categorias", categorias);

        return "/cine/modifica";
    }

    @GetMapping("/reservar/{idPelicula}")
    public String reservar(@PathVariable Long idPelicula) {
        Pelicula pelicula = peliculaService.buscarPorId(idPelicula);
        if (pelicula != null) {
            pelicula.setReservado(true);
            peliculaService.save(pelicula);
        }
        return "redirect:/cine/listado";
    }

    @GetMapping("/cancelar/{idPelicula}")
    public String cancelar(@PathVariable Long idPelicula) {
        Pelicula pelicula = peliculaService.buscarPorId(idPelicula);
        if (pelicula != null) {
            pelicula.setReservado(false);
            peliculaService.save(pelicula);
        }
        return "redirect:/cine/listado";
    }
}