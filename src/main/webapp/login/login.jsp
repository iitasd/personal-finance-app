<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="ISO-8859-1">
    <title>Login</title>
<%--    <script src="../assets/dist/js/bootstrap.bundle.min.js"></script>--%>
<%--    <link href="../assets/dist/css/bootstrap.min.css" rel="stylesheet">--%>
<%--    <link href="../css/custom.css" rel="stylesheet">--%>
</head>

<body class="d-flex flex-column">
<div id="page-content">
    <jsp:include page="../common/header.jsp"></jsp:include>
    <div class="container col-md-8 col-md-offset-3" style="overflow: auto">
        <h1>Login Form</h1>
        <form action="<%=request.getContextPath()%>/login" method="post">

            <div class="form-group">
                <label for="usernameTxt">User Name:</label> <input type="text" class="form-control" id="usernameTxt"
                                                                   placeholder="User Name" name="username" required>
            </div>

            <div class="form-group">
                <label for="passwordTxt">Password:</label> <input type="password" class="form-control" id="passwordTxt"
                                                                  placeholder="Password" name="password" required>
            </div>


            <button type="submit" class="btn btn-primary">Submit</button>
        </form>
    </div>
    <jsp:include page="../common/footer.jsp"></jsp:include>
</div>
</body>
</html>