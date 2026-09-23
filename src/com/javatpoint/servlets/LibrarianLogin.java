package com.javatpoint.servlets;
import java.io.IOException;import javax.servlet.ServletException;import javax.servlet.annotation.WebServlet;import javax.servlet.http.*;import com.javatpoint.dao.LibrarianDao;
@WebServlet("/LibrarianLogin") public class LibrarianLogin extends HttpServlet{
 protected void doPost(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException{
  String email=req.getParameter("email"),password=req.getParameter("password");
  if(LibrarianDao.authenticate(email,password)){HttpSession old=req.getSession(false);if(old!=null)old.invalidate();HttpSession s=req.getSession(true);s.setAttribute("role","librarian");s.setAttribute("email",email.trim().toLowerCase());s.setMaxInactiveInterval(30*60);res.sendRedirect("ViewBook");}
  else{res.setContentType("text/html;charset=UTF-8");res.getWriter().print("<link rel='stylesheet' href='app.css'><div class='page'><div class='card form-card'><h2>Sign in failed</h2><p class='muted'>The credentials were not accepted.</p><a class='btn btn-primary' href='index.html'>Try again</a></div></div>");}
 }
}