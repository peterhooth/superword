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

<%@ page import="org.apdplat.superword.model.Suffix" %>
<%@ page import="org.apdplat.superword.model.Word" %>
<%@ page import="org.apdplat.superword.rule.SuffixRule" %>
<%@ page import="org.apdplat.superword.tools.HtmlFormatter" %>
<%@ page import="java.util.*" %>
<%@ page import="java.util.concurrent.ConcurrentHashMap" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String suffixes = request.getParameter("suffixes");
    String htmlFragment = "";
    int column = 10;
    if(suffixes != null && !"".equals(suffixes.trim())){
        Map<String, Suffix> map = (Map<String, Suffix>)application.getAttribute("all_suffix");
        if(map == null){
            map = new ConcurrentHashMap<String, Suffix>();
            for(Suffix prefix : SuffixRule.getAllSuffixes()){
                map.put(prefix.getSuffix().replace("-", ""), prefix);
            }
            application.setAttribute("all_suffix", map);
        }
        List<Suffix> suffixList = new ArrayList<Suffix>();
        for(String suffix : suffixes.trim().split(",")){
            suffixList.add(new Suffix(suffix, map.get(suffix.replace("-", ""))==null?"":map.get(suffix.replace("-", "")).getDes()));
        }
        Set<Word> words = (Set<Word>)application.getAttribute("words_"+request.getAttribute("words_type"));
        TreeMap<Suffix, List<Word>> data = SuffixRule.findBySuffix(words, suffixList, "N".equalsIgnoreCase(request.getParameter("strict")) ? false : true);
        for(Map.Entry<Suffix, List<Word>> entry : data.entrySet()){
            if(entry.getValue().size() > 500) {
                entry.setValue(entry.getValue().subList(0, 500));
            }
        }
        try{
            column = Integer.parseInt(request.getParameter("column"));
        }catch (Exception e){}
        htmlFragment = HtmlFormatter.toHtmlTableFragmentForRootAffix(SuffixRule.convert(data), column);
    }
%>
<html>
<head>
    <title>后缀规则</title>
    <link href="<%=request.getContextPath()%>/css/superword.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-2.1.4.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/superword.js"></script>
    <script type="text/javascript">
        function update(){
            var suffixes = document.getElementById("suffixes").value;
            var words_type = document.getElementById("words_type").value;
            var strict = document.getElementById("strict").value;
            var column = document.getElementById("column").value;

            if(suffixes == ""){
                return;
            }
            location.href = "suffix-rule.jsp?suffixes="+suffixes+"&words_type="+words_type+"&strict="+strict+"&column="+column;
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
        ***suffix rule:
        Find out the words that are consistent with the suffix rule from the set of the specified English words, such as: ics or ian, multiple suffixes can be separated by a comma, such as: ence, age, ance.
    </p>
    <p>
        <font color="red">input suffix: </font><input onchange="update();" id="suffixes" name="suffixes" value="<%=suffixes==null?"":suffixes%>" size="50" maxlength="50"><br/>
        <font color="red">words per line: </font><input onchange="update();" id="column" name="column" value="<%=column%>" size="50" maxlength="50"><br/>
        <font color="red">strictly match: </font>
        <jsp:include page="../select/strict-select.jsp"/><br/>
        <font color="red">select words level: </font>
        <jsp:include page="../select/words-select.jsp"/><br/><br/>
        <span style="cursor: pointer" onclick="update();"><font color="red">Submit</font></span>
    </p>
    <%=htmlFragment%>
    <jsp:include page="../common/bottom.jsp"/>
</body>
</html>
