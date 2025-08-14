/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package practico.com.service.impl;

import practico.com.dao.PeliculaDao;
import practico.com.domain.Pelicula;
import practico.com.service.PeliculaService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PeliculaServiceImpl implements PeliculaService {

    @Autowired
    private PeliculaDao peliculaDao;

    @Override
    @Transactional(readOnly = true)
    public List<Pelicula> getPeliculas(boolean reservado) {
        var lista = peliculaDao.findAll();
        if (reservado) {
            lista.removeIf(p -> !p.isReservado());
        }
        return lista;
    }

    @Override
    @Transactional(readOnly = true)
    public Pelicula getPelicula(Pelicula pelicula) {
        return peliculaDao.findById(pelicula.getIdPelicula()).orElse(null);
    }

    @Override
    @Transactional
    public void save(Pelicula pelicula) {
        peliculaDao.save(pelicula);
    }

    @Override
    @Transactional
    public void delete(Pelicula pelicula) {
        peliculaDao.delete(pelicula);
    }

    @Override
    @Transactional(readOnly = true)
    public Pelicula buscarPorId(Long id) {
        return peliculaDao.findById(id).orElse(null);
    }
}

