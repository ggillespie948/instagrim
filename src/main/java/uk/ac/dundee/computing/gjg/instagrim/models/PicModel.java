package uk.ac.dundee.computing.gjg.instagrim.models;

/*
 * Expects a cassandra columnfamily defined as
 * use keyspace2;
 CREATE TABLE Tweets (
 user varchar,
 interaction_time timeuuid,
 tweet varchar,
 PRIMARY KEY (user,interaction_time)
 ) WITH CLUSTERING ORDER BY (interaction_time DESC);
 * To manually generate a UUID use:
 * http://www.famkruithof.net/uuid/uuidgen
 */
import uk.ac.dundee.computing.gjg.instagrim.lib.Convertors;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.Bytes;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import static org.imgscalr.Scalr.*;
import org.imgscalr.Scalr.Method;

import uk.ac.dundee.computing.gjg.instagrim.stores.Pic;
//import uk.ac.dundee.computing.aec.stores.TweetStore;

public class PicModel {

    Cluster cluster;

    public void PicModel() {

    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    //add picture to database
    public void insertPic(byte[] b, String type, String name, String user, String title) {
        try {
            Convertors convertor = new Convertors();

            String types[] = Convertors.SplitFiletype(type);
            ByteBuffer buffer = ByteBuffer.wrap(b);
            int length = b.length;
            java.util.UUID picid = convertor.getTimeUUID();

            //The following is a quick and dirty way of doing this, will fill the disk quickly !
            Boolean success = (new File("/var/tmp/instagrim/")).mkdirs();
            FileOutputStream output = new FileOutputStream(new File("/var/tmp/instagrim/" + picid));

            output.write(b);
            byte[] thumbb = picresize(picid.toString(), types[1]);
            int thumblength = thumbb.length;
            ByteBuffer thumbbuf = ByteBuffer.wrap(thumbb);
            
            byte[] processedb = picdecolour(picid.toString(), types[1]);
            ByteBuffer processedbuf = ByteBuffer.wrap(processedb);
            int processedlength = processedb.length;

            Session session = cluster.connect("instagrim");

            PreparedStatement psInsertPic = session.prepare("insert into pics ( picid, image,thumb,processed, user, interaction_time,imagelength,thumblength,processedlength,type,name,title) values(?,?,?,?,?,?,?,?,?,?,?,?)");
            PreparedStatement psInsertPicToUser = session.prepare("insert into userpiclist ( picid, user, pic_added) values(?,?,?)");
            BoundStatement bsInsertPic = new BoundStatement(psInsertPic);
            BoundStatement bsInsertPicToUser = new BoundStatement(psInsertPicToUser);

            Date DateAdded = new Date();
            session.execute(bsInsertPic.bind(picid, buffer, thumbbuf, processedbuf, user, DateAdded, length, thumblength, processedlength, type, name, title));
            session.execute(bsInsertPicToUser.bind(picid, user, DateAdded));
            session.close();

        } catch (Exception e) {
            System.out.println("Error --> " + e);
        }
    }

    public boolean setComment(String comment, java.util.UUID picuuid) {
        Session session = cluster.connect("instagrim");

        comment = "'"+comment+"'";
        
        String picid = picuuid.toString();
        

        String source = "update pics set piccomments = piccomments + [comment] where picid =";
        String update = source.substring(0,45) + comment+ source.substring(52,67) +  picid+ ";";

        session.execute(update);
        return true;

    }

    public byte[] picresize(String picid, String type) {
        try {
            BufferedImage BI = ImageIO.read(new File("/var/tmp/instagrim/" + picid));
            BufferedImage thumbnail = createThumbnail(BI);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(thumbnail, type, baos);
            baos.flush();

            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (IOException et) {

        }
        return null;
    }

    public byte[] picdecolour(String picid, String type) {
        try {
            BufferedImage BI = ImageIO.read(new File("/var/tmp/instagrim/" + picid));
            BufferedImage processed = createProcessed(BI);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(processed, type, baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (IOException et) {

        }
        return null;
    }

    public static BufferedImage createThumbnail(BufferedImage img) {
        img = resize(img, Method.SPEED, 250, OP_ANTIALIAS, OP_GRAYSCALE);
        // Let's add a little border before we return result.
        return pad(img, 2);
    }

    public static BufferedImage createProcessed(BufferedImage img) {
        int Width = img.getWidth() - 1;
        img = resize(img, Method.SPEED, Width, OP_ANTIALIAS, OP_GRAYSCALE);
        return pad(img, 4);
    }

    public java.util.List<String> getComments(java.util.UUID picid) {
        Session session = cluster.connect("instagrim");

        PreparedStatement ps = session.prepare("select piccomments from pics where picid =?"); // select all comments
        ResultSet rs = null;
        BoundStatement bs = new BoundStatement(ps);
        rs = session.execute(
                bs.bind(
                        picid));

        if (rs.isExhausted()) {
            System.out.println("No comments found");
        } else {
            for (Row row : rs) { //go through every row in pic list and convert to string
                java.util.List<String> comments = row.getList("piccomments", String.class);

                //Add comment to Comments
                return comments;
            }
        }

        return null;
    }

    public java.util.LinkedList<Pic> getPicsForUser(String User) {
        java.util.LinkedList<Pic> Pics = new java.util.LinkedList<>();
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("select picid from userpiclist where user =?"); //select all pic id's
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        User));
        if (rs.isExhausted()) {
            System.out.println("No Images returned");
            return null;
        } else {
            for (Row row : rs) { //go through every row in pic list and convert to string
                Pic pic = new Pic();
                java.util.UUID UUID = row.getUUID("picid");
                System.out.println("UUID" + UUID.toString());
                pic.setUUID(UUID);
                Pics.add(pic);
                //add every picture id to item in linked list
            }
        }
        return Pics;
    }

    public java.util.LinkedList<Pic> getPicsForAll() {
        java.util.LinkedList<Pic> Pics = new java.util.LinkedList<>();
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("select picid from userpiclist where user =?"); //select all pic id's
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        "*"));
        if (rs.isExhausted()) {
            System.out.println("No Images returned");
            return null;
        } else {
            for (Row row : rs) { //go through every row in pic list and convert to string
                Pic pic = new Pic();
                java.util.UUID UUID = row.getUUID("picid");
                System.out.println("UUID" + UUID.toString());
                pic.setUUID(UUID);
                Pics.add(pic);
                //add every picture id to item in linked list
            }
        }
        return Pics;
    }

    public Pic getPic(int image_type, java.util.UUID picid) {
        Session session = cluster.connect("instagrim");
        ByteBuffer bImage = null;
        String type = null;
        int length = 0;
        try {
            Convertors convertor = new Convertors();
            ResultSet rs = null;
            PreparedStatement ps = null;

            if (image_type == Convertors.DISPLAY_IMAGE) {

                ps = session.prepare("select image,imagelength,type from pics where picid =?");
            } else if (image_type == Convertors.DISPLAY_THUMB) {
                ps = session.prepare("select thumb,imagelength,thumblength,type from pics where picid =?");
            } else if (image_type == Convertors.DISPLAY_PROCESSED) {
                ps = session.prepare("select processed,processedlength,type from pics where picid =?");
            }
            BoundStatement boundStatement = new BoundStatement(ps);
            rs = session.execute( // this is where the query is executed
                    boundStatement.bind( // here you are binding the 'boundStatement'
                            picid));

            if (rs.isExhausted()) {
                System.out.println("No Images returned");
                return null;
            } else {
                for (Row row : rs) {
                    if (image_type == Convertors.DISPLAY_IMAGE) {
                        bImage = row.getBytes("image");
                        length = row.getInt("imagelength");
                    } else if (image_type == Convertors.DISPLAY_THUMB) {
                        bImage = row.getBytes("thumb");
                        length = row.getInt("thumblength");

                    } else if (image_type == Convertors.DISPLAY_PROCESSED) {
                        bImage = row.getBytes("processed");
                        length = row.getInt("processedlength");
                    }

                    type = row.getString("type");

                }
            }
        } catch (Exception et) {
            System.out.println("Can't get Pic" + et);
            return null;
        }
        session.close();
        Pic p = new Pic();
        p.setPic(bImage, length, type);

        return p;

    }

    //Method which returns a given user's profile picture
    public java.util.UUID getProfilePicID(String user) {

        Session session = cluster.connect("instagrim");
        try {

            PreparedStatement ps = null;

            ps = session.prepare("select profile_pic_id from userprofiles where login = ?");

            BoundStatement bs = new BoundStatement(ps);
            ResultSet rs = null;

            rs = session.execute(
                    bs.bind(
                            user));
            if (rs.isExhausted()) {
                // NO PROFILE PICTURE UPLOADED
                return null;

            } else {
                // ********************** START HERE ***************************
                for (Row row : rs) { //convert result row to uuid
                    java.util.UUID UUID = row.getUUID("profile_pic_id");

                    return UUID;
                }

            }

        } catch (Exception e) {

        }

        java.util.UUID picid = null;

        return picid;
    }

    public String getPicTitle(java.util.UUID picid) {

        Session session = cluster.connect("instagrim");

        try {
            PreparedStatement ps = null;
            ps = session.prepare("select title from pics where picid =?");
            BoundStatement bs = new BoundStatement(ps);

            ResultSet rs = null;

            rs = session.execute(
                    bs.bind(
                            picid));

            for (Row row : rs) {//convert result row to uuid
                String title = row.toString();

                return title;
            }

        } catch (Exception e) {
            System.out.println("Exception: selecting title");
        }

        return null;
    }

    //Method which returns a given user's profile picture
    public boolean setProfilePicID(String user, String PicID) {

        Session session = cluster.connect("instagrim");

        String query = "update userprofiles set profile_pic_id='' where login=";
        String queryValues = query.substring(0, 39) + PicID + query.substring(41, 54) + user;
        
        try{
            session.execute(queryValues);
        
        } catch (Exception e){
            System.out.print("Error: could not update profile picture id");
            return false;                
        }
        
        
        return true;
    }

    //public String getPicTitle
}