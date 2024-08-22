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

<%@ page import="org.apdplat.superword.model.Word" %>
<%@ page import="java.util.Set" %>
<%@ page import="org.apdplat.superword.model.Prefix" %>
<%@ page import="java.util.List" %>
<%@ page import="org.apdplat.superword.rule.DynamicPrefixRule" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.apdplat.superword.model.UserDynamicPrefix" %>
<%@ page import="java.util.Date" %>
<%@ page import="org.apdplat.superword.tools.MySQLUtils" %>
<%@ page import="org.apdplat.superword.model.User" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String prefixes = request.getParameter("prefixes");
    String htmlFragment = "";
    if(prefixes != null && !"".equals(prefixes.trim()) && prefixes.contains("-")){
        User user = (User)session.getAttribute("user");
        UserDynamicPrefix userDynamicPrefix = new UserDynamicPrefix();
        userDynamicPrefix.setDynamicPrefix(prefixes);
        userDynamicPrefix.setDateTime(new Date());
        userDynamicPrefix.setUserName(user == null ? "anonymity" : user.getUserName());
        MySQLUtils.saveUserDynamicPrefixToDatabase(userDynamicPrefix);

        Set<Word> words = (Set<Word>)application.getAttribute("words_"+request.getAttribute("words_type"));

        List<Prefix> prefixList = new ArrayList<Prefix>();
        for(String prefix : prefixes.trim() .split("-")){
            prefixList.add(new Prefix(prefix, ""));
        }
        List<Word> data = DynamicPrefixRule.findByPrefix(words, prefixList);
        if(data.size() > 500){
            data = data.subList(0, 500);
        }
        htmlFragment = DynamicPrefixRule.toHtmlFragment(data, prefixList);
    }
%>
<html>
<head>
    <title>dynamic prefix rule</title>
    <link href="<%=request.getContextPath()%>/css/superword.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-2.1.4.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/superword.js"></script>
    <script type="text/javascript">
        function update(){
            var prefixes = document.getElementById("prefixes").value;
            var words_type = document.getElementById("words_type").value;
            if(prefixes == ""){
                return;
            }
            location.href = "dynamic-prefix-rule.jsp?prefixes="+prefixes+"&words_type="+words_type;
        }
        document.onkeypress=function(e){
            var e = window.event || e ;
            if(e.charCode == 13){
                update();
            }
        }
    </script>
</head>
<body id="top">
    <jsp:include page="../common/head.jsp"/>
    <p>
        ***dynamic prefix rule:
        For example, the rule is: m-imm, there are any two words respective started with m and imm, and in addition to the prefix, the other parts are the same.
    </p>
    <p>
        <font color="red">input dynamic prefix：</font><input onchange="update();" id="prefixes" name="prefixes" value="<%=prefixes==null?"":prefixes%>" size="50" maxlength="50"><br/>
        <font color="red">select words level：</font>
        <jsp:include page="../select/words-select.jsp"/><br/><br/>
        <span style="cursor: pointer" onclick="update();"><font color="red">Submit</font></span>
    </p>
    <%=htmlFragment%>
    <jsp:include page="../common/bottom.jsp"/>
</body>
</html>
