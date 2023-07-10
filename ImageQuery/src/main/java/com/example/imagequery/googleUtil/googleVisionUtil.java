package com.example.imagequery.googleUtil;
import com.example.imagequery.image.imageProperties;
import com.google.cloud.vision.v1.*;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.protobuf.ByteString;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class googleVisionUtil {
    private Integer Id;
    private byte[] img;
    List<AnnotateImageRequest> requests = new ArrayList<>();

    public googleVisionUtil(Integer id, byte[] image){
        this.Id = id;
        this.img = image;
        publishToSql(objectDet(image));
    }
    public List<String> objectDet(byte[] image){
        List<String> objectsDetected = new ArrayList<>();
        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {
            ByteString imgBytes = ByteString.copyFrom(image);
            Image img = Image.newBuilder().setContent(imgBytes).build();

            Feature feat = Feature.newBuilder().setType(Type.LABEL_DETECTION).build();
            AnnotateImageRequest request =
                    AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
            requests.add(request);

            // Performs label detection on the image file
            BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                for (LocalizedObjectAnnotation entity : res.getLocalizedObjectAnnotationsList()) {
                    objectsDetected.add(entity.getName());
                }
            }
            return objectsDetected;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void publishToSql(List<String> objectsDetected){
        //publish objectsdetected+id to sql
        try
        {
            // create a mysql database connection
            String myDriver = "com.mysql.cj.jdbc.Driver";
            String myUrl = "jdbc:mysql://localhost:3306/DB?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
            Class.forName(myDriver);
            Connection conn = DriverManager.getConnection(myUrl, "root", "");

            for (String objectDetected: objectsDetected) {
                // the mysql insert statement
                String query = " insert into objectsDetected (id, objectDetected)"
                        + " values (?, ?)";

                // create the mysql insert preparedstatement
                PreparedStatement preparedStmt = conn.prepareStatement(query);
                preparedStmt.setString(1, this.Id.toString());
                preparedStmt.setString(2, objectDetected);
                // execute the preparedstatement
                preparedStmt.execute();
            }
            conn.close();
        }
        catch (Exception e)
        {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }
    }
    public List<imageProperties> querySql(List<String> objectsLookingFor){
        List<imageProperties> returnList = new ArrayList<>();
        try
        {
            // create a mysql database connection
            String myDriver = "com.mysql.cj.jdbc.Driver";
            String myUrl = "jdbc:mysql://localhost:3306/DB?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
            Class.forName(myDriver);
            Connection conn = DriverManager.getConnection(myUrl, "root", "");

            for (String objectDetected: objectsLookingFor) {
                // the mysql insert statement
                String query = " SELECT * from objectsDetected where objectsDetected = ?";

                // create the mysql insert preparedstatement
                PreparedStatement preparedStmtObjectsDetected = conn.prepareStatement(query);
                preparedStmtObjectsDetected.setString(1, objectDetected);
                // execute the preparedstatement
                ResultSet rs = preparedStmtObjectsDetected.executeQuery();
                while(rs.next()){
                    Integer id = rs.getInt("Id");
                    String objQuery = "SELECT * FROM image_properties where id = ?";
                    PreparedStatement preparedStmtImageProperties = conn.prepareStatement(objQuery);
                    preparedStmtImageProperties.setInt(1,id);
                    ResultSet resultSet = preparedStmtImageProperties.executeQuery();
                    while(resultSet.next()){
                        returnList.add(new imageProperties(rs.getInt("id"),rs.getBytes("image")));
                    }
                }
            }
            conn.close();
        }
        catch (Exception e)
        {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }
        return returnList;
    }


}
