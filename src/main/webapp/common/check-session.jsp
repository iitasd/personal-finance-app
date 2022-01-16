<%
    if (request.getSession(false).getAttribute("userId") == null
            || request.getSession(false).getAttribute("username") == null) {

        response.sendRedirect("login/login.jsp");
    }
%>
