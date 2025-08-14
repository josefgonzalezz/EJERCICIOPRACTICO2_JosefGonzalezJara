/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package practico.com.service.impl;

import practico.com.dao.FacturaDao;
import practico.com.dao.PeliculaDao;
import practico.com.dao.UsuarioDao;
import practico.com.dao.VentaDao;
import practico.com.domain.Usuario;
import practico.com.domain.Factura;
import practico.com.domain.Item;
import practico.com.domain.Pelicula;
import practico.com.domain.Venta;
import practico.com.service.ItemService;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private HttpSession session;

    @Override
    public List<Item> gets() {
        @SuppressWarnings("unchecked")
        List<Item> listaItems = (List<Item>) session.getAttribute("listaItems");
        return listaItems;
    }

    @Override
    public Item get(Item item) {
        @SuppressWarnings("unchecked")
        List<Item> listaItems = (List<Item>) session.getAttribute("listaItems");
        if (listaItems != null) {
            for (Item i : listaItems) {
                if (Objects.equals(i.getIdPelicula(), item.getIdPelicula())) {
                    return i;
                }
            }
        }
        return null;
    }

    @Override
    public void delete(Item item) {
        @SuppressWarnings("unchecked")
        List<Item> listaItems = (List<Item>) session.getAttribute("listaItems");
        if (listaItems != null) {
            var posicion = -1;
            var existe = false;
            for (var i : listaItems) {
                posicion++;
                if (Objects.equals(i.getIdPelicula(), item.getIdPelicula())) {
                    existe = true;
                    break;
                }
            }
            if (existe) {
                listaItems.remove(posicion);
                session.setAttribute("listaItems", listaItems);
            }
        }
    }

    @Override
    public void save(Item item) {
        @SuppressWarnings("unchecked")
        List<Item> listaItems = (List<Item>) session.getAttribute("listaItems");
        if (listaItems == null) {
            listaItems = new ArrayList<>();
        }
        var existe = false;
        for (var i : listaItems) {
            if (Objects.equals(i.getIdPelicula(), item.getIdPelicula())) {
                existe = true;
                if (i.getCantidad() < i.getExistencias()) {
                    i.setCantidad(i.getCantidad() + 1);
                }
                break;
            }
        }
        if (!existe) {
            item.setCantidad(1);
            listaItems.add(item);
        }
        session.setAttribute("listaItems", listaItems);
    }

    @Override
    public void update(Item item) {
        @SuppressWarnings("unchecked")
        List<Item> listaItems = (List<Item>) session.getAttribute("listaItems");
        if (listaItems != null) {
            for (var i : listaItems) {
                if (Objects.equals(i.getIdPelicula(), item.getIdPelicula())) {
                    i.setCantidad(item.getCantidad());
                    session.setAttribute("listaItems", listaItems);
                    break;
                }
            }
        }
    }

    @Autowired
    private UsuarioDao usuarioDao;

    @Autowired
    private PeliculaDao peliculaDao;

    @Autowired
    private FacturaDao facturaDao;

    @Autowired
    private VentaDao ventaDao;

    @Override
    public void facturar() {
        // Obtener el usuario autenticado
        String username = "";
        var principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
        } else {
            if (principal != null) {
                username = principal.toString();
            }
        }

        if (username.isBlank()) {
            System.out.println("username en blanco...");
            return;
        }

        Usuario usuario = usuarioDao.findByUsername(username);
        if (usuario == null) {
            System.out.println("Usuario no existe en usuarios...");
            return;
        }

        // Crear factura
        Factura factura = new Factura(usuario.getIdUsuario());
        factura = facturaDao.save(factura);

        // Procesar cada película en el carrito
        @SuppressWarnings("unchecked")
        List<Item> listaItems = (List<Item>) session.getAttribute("listaItems");
        if (listaItems != null) {
            double total = 0;
            for (Item i : listaItems) {
                Pelicula pelicula = peliculaDao.getReferenceById(i.getIdPelicula());
                if (pelicula.getExistencias() >= i.getCantidad()) {
                    Venta venta = new Venta(factura.getIdFactura(),
                            i.getIdPelicula(),
                            i.getPrecio(),
                            i.getCantidad());
                    ventaDao.save(venta);
                    pelicula.setExistencias(pelicula.getExistencias() - i.getCantidad());
                    peliculaDao.save(pelicula);
                    total += i.getCantidad() * i.getPrecio();
                }
            }

            // Actualizar el total de la factura
            factura.setTotal(total);
            facturaDao.save(factura);

            // Limpiar el carrito
            listaItems.clear();
        }
    }

    @Override
    public double getTotal() {
        @SuppressWarnings("unchecked")
        var listaItems = (List<Item>) session.getAttribute("listaItems");
        double total = 0;
        if (listaItems != null) {
            for (Item i : listaItems) {
                total += i.getCantidad() * i.getPrecio();
            }
        }
        return total;
    }
}
