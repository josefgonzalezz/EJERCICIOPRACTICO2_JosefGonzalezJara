/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package practico.com.service.impl;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.Storage.SignUrlOption;
import com.google.cloud.storage.StorageOptions;
import practico.com.service.FirebaseStorageService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;


import org.springframework.web.multipart.MultipartFile;

@Service
public class FireBaseStorageServiceImpl implements FirebaseStorageService {
    
    @Override
    public String cargarImagen(MultipartFile archivoLocalCliente, String carpeta, Long id) {
        try {
            // El nombre original del archivo local del cliente
            String extension = archivoLocalCliente.getOriginalFilename();
            
            // Se genera el nombre según el código del artículo
            String fileName = "img" + sacaNumero(id) + extension;
            
            // Se convierte/sube el archivo a un archivo temporal
            File file = this.convertToFile(archivoLocalCliente);
            
            // Se copia a Firebase y se obtiene el enlace de la imagen (por 10 años)
            String URL = this.uploadFile(file, carpeta, fileName);
            
            // Se elimina el archivo temporal generado desde el cliente
            file.delete();
            
            return URL;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private String uploadFile(File file,
                             String carpeta,
                             String fileName) throws IOException {
        //Se define el lugar y acceso al archivo imagen
        ClassPathResource json = new ClassPathResource("firebase/techshop-90813.json");
        BlobId blobId = BlobId.of(BucketName, rutaSuperiorStorage + "/" + carpeta + "/" + fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType("image/jpg").build();
        
        Credentials credentials = GoogleCredentials
                .fromStream(json.getInputStream())
                .createScoped(Arrays.asList("https://www.googleapis.com/auth/cloud-platform"));
        
        Storage storage = StorageOptions.newBuilder()
                .setCredentials(credentials).build().getService();
        
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));
        
        String url = storage.signUrl(blobInfo,
                360,
                TimeUnit.DAYS,
                SignUrlOption.signWith((ServiceAccountCredentials) credentials))
                .toString();
        
        return url;
    }
    
    //Método utilitario que convierte el archivo desde el equipo local
    //del usuario a un archivo temporal en el servidor
    private File convertToFile(MultipartFile archivoLocalCliente) throws IOException {
        File tempFile = File.createTempFile("img", null);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(archivoLocalCliente.getBytes());
            fos.close();
        }
        return tempFile;
    }
    
    //Método utilitario para obtener un string con ceros....
    private String sacaNumero(Long id) {
        return String.format("%04d", id);
    }
}