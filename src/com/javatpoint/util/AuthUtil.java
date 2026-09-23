package com.javatpoint.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class AuthUtil {
    private AuthUtil() { }
    public static boolean requireAdmin(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException {
        if (!"admin".equals(request.getSession(false) == null ? null : request.getSession(false).getAttribute("role"))) {
            response.sendRedirect("index.html"); return false;
        }
        return true;
    }
    public static boolean requireLibrarian(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException {
        if (!"librarian".equals(request.getSession(false) == null ? null : request.getSession(false).getAttribute("role"))) {
            response.sendRedirect("index.html"); return false;
        }
        return true;
    }
}
