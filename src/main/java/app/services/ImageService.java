package app.services;

import app.domain.FileObject;
import app.exceptions.IllegalFileType;
import app.repositories.ImageRepository;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * Manages image files stored in the database
 */
@Component
public class ImageService {
    
    @Autowired
    private ImageRepository imageRepo;
    
    /**
     * Get an image from database to be sent to the client
     * @param id image id
     * @return a response for the client
     */
    public ResponseEntity<byte[]> getImage(Long id) {
        FileObject image = imageRepo.findOne(id);
        
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(image.getMediaType()));
        headers.setContentLength(image.getSize());
        headers.set("filename", image.getName());
        
        return new ResponseEntity<byte[]>(image.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * Save an image to database
     * @param file image file to save
     * @return saved image's database id
     * @throws IOException 
     */
    public Long saveImage(MultipartFile file) throws IOException {
        if (!file.getContentType().matches("image/(gif|jpeg|png)")) {
            throw new IllegalFileType();
        }
        
        FileObject image = new FileObject();
        image.setName(file.getOriginalFilename());
        image.setMediaType(file.getContentType());
        image.setSize(file.getSize());
        image.setContent(file.getBytes());
        imageRepo.save(image);
        
        return image.getId();
    }
}
