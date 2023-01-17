package com.example.instagram;

import com.example.instagram.models.Comment;
import com.example.instagram.models.FollowRequest;
import com.example.instagram.models.Post;
import com.example.instagram.models.User;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;

import java.io.*;
import java.nio.file.OpenOption;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

public class SqlManager {
    private final String url = "jdbc:mysql://localhost/instagram";
    private final String username = "root";
    private final String password = "";
    private static SqlManager sqlManager = null;

    private SqlManager() {

    }

    public static SqlManager getInstance() {
        if (sqlManager == null) {
            return new SqlManager();
        }
        return sqlManager;
    }

    public Connection connectDb()  {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, username, password);
            return con;
        } catch (Exception e) {
            System.out.println("Unable to connect to database");
            return null;
        }
    }

    public boolean addUser(User user) {
        Connection con = connectDb();
        try {
            String sqlCmd = String.format("INSERT INTO `users` (username, password, fullName, mobileNumber, pageState) " +
                            "VALUES ('%s', '%s', '%s', '%s', '%s');"
                    , user.getUsername(), user.getPassword(), user.getFullName(), user.getMobileNumber(), user.getPageState());
            Statement s = con.prepareStatement(sqlCmd);
            s.execute(sqlCmd);
            con.close();
            System.out.println("user '" + user.getUsername() + "' successfully ADDED to the database.");
            return true;
        } catch (Exception ex) {
            System.out.println("couldn't add user '" + user.getUsername() + "' to the database.");
            return false;
        }
    }

    public boolean deleteUser(String username) {
        Connection con = connectDb();
        try {
            String sqlCmd = String.format("DELETE FROM `users` WHERE `username`='%s';", username);
            Statement s = con.prepareStatement(sqlCmd);
            s.execute(sqlCmd);
            con.close();
            System.out.println("user '" + username + "' successfully DELETED to the database.");
            return true;
        } catch (Exception ex) {
            System.out.println("couldn't delete user '" + username + "' from the database.");
            return false;
        }
    }

//    private boolean changeUserInfo(User user) {
//        Connection con = connectDb();
//        try {
//                String sqlCmd = String.format("UPDATE `users` SET `username`='%s' `fullName`='%s' `password`='%s' `mobileNumber`='%s' WHERE `username`='%s';"
//                    , user.getUsername(), user.getPassword(), user.getFullName(), user.getMobileNumber(), user.getUsername());
//            Statement s = con.prepareStatement(sqlCmd);
//            s.execute(sqlCmd);
//            con.close();
//            System.out.println("info of user '"+user.getUsername()+"' CHANGED successfully.");
//            return true;
//        } catch (Exception e) {
//            System.out.println("couldn't change info of user '"+user.getUsername()+"' from database.");
//            return false;
//        }
//    }


    public ArrayList<FollowRequest> getFollowRequestsFollowersOf(String username) {
        ArrayList<FollowRequest> list = new ArrayList<>();
        Connection con = connectDb();
        try {
            PreparedStatement ps = con.prepareStatement(String.format("SELECT * FROM `follow_request` WHERE `followingUsername`='%s'", username));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new FollowRequest(
                    findUser(rs.getString("followerUsername")),
                    findUser(rs.getString("followingUsername")),
                    rs.getString("time"))
                );
            }
        } catch (Exception e) {
            System.out.println("trying to load follow requests failed");
            System.out.println(e.getMessage());
        }
        return list;
    }


    public boolean updateUserInfo(User user, String bio, String absolutePath, String date) {
        Connection con = connectDb();
        try {
            String sqlCmd = String.format("UPDATE users SET username='%s', password='%s', fullName='%s', mobileNumber='%s', pageState='%s' WHERE username='%s'"
                    , user.getUsername(), user.getPassword(), user.getFullName(), user.getMobileNumber(), user.getPageState(), user.getUsername());
            PreparedStatement pstmt = con.prepareStatement(sqlCmd);
            pstmt.execute(sqlCmd);

            String sqlCmd1 = String.format("DELETE FROM user_info WHERE username='%s'", user.getUsername());
            PreparedStatement pstmt1 = con.prepareStatement(sqlCmd1);
            pstmt1.execute(sqlCmd1);

            String sqlCmd2 = String.format("INSERT INTO user_info (username, bio, image, birthday) VALUES ('%s', '%s', '%s', '%s')"
                    , user.getUsername(), bio, absolutePath, date);

            PreparedStatement pstmt2 = con.prepareStatement(sqlCmd2);
//            pstmt2.setString(user.getUsername(), "usernmae");
//            pstmt2.setString(2, bio);
////            File file = new File(absolutePath);
////            InputStream in = new FileInputStream(file);
////            pstmt2.setBinaryStream(3, in, (int)file.length());
//            pstmt2.setString(3, absolutePath);
//            pstmt2.setString(4, date);
//            pstmt2.setString(5, pageState);
            pstmt2.execute();
            con.close();
            System.out.println("info of user '"+user.getUsername()+"' CHANGED successfully.");
            return true;
        } catch (Exception e) {
            System.out.println("couldn't change info of user '"+user.getUsername()+"' from database.");
            System.out.println(e.getMessage());
            return false;
        }
    }

    public User findUser(String username) {
        Connection con = connectDb();
        try {
            String sqlCmd = String.format("SELECT * FROM `users` WHERE `username`='%s';"
                    , username);
            Statement s = con.prepareStatement(sqlCmd);
            ResultSet rs = s.executeQuery(sqlCmd);

            User user = new User();
            while (rs.next()) {
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setFullName((rs.getString("fullName")));
                user.setMobileNumber(rs.getString("mobileNumber"));
                user.setPageState(rs.getString("pageState"));
            }
            // check if this user did not exist before, we don't go for more information
            if (user.getUsername()==null) {
                return new User();
            }
            // secondary info setup
            sqlCmd = String.format("SELECT * FROM user_info WHERE `username`='%s'", user.getUsername());
            Statement ps = con.createStatement();
            rs = ps.executeQuery(sqlCmd);
//            int i = 0;
            while(rs.next()) {
                user.setBio(rs.getString("bio"));
                user.setBirthday(rs.getString("birthday"));
                user.setProfImagePath(rs.getString("image"));
                user.setBirthday(rs.getString("birthday"));
//                InputStream input = rs.getBinaryStream("image");
//                String name = "system_files/system_images/"+user.getUsername()+"_"+i+".jpg";
//                new File(name).mkdirs();
//                OutputStream output = new FileOutputStream(name);
//                i++;
//                int b = 0;
//                while((b = input.read()) > -1) {
//                    output.write(b);
//                }
//                user.setProfImage(new Image(new FileInputStream(name)));
//                output.close();
//                input.close();
            }
            con.close();
            System.out.println("Info of User '"+user.getUsername()+"' successfully retrieved from database.");
            return user;
        } catch (Exception ex) {
            System.out.println("Couldn't load info of User '"+username+"' from database.");
            System.out.println(ex.getMessage());
            return new User();
        }
    }

    public Post findPost(String time, String username) {
        Connection con = connectDb();
        Post post = new Post();
        try {
            String sqlCmd = String.format("SELECT * FROM posts WHERE time='%s' AND username='%s'", time, username);
            PreparedStatement ps = con.prepareStatement(sqlCmd);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                post.setUser(findUser(rs.getString("username")));
                post.setContent(rs.getString("content"));
                post.setTime(rs.getString("time"));
                post.setImagePath(rs.getString("image"));
            }
        } catch (Exception e) {
            System.out.println("Couldn't find any post at time '" + time + "' from user '" + username+ "'.");
            System.out.println(e.getMessage());
        }
        return post;
    }

    public boolean addPost(Post post) {
        Connection con = connectDb();
        try {
            String sqlCmd = String.format("INSERT INTO `posts` (`username`, `content`, `image`, time) VALUES('%s', '%s', '%s', '%s')"
                    , post.getUser().getUsername(), post.getContent(), post.getImagePath(), currentTime());
            PreparedStatement s = con.prepareStatement(sqlCmd);
//            s.setString(1, post.getUser().getUsername());
//            s.setString(2, post.getContent());
//            s.setString(3, Integer.toString(post.getScore()));
//            s.setString(4, post.getImagePath());
            s.execute(sqlCmd);
            con.close();
            System.out.println("post of '" + post.getUser().getUsername() + "' successfully ADDED to the database.");
            return true;
        } catch (Exception ex) {
            System.out.println("couldn't add Post of '" + post.getUser().getUsername() + "' to the database.");
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public boolean editPost(Post post) {
        Connection con = connectDb();
        try {
            String sqlCmd = String.format("UPDATE posts SET content='%s', image='%s' WHERE username='%s' AND time='%s'"
                    , post.getContent(), post.getImagePath(), post.getUser().getUsername(), post.getTime());
            PreparedStatement pstmt = con.prepareStatement(sqlCmd);
            pstmt.execute(sqlCmd);
            Statement s = con.prepareStatement(sqlCmd);
            s.execute(sqlCmd);
            con.close();
            System.out.println("post of '" + post.getUser().getUsername() + "' successfully EDITED to the database.");
            return true;
        } catch (Exception ex) {
            System.out.println("couldn't edit Post of '" + post.getUser().getUsername() + "' from the database.");
            return false;
        }
        //notify all followers
    }

    public ArrayList<Post> findPosts(String username) {
        ArrayList<Post> list = new ArrayList<>();
        Connection con = connectDb();
        try {
            PreparedStatement ps = con.prepareStatement(String.format("SELECT * FROM posts WHERE username='%s'", username));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Post p = new Post(
                        rs.getString("content"),
                        rs.getString("image"),
                        rs.getString("time")
                );
                p.setUser(findUser(username));
                list.add(p);
            }
        } catch (Exception e) {
            System.out.println("trying to load follow requests failed");
            System.out.println(e.getMessage());
        }
        return list;
    }

    public boolean addFollowing(String username, String username1) {
        Connection con = connectDb();
        try {
            String sqlCmd1 = String.format("DELETE FROM follows WHERE followerUsername='%s' AND followingUsername='%s'",
                    username, username1);
            Statement s1 = con.prepareStatement(sqlCmd1);
            s1.execute(sqlCmd1);

            String sqlCmd = String.format("INSERT INTO `follows` (followerUsername, followingUsername) " +
                            "VALUES ('%s', '%s');"
                    , username, username1);
            Statement s = con.prepareStatement(sqlCmd);
            s.execute(sqlCmd);

            con.close();
            System.out.println("user '" + username + "' follows user '"+username1+"'.");
            return true;
        } catch (Exception ex) {
            System.out.println("user '" + username + "' couldn't follow user '"+username1+"'.");
            System.out.println(ex.getMessage());
            return false;
        }
    }


    public ArrayList<String> getFollowers(String username) {
        ArrayList<String> list = new ArrayList<>();
        Connection con = connectDb();
        try {
            PreparedStatement ps = con.prepareStatement(String.format("SELECT * FROM follows WHERE followingUsername='%s'", username));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("followerUsername"));
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    public ArrayList<String> getFollowings(String username) {
        ArrayList<String> list = new ArrayList<>();
        Connection con = connectDb();
        try {
            PreparedStatement ps = con.prepareStatement(String.format("SELECT * FROM follows WHERE followerUsername='%s'", username));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("followingUsername"));
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    public boolean deleteFollowing(String username, String username1) {
        Connection con = connectDb();
        try {
            String sqlCmd1 = String.format("DELETE FROM follows WHERE followerUsername='%s' AND followingUsername='%s'",
                    username, username1);
            Statement s1 = con.prepareStatement(sqlCmd1);
            s1.execute(sqlCmd1);


            con.close();
            System.out.println("user '" + username + "' follows user '"+username1+"'.");
            return true;
        } catch (Exception ex) {
            System.out.println("user '" + username + "' couldn't follow user '"+username1+"'.");
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public boolean addRequest(String username, String username1) {
        Connection con = connectDb();
        try {
            String sqlCmd1 = String.format("DELETE FROM `follow_request` WHERE followerUsername='%s' AND followingUsername='%s'",
                    username, username1);
            Statement s1 = con.prepareStatement(sqlCmd1);
            s1.execute(sqlCmd1);

            String sqlCmd = String.format("INSERT INTO `follow_request` (followerUsername, followingUsername, time) VALUES ('%s', '%s', '%s')"
                    , username, username1, currentTime());
            Statement s = con.prepareStatement(sqlCmd);
            s.execute(sqlCmd);
            con.close();
            System.out.println("request for user '" + username + "' to follow user '"+username1+"' successfully set.");
            return true;
        } catch (Exception ex) {
            System.out.println("setting request for user '" + username + "' to follow user '"+username1+"' failed.");
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public boolean deleteRequest(String username, String username1) {
        Connection con = connectDb();
        try {
            String sqlCmd1 = String.format("DELETE FROM `follow_request` WHERE followerUsername='%s' AND followingUsername='%s'",
                    username, username1);
            Statement s1 = con.prepareStatement(sqlCmd1);
            s1.execute(sqlCmd1);
            con.close();
            return true;
        } catch (Exception e) {
            System.out.println("Unable to delete request");
            System.out.println(e.getMessage());
            return false;
        }
    }

    private static String currentTime() {
        long millis = System.currentTimeMillis();
        java.util.Date date = new java.util.Date(millis);
        return date.toString();
    }

    public boolean acceptRequest(String username, String username1) {
        Connection con = connectDb();
        try {
            addFollowing(username, username1);
            deleteRequest(username, username1);
            System.out.println("request of user '"+ username + "accepted form user '"+ username1);
            return true;
        } catch (Exception e) {
            System.out.println("Couldn't accept request of user '"+username+"' from user '" +username1);
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean rejectRequest(String username, String username1) {
        try {
            deleteRequest(username, username1);
            System.out.println("request of user '"+ username + "rejected form user '"+ username1);
            return true;
        } catch (Exception e) {
            System.out.println("Couldn't reject request of user '"+username+"' from user '" +username1);
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean addComment(User user, String content, Post post) {
        Connection con = connectDb();
        try {
            String sqlCmd = String.format("INSERT INTO comment (time, content, username, postTime, postUsername) VALUES ('%s', '%s', '%s', '%s', '%s')",
                    currentTime(), content.replace("'", "\'"), user.getUsername(), post.getTime(), post.getUser().getUsername());
            PreparedStatement ps = con.prepareStatement(sqlCmd);
            ps.execute(sqlCmd);
            con.close();
            return true;
        } catch (Exception e) {
            System.out.println("Unable to add this comment");
            System.out.println(e.getMessage());
            return false;
        }
    }

    public ArrayList<Comment> getComments(Post post) {
        Connection con = connectDb();
        ArrayList<Comment> comments = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(String.format("SELECT * FROM comment WHERE postTime='%s' AND postUsername='%s'",
                    post.getTime(), post.getUser().getUsername()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                comments.add(new Comment(
                    findUser(rs.getString("username")),
                    rs.getString("content"),
                    findPost(rs.getString("postTime"), post.getUser().getUsername()),
                    rs.getString("time")
                ));
            }
            con.close();
        } catch (Exception e) {
            System.out.println("Couldn't load comments of this post");
            System.out.println(e.getMessage());
        }
        return comments;
    }

    public boolean addOpinion(Opinion opinion) {
        Connection con = connectDb();
        try {
            deleteOpinion(opinion);
            String sqlCmd = String.format("INSERT INTO opinions (username, postTime, postUsername, state) VALUES ('%s', '%s', '%s', '%s')",
                    opinion.getUser().getUsername(), opinion.getPost().getTime(), opinion.getPost().getUser().getUsername(), opinion.getState());
            PreparedStatement ps = con.prepareStatement(sqlCmd);
            ps.execute(sqlCmd);
            con.close();
            System.out.println("User '"+opinion.getUser().getUsername()+"' liked one post of user '" + opinion.getPost().getUser().getUsername()+"' .");
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private ArrayList<Opinion> getOpinions(Post post) {
        Connection con = connectDb();
        ArrayList<Opinion> opinions = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(String.format("SELECT * FROM opinions WHERE postTime='%s' AND postUsername='%s'",
                    post.getTime(), post.getUser().getUsername()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                opinions.add(new Opinion(
                        rs.getString("state"),
                        findUser(rs.getString("username")),
                        findPost(rs.getString("postTime"), post.getUser().getUsername())
                ));
            }
            con.close();
        } catch (Exception e) {
            System.out.println("Couldn't load opinions of this post");
            System.out.println(e.getMessage());
        }
        return opinions;
    }

    public ArrayList<Opinion> getLikes(Post post) {
        ArrayList<Opinion> likes = new ArrayList<>();
        for (Opinion o : getOpinions(post)) {
            if (o.getState().equals("like")) {
                likes.add(o);
            }
        }
        return likes;
    }

    public ArrayList<Opinion> getDislikes(Post post) {
        ArrayList<Opinion> dislikes = new ArrayList<>();
        for (Opinion o : getOpinions(post)) {
            if (o.getState().equals("dislike")) {
                dislikes.add(o);
            }
        }
        return dislikes;
    }

    public boolean deleteOpinion(Opinion opinion) {
        Connection con = connectDb();
        try {
            String sqlCmd1 = String.format("DELETE FROM opinions WHERE username='%s' AND postTime='%s'",
                    opinion.getUser().getUsername(), opinion.getPost().getTime());
            Statement s1 = con.prepareStatement(sqlCmd1);
            s1.execute(sqlCmd1);
            con.close();
            return true;
        } catch (Exception e) {
            System.out.println("Unable to delete Opinion of user '"+opinion.getUser().getUsername()+"' from post.");
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean deleteComment(Comment comment) {
        Connection con = connectDb();
        try {
            String sqlCmd1 = String.format("DELETE FROM comment WHERE username='%s' AND time='%s'",
                    comment.getUser().getUsername(), comment.getTime());
            Statement s1 = con.prepareStatement(sqlCmd1);
            s1.execute(sqlCmd1);
            con.close();
            return true;
        } catch (Exception e) {
            System.out.println("Unable to delete Comment of user '"+comment.getUser().getUsername()+"' from post.");
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean addMessage(Message message) {
        Connection con = connectDb();
        try {
            String sqlCmd = String.format("INSERT INTO messages (fromUsername, toUsername, message) VALUES ('%s','%s','%s')",
                    message.getFrom().getUsername(), message.getTo().getUsername(), message.getMessage());
            PreparedStatement ps = con.prepareStatement(sqlCmd);
            ps.execute(sqlCmd);
            con.close();
            return true;
        } catch (Exception e) {
            System.out.println("Coudn't set message from user '"+message.getFrom().getUsername()
                    + "' to user '" + message.getTo().getUsername()+"' .");
            System.out.println(e.getMessage());
            return false;
        }

    }

    public ArrayList<Message> getMessages() {
        Connection con = connectDb();
        ArrayList<Message> messages = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM messages");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                messages.add(new Message(
                        findUser(rs.getString("fromUsername")),
                        findUser(rs.getString("toUsername")),
                        rs.getString("message")
                ));
            }
            con.close();
        } catch (Exception e) {
            System.out.println("Couldn't load messages");
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public ArrayList<String> getAllUsernames() {
        Connection con = connectDb();
        ArrayList<String> users = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM users");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                users.add(rs.getString("username"));
            }
            con.close();
        } catch (Exception e) {
            System.out.println("Couldn't get all usernames");
            System.out.println(e.getMessage());
        }
        return users;
    }
}

