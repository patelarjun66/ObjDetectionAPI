# ObjDetectionAPI
Image Object Detection and storage API for HEB assessment.


# DB Creation and instantiation:

1. Create the MySQL database instance.
Download MySQL:
[Install MySQL](https://www.mysql.com/downloads/)https://www.mysql.com/downloads/

2. Run the following command:

```
mysql.server start
```
3. Upon creation connect to the MySQL and run the following queries (I personally used MySQL workbench):

```
CREATE DATABASE db;
create table db.image_properties(
id int NOT NULL AUTO_INCREMENT primary key,
image LONGBLOB NOT NULL);

create table db.objectsDetected(
id int not null,
objectDetected varchar(255) NOT NULL);
```
These sql statement create the database and both tables needed to store the image data and the objects detected using the vision algorithm.

# Cloning and running the app:

1. After cloning the repo, CD into the project directory
2. Run mvn spring-boot:run

Once running you should be able to connect to the API using a REST client.

# Hitting the endpoints:

**GET /imageProperties** 

Returns all image metadata and corresponding ID

**POST /imageProperties**

Example request params:
![image](https://github.com/patelarjun66/imageObjectDetectionAPI/assets/25109672/2d393fdd-ab27-45c0-9787-994dee543f64)
Uploads image and denotes flag to determine if object detection should be run


**GET /imageProperties/{id}**

Returns all image metadata for specified image

**GET /imageProperties?objects="dog,cat"**

Returns all image metadata that contains specified objects


