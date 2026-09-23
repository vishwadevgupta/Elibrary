package com.javatpoint.util;
public final class HtmlUtil{private HtmlUtil(){}public static String e(String s){if(s==null)return "";return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;").replace("\"","&quot;").replace("'","&#39;");}}
