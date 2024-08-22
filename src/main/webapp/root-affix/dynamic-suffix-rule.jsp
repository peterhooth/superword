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
<%@ page import="org.apdplat.superword.model.Suffix" %>
<%@ page import="java.util.List" %>
<%@ page import="org.apdplat.superword.rule.DynamicSuffixRule" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.apdplat.superword.model.UserDynamicSuffix" %>
<%@ page import="java.util.Date" %>
<%@ page import="org.apdplat.superword.tools.MySQLUtils" %>
<%@ page import="org.apdplat.superword.model.User" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String suffixes = request.getParameter("suffixes");
    String htmlFragment = "";
    if(suffixes != null && !"".equals(suffixes.trim()) && suffixes.contains("-")){
        User user = (User)session.getAttribute("user");
        UserDynamicSuffix userDynamicSuffix = new UserDynamicSuffix();
        userDynamicSuffix.setDynamicSuffix(suffixes);
        userDynamicSuffix.setDateTime(new Date());
        userDynamicSuffix.setUserName(user == null ? "anonymity" : user.getUserName());
        MySQLUtils.saveUserDynamicSuffixToDatabase(userDynamicSuffix);

        List<Suffix> suffixList = new ArrayList<Suffix>();
        for(String suffix : suffixes.trim() .split("-")){
            suffixList.add(new Suffix(suffix, ""));
        }
        Set<Word> words = (Set<Word>)application.getAttribute("words_"+request.getAttribute("words_type"));
        List<Word> data = DynamicSuffixRule.findBySuffix(words, suffixList);
        if(data.size() > 500){
            data = data.subList(0, 500);
        }
        htmlFragment = DynamicSuffixRule.toHtmlFragment(data, suffixList);
    }
%>
<html>
<head>
    <title>dynamic suffix rule</title>
    <link href="<%=request.getContextPath()%>/css/superword.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-2.1.4.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/superword.js"></script>
    <script type="text/javascript">
        function update(){
            var suffixes = document.getElementById("suffixes").value;
            var words_type = document.getElementById("words_type").value;
            if(suffixes == ""){
                return;
            }
            location.href = "dynamic-suffix-rule.jsp?suffixes="+suffixes+"&words_type="+words_type;
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
        ***dynamic suffix rule:
        For example, the rule is: ise-ize, there are any two words respective ended with ize and ise, and in addition to the suffix, the other parts are the same.
    </p>
    <p>
        <font color="red">input dynamic suffix：</font><input onchange="update();" id="suffixes" name="suffixes" value="<%=suffixes==null?"":suffixes%>" size="50" maxlength="50"><br/>
        <font color="red">select words level：</font>
        <jsp:include page="../select/words-select.jsp"/><br/><br/>
        <span style="cursor: pointer" onclick="update();"><font color="red">Submit</font></span>
    </p>
    <%=htmlFragment%>
    <jsp:include page="../common/bottom.jsp"/>
</body>
</html>
