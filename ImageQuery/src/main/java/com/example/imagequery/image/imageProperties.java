package com.example.imagequery.image;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import com.example.imagequery.googleUtil.googleVisionUtil;


//Entity Model Class
@Entity
@Table(name = "imageProperties")
public class imageProperties {
    private int id;
    private byte[] image;

    public imageProperties(){
    }

    public imageProperties(int id, byte[] Image, boolean objectDetectionFlag){
        this.id = id;
        this.image = Image;
        if (objectDetectionFlag){
            new googleVisionUtil(id,Image);
        }
    }
    public imageProperties(int id, byte[] Image){
        this.id = id;
        this.image = Image;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] Image) {
        this.image = Image;
    }
}
