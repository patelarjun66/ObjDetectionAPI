package com.example.imagequery.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/imageProperties")
public class imagePropertiesController {
    @Autowired
    imageService ImageService;
    Integer Id = 0;

    @GetMapping("")
    public List<imageProperties> list() {
        return ImageService.listAllImages();
    }
    @GetMapping("/{id}")
    public ResponseEntity<imageProperties> get(@PathVariable Integer id) {
        try {
            imageProperties ImageProperties = ImageService.getImage(id);
            return new ResponseEntity<imageProperties>(ImageProperties, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<imageProperties>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/")
    public void add(@RequestParam("image") MultipartFile image, @RequestParam("detFlag") boolean objdetflag) throws IOException {
        ImageService.saveImage(new imageProperties(++Id,image.getBytes(), objdetflag));
    }

    @GetMapping("/object/{string}")
    public ResponseEntity<List<imageProperties>> getbyobject(@RequestParam("objects") List<String> string){
        try {
            List<imageProperties> ImageProperties = ImageService.getImageByObject(string);
            return new ResponseEntity<List<imageProperties>>(ImageProperties, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<List<imageProperties>>(HttpStatus.NOT_FOUND);
        }
    }
}
