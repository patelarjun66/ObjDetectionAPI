package com.example.imagequery.image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.imagequery.googleUtil.googleVisionUtil;

import java.util.List;
@Service
@Transactional
public class imageService {
    @Autowired
    private imageRepository ImageRepository;
    private googleVisionUtil GoogleVisionUtil;
    public List<imageProperties> listAllImages(){
        return ImageRepository.findAll();
    }

    public void saveImage(imageProperties Image){
        ImageRepository.save(Image);
    }

    public imageProperties getImage(Integer id){
        return ImageRepository.findById(id).get();
    }

    public List<imageProperties> getImageByObject(List<String> object){
        return GoogleVisionUtil.querySql(object);
    }

}
