package com.javatpoint.servlets;
import java.io.IOException;import javax.servlet.*;import javax.servlet.annotation.*;import javax.servlet.http.*;import com.javatpoint.dao.BookDao;import com.javatpoint.util.AuthUtil;
@WebServlet("/DeleteBook") public class DeleteBook extends HttpServlet{protected void doPost(HttpServletRequest q,HttpServletResponse r)throws ServletException,IOException{if(!AuthUtil.requireLibrarian(q,r))return;BookDao.delete(q.getParameter("callno"));r.sendRedirect("ViewBook");}}
