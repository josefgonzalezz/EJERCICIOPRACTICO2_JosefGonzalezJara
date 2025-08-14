/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package practico.com.service;

import practico.com.domain.Usuario;
import java.util.List;

public interface UsuarioService {
    
    
    public List<Usuario> getUsuarios();
    
  
    public Usuario getUsuario(Usuario usuario);
    
  
    public Usuario getUsuarioPorUsername(String username);

    
    public Usuario getUsuarioPorUsernameYPassword(String username, String password);
    
   
    public Usuario getUsuarioPorUsernameOCorreo(String username, String correo);
    
    
    public boolean existeUsuarioPorUsernameOCorreo(String username, String correo);
    
   
    public void save(Usuario usuario,boolean crearRolUser);
    
   
    public void delete(Usuario usuario);
    
}
