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

<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.apdplat.superword.tools.*" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String pos = request.getParameter("pos");
    String dictionary = request.getParameter("dictionary");
    int limit = 100;
    StringBuilder example = new StringBuilder();
    if(StringUtils.isNotBlank(pos) && StringUtils.isNotBlank(dictionary)){
        String key = "pos-"+pos+dictionary+limit;
        List<String> words = (List<String>)application.getAttribute(key);
        if(words == null){
            words = MySQLUtils.getWordsByPOS(pos, dictionary, limit);
            application.setAttribute(key, words);
        }
        if(!words.isEmpty()){
            example.append("In ")
                    .append(WordLinker.Dictionary.valueOf(dictionary).getDes())
                    .append(" dictionary, the words have the part of speech ")
                    .append(pos)
                    .append(" list below: <br/><br/>");
            List<String> data = new ArrayList<String>();
            for(String word : words){
                data.add(WordLinker.toLink(word));
            }
            example.append(HtmlFormatter.toHtmlTableFragment(data, 10));
        }
    }
%>

<html>
<head>
   <title>part of speech symbol list</title>
    <link href="<%=request.getContextPath()%>/css/superword.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-2.1.4.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/superword.js"></script>
</head>
<body id="top" ondblclick="querySelectionWord();" >
    <jsp:include page="common/head.jsp"/>

    <h3>part of speech symbol list</h3>
    <table border="1" ondblclick="querySelectionWord();">
        <tr><td>Oxford</td><td>Webster's</td><td>iCIBA</td><td>Youdao</td></tr>
        <tr>
            <td valign="top">
                <ol>
                <%
                    for(OxfordPOS item : OxfordPOS.values()){
                        String _pos = item.name().replace("_", " ");
                %>
                    <li><a href="pos.jsp?pos=<%=_pos%>&dictionary=OXFORD"><%=_pos%></a></li>
                <%
                    }
                %>
                <ol>
            </td>
            <td valign="top">
                <ol>
                <%
                for(WebsterPOS item : WebsterPOS.values()){
                        String _pos = item.name();
                %>
                    <li><a href="pos.jsp?pos=<%=_pos%>&dictionary=WEBSTER"><%=_pos%></a></li>
                <%
                }
                %>
                <ol>
            </td>
            <td valign="top">
                <ol>
                    <li><a href="pos.jsp?pos=a.&dictionary=ICIBA">a.</a></li>
                    <li><a href="pos.jsp?pos=abbr.&dictionary=ICIBA">abbr.</a></li>
                    <li><a href="pos.jsp?pos=adj.&dictionary=ICIBA">adj.</a></li>
                    <li><a href="pos.jsp?pos=adv.&dictionary=ICIBA">adv.</a></li>
                    <li><a href="pos.jsp?pos=art.&dictionary=ICIBA">art.</a></li>
                    <li><a href="pos.jsp?pos=aux.&dictionary=ICIBA">aux.</a></li>
                    <li><a href="pos.jsp?pos=conj.&dictionary=ICIBA">conj.</a></li>
                    <li><a href="pos.jsp?pos=det.&dictionary=ICIBA">det.</a></li>
                    <li><a href="pos.jsp?pos=exclam.&dictionary=ICIBA">exclam.</a></li>
                    <li><a href="pos.jsp?pos=int.&dictionary=ICIBA">int.</a></li>
                    <li><a href="pos.jsp?pos=interj.&dictionary=ICIBA">interj.</a></li>
                    <li><a href="pos.jsp?pos=link-v.&dictionary=ICIBA">link-v.</a></li>
                    <li><a href="pos.jsp?pos=n.&dictionary=ICIBA">n.</a></li>
                    <li><a href="pos.jsp?pos=na.&dictionary=ICIBA">na.</a></li>
                    <li><a href="pos.jsp?pos=num.&dictionary=ICIBA">num.</a></li>
                    <li><a href="pos.jsp?pos=phr.&dictionary=ICIBA">phr.</a></li>
                    <li><a href="pos.jsp?pos=pref.&dictionary=ICIBA">pref.</a></li>
                    <li><a href="pos.jsp?pos=prep.&dictionary=ICIBA">prep.</a></li>
                    <li><a href="pos.jsp?pos=pron.&dictionary=ICIBA">pron.</a></li>
                    <li><a href="pos.jsp?pos=v.&dictionary=ICIBA">v.</a></li>
                    <li><a href="pos.jsp?pos=vi.&dictionary=ICIBA">vi.</a></li>
                    <li><a href="pos.jsp?pos=vt.&dictionary=ICIBA">vt.</a></li>
                </ol>
            </td>
            <td valign="top">
                <ol>
                    <li><a href="pos.jsp?pos=abbr.&dictionary=YOUDAO">abbr.</a></li>
                    <li><a href="pos.jsp?pos=adj.&dictionary=YOUDAO">adj.</a></li>
                    <li><a href="pos.jsp?pos=adv.&dictionary=YOUDAO">adv.</a></li>
                    <li><a href="pos.jsp?pos=art.&dictionary=YOUDAO">art.</a></li>
                    <li><a href="pos.jsp?pos=aux.&dictionary=YOUDAO">aux.</a></li>
                    <li><a href="pos.jsp?pos=comb.&dictionary=YOUDAO">comb.</a></li>
                    <li><a href="pos.jsp?pos=conj.&dictionary=YOUDAO">conj.</a></li>
                    <li><a href="pos.jsp?pos=illustrator.&dictionary=YOUDAO">illustrator.</a></li>
                    <li><a href="pos.jsp?pos=int.&dictionary=YOUDAO">int.</a></li>
                    <li><a href="pos.jsp?pos=interj.&dictionary=YOUDAO">interj.</a></li>
                    <li><a href="pos.jsp?pos=misc.&dictionary=YOUDAO">misc.</a></li>
                    <li><a href="pos.jsp?pos=n.&dictionary=YOUDAO">n.</a></li>
                    <li><a href="pos.jsp?pos=neg.&dictionary=YOUDAO">neg.</a></li>
                    <li><a href="pos.jsp?pos=num.&dictionary=YOUDAO">num.</a></li>
                    <li><a href="pos.jsp?pos=pref.&dictionary=YOUDAO">pref.</a></li>
                    <li><a href="pos.jsp?pos=prep.&dictionary=YOUDAO">prep.</a></li>
                    <li><a href="pos.jsp?pos=pron.&dictionary=YOUDAO">pron.</a></li>
                    <li><a href="pos.jsp?pos=suff.&dictionary=YOUDAO">suff.</a></li>
                    <li><a href="pos.jsp?pos=symb.&dictionary=YOUDAO">symb.</a></li>
                    <li><a href="pos.jsp?pos=v.&dictionary=YOUDAO">v.</a></li>
                    <li><a href="pos.jsp?pos=vbl.&dictionary=YOUDAO">vbl.</a></li>
                    <li><a href="pos.jsp?pos=vi.&dictionary=YOUDAO">vi.</a></li>
                    <li><a href="pos.jsp?pos=vt.&dictionary=YOUDAO">vt.</a></li>
                </ol>
            </td>
        </tr>
    </table>

    <br/>
    <%=example%>

    <jsp:include page="common/bottom.jsp"/>
</body>
</html>
