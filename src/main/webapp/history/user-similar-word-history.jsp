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

<%@ page import="org.apdplat.superword.tools.MySQLUtils" %>
<%@ page import="java.util.List" %>
<%@ page import="org.apdplat.superword.model.UserSimilarWord" %>
<%@ page import="org.apdplat.superword.model.User" %>
<%@ page import="org.apdplat.superword.model.QQUser" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    User user = (User)session.getAttribute("user");
    String displayName = user.getUserName();
    if(user instanceof QQUser){
        displayName = ((QQUser)user).getNickname();
    }
    List<UserSimilarWord> userSimilarWords = MySQLUtils.getHistoryUserSimilarWordsFromDatabase(user.getUserName());
    StringBuilder htmlFragment = new StringBuilder();
    htmlFragment.append("<table>");
    htmlFragment.append("<tr><th>No.</th><th>Spelling Similarity</th><th>Definition Similarity</th><th>Pronunciation Similarity</th><th>Time</th></tr>");
    int i = 1;
    for (UserSimilarWord userSimilarWord : userSimilarWords) {
        htmlFragment.append("<tr><td>")
                .append(i++)
                .append("</td><td>")
                .append("<a target=\"_blank\" href=\"../similar/spell-similar-rule.jsp?count=100&words_type=SYLLABUS&word=")
                .append(userSimilarWord.getSimilarWord())
                .append("\">")
                .append(userSimilarWord.getSimilarWord())
                .append("</a>")
                .append("</td><td>")
                .append("<a target=\"_blank\" href=\"../similar/definition-similar-rule.jsp?dictionary=WEBSTER&count=100&words_type=SYLLABUS&word=")
                .append(userSimilarWord.getSimilarWord())
                .append("\">")
                .append(userSimilarWord.getSimilarWord())
                .append("</a>")
                .append("</td><td>")
                .append("<a target=\"_blank\" href=\"../similar/pronunciation-similar-rule.jsp?dictionary=OXFORD&count=100&words_type=SYLLABUS&word=")
                .append(userSimilarWord.getSimilarWord())
                .append("\">")
                .append(userSimilarWord.getSimilarWord())
                .append("</a>")
                .append("</td><td>")
                .append(userSimilarWord.getDateTimeString())
                .append("</td></tr>");
    }
    htmlFragment.append("</table>");
%>

<html>
<head>
    <title>similar word analysis record</title>
    <link href="<%=request.getContextPath()%>/css/superword.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-2.1.4.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/superword.js"></script>
</head>
<body id="top">
<jsp:include page="../common/head.jsp"/>
<h3>user <%=displayName%> similar word analysis record</h3>
<%=htmlFragment%>
<jsp:include page="../common/bottom.jsp"/>
</body>
</html>
