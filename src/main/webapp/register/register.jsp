<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="ISO-8859-1">
    <title>Register</title>

    <%--    <script src="../assets/dist/js/bootstrap.bundle.min.js"></script>--%>
    <%--    <link href="../assets/dist/css/bootstrap.min.css" rel="stylesheet">--%>
    <%--    <link href="../css/custom.css" rel="stylesheet">--%>
</head>

<body class="d-flex flex-column">
<div id="page-content">
    <jsp:include page="../common/header.jsp"></jsp:include>
    <div class="container">

        <h2>User Register Form</h2>
        <div class="col-md-6 col-md-offset-3">
            <%--        <div class="alert alert-success center" role="alert">--%>
            <%--            <p>${msg}</p>--%>
            <%--        </div>--%>

            <form action="<%=request.getContextPath()%>/register" method="post">

                <div class="form-group">
                    <label for="fistNameTxt">First Name:</label> <input type="text" class="form-control"
                                                                        id="fistNameTxt"
                                                                        placeholder="First Name" name="firstName"
                                                                        required>
                </div>

                <div class="form-group">
                    <label for="lastNameTxt">Last Name:</label> <input type="text" class="form-control" id="lastNameTxt"
                                                                       placeholder="last Name" name="lastName" required>
                </div>

                <div class="form-group">
                    <label for="usernameTxt">User Name:</label> <input type="text" class="form-control" id="usernameTxt"
                                                                       placeholder="User Name" name="username" required>
                </div>

                <div class="form-group">
                    <label for="passwordTxt">Password:</label> <input type="password" class="form-control"
                                                                      id="passwordTxt"
                                                                      placeholder="Password" name="password" required>
                </div>

                <button type="submit" class="btn btn-primary">Submit</button>

            </form>
        </div>
    </div>
    <jsp:include page="../common/footer.jsp"></jsp:include>
</div>
</body>
</html>