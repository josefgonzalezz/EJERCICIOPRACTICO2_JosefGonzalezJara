/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package practico.com.service;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FirebaseStorageService {

    public String cargarImagen(MultipartFile archivoLocalCliente, String carpeta, Long id);

   
    final String BucketName = "techshop-90813.appspot.com";

    
    final String rutaSuperiorStorage = "techshop";

    
    final String rutaJsonFile = "firebase";

    
    final String archivoJsonFile = "techshop-90813-firebase-adminsdk-fbsvc-eaecb38960"; 
}