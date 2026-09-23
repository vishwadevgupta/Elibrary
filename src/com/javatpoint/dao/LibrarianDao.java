package com.javatpoint.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.javatpoint.beans.LibrarianBean;
import com.javatpoint.util.SecurityUtil;

public class LibrarianDao {
    public static int save(LibrarianBean bean) {
        if (bean == null || blank(bean.getName()) || blank(bean.getEmail()) || blank(bean.getPassword()) || bean.getMobile() <= 0) return 0;
        String sql="insert into e_librarian(id,name,email,password,mobile) values(e_librarian_seq.nextval,?,?,?,?)";
        try(Connection con=DB.getCon(); PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setString(1, bean.getName().trim()); ps.setString(2, bean.getEmail().trim().toLowerCase()); ps.setString(3, SecurityUtil.hashPassword(bean.getPassword())); ps.setLong(4, bean.getMobile()); return ps.executeUpdate();
        } catch(Exception e) { System.err.println("Librarian save failed: "+e.getMessage()); return 0; }
    }
    public static int update(LibrarianBean bean) {
        if (bean == null || bean.getId() <= 0 || blank(bean.getName()) || blank(bean.getEmail()) || bean.getMobile() <= 0) return 0;
        boolean changePassword=!blank(bean.getPassword());
        String sql=changePassword?"update e_librarian set name=?,email=?,password=?,mobile=? where id=?":"update e_librarian set name=?,email=?,mobile=? where id=?";
        try(Connection con=DB.getCon(); PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setString(1,bean.getName().trim()); ps.setString(2,bean.getEmail().trim().toLowerCase());
            if(changePassword){ps.setString(3,SecurityUtil.hashPassword(bean.getPassword())); ps.setLong(4,bean.getMobile()); ps.setInt(5,bean.getId());}
            else {ps.setLong(3,bean.getMobile()); ps.setInt(4,bean.getId());}
            return ps.executeUpdate();
        } catch(Exception e){System.err.println("Librarian update failed: "+e.getMessage()); return 0;}
    }
    public static List<LibrarianBean> view() {
        List<LibrarianBean> list=new ArrayList<LibrarianBean>();
        String sql="select id,name,email,mobile from e_librarian order by id";
        try(Connection con=DB.getCon(); PreparedStatement ps=con.prepareStatement(sql); ResultSet rs=ps.executeQuery()) {
            while(rs.next()){LibrarianBean b=new LibrarianBean(); b.setId(rs.getInt("id")); b.setName(rs.getString("name")); b.setEmail(rs.getString("email")); b.setMobile(rs.getLong("mobile")); list.add(b);}
        } catch(Exception e){System.err.println("Librarian view failed: "+e.getMessage());}
        return list;
    }
    public static LibrarianBean viewById(int id) {
        LibrarianBean b=new LibrarianBean();
        String sql="select id,name,email,mobile from e_librarian where id=?";
        try(Connection con=DB.getCon(); PreparedStatement ps=con.prepareStatement(sql)){ps.setInt(1,id); try(ResultSet rs=ps.executeQuery()){if(rs.next()){b.setId(rs.getInt("id"));b.setName(rs.getString("name"));b.setEmail(rs.getString("email"));b.setMobile(rs.getLong("mobile"));}}} catch(Exception e){System.err.println("Librarian lookup failed: "+e.getMessage());}
        return b;
    }
    public static int delete(int id){
        if(id<=0)return 0; try(Connection con=DB.getCon();PreparedStatement ps=con.prepareStatement("delete from e_librarian where id=?")){ps.setInt(1,id);return ps.executeUpdate();}catch(Exception e){System.err.println("Librarian delete failed: "+e.getMessage());return 0;}
    }
    public static boolean authenticate(String email,String password){
        if(blank(email)||blank(password))return false;
        String sql="select password from e_librarian where lower(email)=lower(?)";
        try(Connection con=DB.getCon();PreparedStatement ps=con.prepareStatement(sql)){ps.setString(1,email.trim());try(ResultSet rs=ps.executeQuery()){if(!rs.next())return false;String stored=rs.getString(1);if(SecurityUtil.verifyPassword(password,stored))return true;if(SecurityUtil.isLegacyPlaintext(stored)&&stored.equals(password)){try(PreparedStatement up=con.prepareStatement("update e_librarian set password=? where lower(email)=lower(?)")){up.setString(1,SecurityUtil.hashPassword(password));up.setString(2,email.trim());up.executeUpdate();}return true;}return false;}}catch(Exception e){System.err.println("Authentication failed: "+e.getMessage());return false;}
    }
    private static boolean blank(String s){return s==null||s.trim().isEmpty();}
}
