package com.javatpoint.servlets;
import java.io.IOException;
import javax.servlet.ServletException;import javax.servlet.annotation.WebServlet;import javax.servlet.http.*;
@WebServlet("/AdminLogin") public class AdminLogin extends HttpServlet{
 protected void doPost(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException{
  String email=req.getParameter("email"),password=req.getParameter("password");String expectedEmail=System.getenv("ELIBRARY_ADMIN_EMAIL"),expectedPassword=System.getenv("ELIBRARY_ADMIN_PASSWORD");
  if(email!=null&&password!=null&&expectedEmail!=null&&expectedPassword!=null&&email.trim().equalsIgnoreCase(expectedEmail.trim())&&password.equals(expectedPassword)){
   HttpSession old=req.getSession(false);if(old!=null)old.invalidate();HttpSession s=req.getSession(true);s.setAttribute("role","admin");s.setMaxInactiveInterval(30*60);res.sendRedirect("ViewLibrarian");
  }else{res.setContentType("text/html;charset=UTF-8");res.getWriter().print("<link rel='stylesheet' href='app.css'><div class='page'><div class='card form-card'><h2>Sign in failed</h2><p class='muted'>The credentials were not accepted.</p><a class='btn btn-primary' href='index.html'>Try again</a></div></div>");}
 }
}