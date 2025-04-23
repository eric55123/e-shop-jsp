<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="com.eshop.admin.model.Admin" %>
<html>
<head>
    <title>評論檢舉審核</title>
    <a href="<%= request.getContextPath() %>/back" class="btn btn-secondary">🔙 回後台首頁</a>
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 10px;
            text-align: left;
            vertical-align: top;
        }
        th {
            background-color: #f2f2f2;
        }
        .reply-box {
            margin-top: 5px;
        }
    </style>
</head>
<body>

<%
    Admin loggedInAdmin = (Admin) session.getAttribute("loggedInAdmin");
    if (loggedInAdmin == null) {
        response.sendRedirect(request.getContextPath() + "/admin/adminLogin.jsp");
        return;
    }
%>

<h2>🛡️ 評論檢舉審核</h2>

<s:if test="pendingReports.size() == 0">
    <p>目前沒有待審核的檢舉。</p>
</s:if>

<s:else>
    <table>
        <tr>
            <th>檢舉編號</th>
            <th>評論內容</th>
            <th>檢舉理由</th>
            <th>檢舉時間</th>
            <th>檢舉人</th>
            <th>審核操作</th>
        </tr>

        <s:iterator value="pendingReports" var="r">
            <tr>
                <td><s:property value="#r.reportId" /></td>
                <td><s:property value="#r.comment.commentText" /></td>
                <td><s:property value="#r.reason" /></td>
                <td><s:property value="#r.reportTime" /></td>
                <td><s:property value="#r.reporter.name" /></td>
                <td>
                    <form action="updateCommentReportStatus.action" method="post">
                        <input type="hidden" name="reportId" value="<s:property value='#r.reportId'/>"/>

                        <div class="reply-box">
                            <label for="reply_<s:property value='#r.reportId'/>">回覆：</label><br>
                            <textarea id="reply_<s:property value='#r.reportId'/>" name="reply" rows="3" cols="30"
                                      placeholder="處理意見，可留空"></textarea>
                        </div>

                        <br>
                        <button type="submit" name="status" value="1"
                                onclick="return confirm('確定要駁回這筆檢舉嗎？')">✅ 駁回</button>
                        <button type="submit" name="status" value="2"
                                onclick="return confirm('確定要封鎖這筆評論嗎？')">🚫 封鎖</button>
                    </form>
                </td>
            </tr>
        </s:iterator>

    </table>
</s:else>

</body>
</html>
