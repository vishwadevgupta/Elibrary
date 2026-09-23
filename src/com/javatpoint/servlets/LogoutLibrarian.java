package com.javatpoint.servlets;
import java.io.IOException;import javax.servlet.*;import javax.servlet.annotation.*;import javax.servlet.http.*;
@WebServlet("/LogoutLibrarian") public class LogoutAdmin extends HttpServlet{protected void doGet(HttpServletRequest q,HttpServletResponse r)throws ServletException,IOException{HttpSession s=q.getSession(false);if(s!=null)s.invalidate();r.sendRedirect("index.html");}}
