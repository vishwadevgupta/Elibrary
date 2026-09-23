package com.javatpoint.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.javatpoint.beans.BookBean;
import com.javatpoint.beans.IssueBookBean;

public class BookDao {
    public static int save(BookBean b){
        if(b==null||blank(b.getCallno())||blank(b.getName())||blank(b.getAuthor())||blank(b.getPublisher())||b.getQuantity()<=0)return 0;
        String sql="insert into e_book(callno,name,author,publisher,quantity,issued) values(?,?,?,?,?,0)";
        try(Connection c=DB.getCon();PreparedStatement p=c.prepareStatement(sql)){p.setString(1,b.getCallno().trim());p.setString(2,b.getName().trim());p.setString(3,b.getAuthor().trim());p.setString(4,b.getPublisher().trim());p.setInt(5,b.getQuantity());return p.executeUpdate();}catch(Exception e){System.err.println("Book save failed: "+e.getMessage());return 0;}
    }
    public static List<BookBean> view(){List<BookBean> list=new ArrayList<BookBean>();String sql="select callno,name,author,publisher,quantity,issued from e_book order by name";try(Connection c=DB.getCon();PreparedStatement p=c.prepareStatement(sql);ResultSet r=p.executeQuery()){while(r.next()){BookBean b=new BookBean();b.setCallno(r.getString("callno"));b.setName(r.getString("name"));b.setAuthor(r.getString("author"));b.setPublisher(r.getString("publisher"));b.setQuantity(r.getInt("quantity"));b.setIssued(r.getInt("issued"));list.add(b);}}catch(Exception e){System.err.println("Book view failed: "+e.getMessage());}return list;}
    public static int delete(String callno){if(blank(callno))return 0;try(Connection c=DB.getCon();PreparedStatement p=c.prepareStatement("delete from e_book where callno=? and issued=0")){p.setString(1,callno.trim());return p.executeUpdate();}catch(Exception e){System.err.println("Book delete failed: "+e.getMessage());return 0;}}
    public static int getIssued(String callno){try(Connection c=DB.getCon();PreparedStatement p=c.prepareStatement("select issued from e_book where callno=?")){p.setString(1,callno);try(ResultSet r=p.executeQuery()){return r.next()?r.getInt(1):0;}}catch(Exception e){return 0;}}
    public static boolean checkIssue(String callno){try(Connection c=DB.getCon();PreparedStatement p=c.prepareStatement("select 1 from e_book where callno=? and issued<quantity")){p.setString(1,callno);try(ResultSet r=p.executeQuery()){return r.next();}}catch(Exception e){return false;}}
    public static int issueBook(IssueBookBean b){
        if(b==null||blank(b.getCallno())||blank(b.getStudentid())||blank(b.getStudentname())||b.getStudentmobile()<=0)return 0;
        String lock="select quantity,issued from e_book where callno=? for update";
        try(Connection c=DB.getCon()){
            c.setAutoCommit(false);
            int available=-1;
            try(PreparedStatement p=c.prepareStatement(lock)){p.setString(1,b.getCallno().trim());try(ResultSet r=p.executeQuery()){if(r.next())available=r.getInt("quantity")-r.getInt("issued");}}
            if(available<=0){c.rollback();return 0;}
            try(PreparedStatement p=c.prepareStatement("insert into e_issuebook(callno,studentid,studentname,studentmobile,issueddate,returnstatus) values(?,?,?,?,SYSDATE,'no')")){p.setString(1,b.getCallno().trim());p.setString(2,b.getStudentid().trim());p.setString(3,b.getStudentname().trim());p.setLong(4,b.getStudentmobile());p.executeUpdate();}
            try(PreparedStatement p=c.prepareStatement("update e_book set issued=issued+1 where callno=?")){p.setString(1,b.getCallno().trim());p.executeUpdate();}
            c.commit();return 1;
        }catch(Exception e){System.err.println("Issue failed: "+e.getMessage());return 0;}
    }
    public static int returnBook(String callno,String studentid){
        if(blank(callno)||blank(studentid))return 0;
        try(Connection c=DB.getCon()){
            c.setAutoCommit(false);
            String sql="select rowid from e_issuebook where callno=? and studentid=? and returnstatus='no' for update";
            String row=null;try(PreparedStatement p=c.prepareStatement(sql)){p.setString(1,callno.trim());p.setString(2,studentid.trim());try(ResultSet r=p.executeQuery()){if(r.next())row=r.getString(1);}}
            if(row==null){c.rollback();return 0;}
            try(PreparedStatement p=c.prepareStatement("update e_issuebook set returnstatus='yes' where rowid=?")){p.setString(1,row);p.executeUpdate();}
            try(PreparedStatement p=c.prepareStatement("update e_book set issued=case when issued>0 then issued-1 else 0 end where callno=?")){p.setString(1,callno.trim());p.executeUpdate();}
            c.commit();return 1;
        }catch(Exception e){System.err.println("Return failed: "+e.getMessage());return 0;}
    }
    public static List<IssueBookBean> viewIssuedBooks(){List<IssueBookBean> list=new ArrayList<IssueBookBean>();try(Connection c=DB.getCon();PreparedStatement p=c.prepareStatement("select callno,studentid,studentname,studentmobile,issueddate,returnstatus from e_issuebook order by issueddate desc");ResultSet r=p.executeQuery()){while(r.next()){IssueBookBean b=new IssueBookBean();b.setCallno(r.getString("callno"));b.setStudentid(r.getString("studentid"));b.setStudentname(r.getString("studentname"));b.setStudentmobile(r.getLong("studentmobile"));b.setIssueddate(r.getDate("issueddate"));b.setReturnstatus(r.getString("returnstatus"));list.add(b);}}catch(Exception e){System.err.println("Issued books view failed: "+e.getMessage());}return list;}
    private static boolean blank(String s){return s==null||s.trim().isEmpty();}
}
