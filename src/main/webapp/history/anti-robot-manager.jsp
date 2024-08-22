<%--
  ~ APDPlat - Application Product Development Platform
  ~ Copyright (c) 2013, 杨尚川, yang-shangchuan@qq.com
  ~
  ~  This program is free software: you can redistribute it and/or modify
  ~  it under the terms of the GNU General Public License as published by
  ~  the Free Software Foundation, either version 3 of the License, or
  ~  (at your option) any later version.
  ~
  ~  This program is distributed in the hope that it will be useful,
  ~  but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~  GNU General Public License for more details.
  ~
  ~  You should have received a copy of the GNU General Public License
  ~  along with this program.  If not, see <http://www.gnu.org/licenses/>.
  --%>

<%@ page import="org.apdplat.superword.model.User" %>
<%@ page import="java.util.concurrent.atomic.AtomicInteger" %>
<%@ page import="org.apdplat.superword.system.AntiRobotFilter" %>
<%@ page import="org.apdplat.superword.tools.IPUtils" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User user = (User) request.getSession().getAttribute("user");
    if(user == null || !user.getUserName().equals("ysc")){
        out.println("You do not have permission to access the page.");
        return;
    }

    StringBuilder html = new StringBuilder();

    String limit = request.getParameter("limit");
    try{
        AntiRobotFilter.limit = Integer.parseInt(limit);
    }catch (Exception e){}
    html.append("The maximum number of requests is ")
            .append(AntiRobotFilter.limit)
            .append(" per user per day, invalid request number is ")
            .append(AntiRobotFilter.invalidCount)
            .append(".<br/><br/>");

    html.append("<table>")
            .append("<tr><th>No.</th><th>Username</th><th>User IP</th><th>Access Date</th><th>Access Count</th><th>User Location</th><th>User Agent</th></tr>");
    AtomicInteger i = new AtomicInteger();
    for(String item : AntiRobotFilter.getData()){
        String[] attrs = item.split("-");
        String pre = "";
        String suf = "";
        if(attrs[2].contains("Spider") || attrs[2].contains("bot")){
            pre = "<font color=\"red\">";
            suf = "</font>";
        }
        html.append("<tr><td>")
                .append(pre)
                .append(i.incrementAndGet())
                .append(suf)
                .append("</td><td>")
                .append(pre)
                .append(attrs[0])
                .append(suf)
                .append("</td><td>")
                .append(pre)
                .append(attrs[1])
                .append(suf)
                .append("</td><td>")
                .append(pre)
                .append(attrs[3])
                .append(suf)
                .append("</td><td>")
                .append(pre)
                .append(attrs[4])
                .append(suf)
                .append("</td><td>")
                .append(pre)
                .append(IPUtils.getIPLocation(attrs[1]))
                .append(suf)
                .append("</td><td>")
                .append(pre)
                .append(attrs[2])
                .append(suf)
                .append("</td></tr>");
    }
    html.append("</table>");
%>
<html>
<head>
    <title>anti robot management</title>
    <link href="<%=request.getContextPath()%>/css/superword.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-2.1.4.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/superword.js"></script>
</head>
<body id="top">
<jsp:include page="../common/head.jsp"/>
<%=html.toString()%>
<jsp:include page="../common/bottom.jsp"/>
</body>
</html>
