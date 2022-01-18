<%@ page import="lk.ac.iit.finance.app.model.AbstractCategory" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <%
        if (request.getSession(false) == null || request.getSession(false).getAttribute("userId") == null) {
            response.sendRedirect("login.jsp");
        }
    %>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Personal Finance Manager - Dashboard</title>

    <!-- Custom fonts for this template-->
    <link href="vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
    <link
            href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
            rel="stylesheet">

    <!-- Custom styles for this template-->
    <link href="css/sb-admin-2.min.css" rel="stylesheet">
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/css/bootstrap-datepicker.min.css">

    <script type="application/javascript">
        $('.datepicker').datepicker({
            format: 'mm-dd-yyyy'
        });
    </script>
</head>

<body id="page-top">

<!-- Page Wrapper -->
<div id="wrapper">

    <!-- Sidebar -->
    <ul class="navbar-nav bg-gradient-primary sidebar sidebar-dark accordion" id="accordionSidebar">

        <!-- Sidebar - Brand -->
        <a class="sidebar-brand d-flex align-items-center justify-content-center" href="<%=request.getContextPath()%>/">
            <div class="sidebar-brand-icon rotate-n-15">
                <i class="fas fa-laugh"></i>
            </div>
            <div class="sidebar-brand-text mx-3">PF MANAGER</div>
        </a>

        <!-- Divider -->
        <hr class="sidebar-divider my-0">

        <!-- Nav Item - Dashboard -->
        <li class="nav-item">
            <a class="nav-link" href="index.jsp">
                <i class="fas fa-fw fa-tachometer-alt"></i>
                <span>Summary</span></a>
        </li>

        <!-- Divider -->
        <hr class="sidebar-divider">

        <!-- Heading -->
        <div class="sidebar-heading">
            Operations
        </div>

        <!-- Nav Item - Pages Collapse Menu -->
        <!-- Nav Item - Tables -->
        <li class="nav-item active">
            <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapseOne"
               aria-expanded="true" aria-controls="collapseOne">
                <i class="fas fa-fw fa-university"></i>
                <span>Transactions</span>
            </a>
            <div id="collapseOne" class="collapse" aria-labelledby="headingTwo" data-parent="#accordionSidebar">
                <div class="bg-white py-2 collapse-inner rounded">
                    <h6 class="collapse-header">Actions:</h6>
                    <a class="collapse-item" href="<%=request.getContextPath()%>/transactions">Completed
                        Transactions</a>
                    <a class="collapse-item"
                       href="<%=request.getContextPath()%>/recurring-transactions">Recurring Transactions
                    </a>
                    <a class="collapse-item" href="<%=request.getContextPath()%>/add-income">Add Income</a>
                    <a class="collapse-item" href="<%=request.getContextPath()%>/add-expense">Add Expense</a>
                </div>
            </div>
        </li>
        <li class="nav-item">
            <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapseTwo"
               aria-expanded="true" aria-controls="collapseTwo">
                <i class="fas fa-fw fa-bars"></i>
                <span>Categories</span>
            </a>
            <div id="collapseTwo" class="collapse" aria-labelledby="headingTwo" data-parent="#accordionSidebar">
                <div class="bg-white py-2 collapse-inner rounded">
                    <h6 class="collapse-header">Actions:</h6>
                    <a class="collapse-item active" href="<%=request.getContextPath()%>/categories">List</a>
                    <a class="collapse-item active" href="<%=request.getContextPath()%>/add-category">Add</a>
                </div>
            </div>
        </li>
        <li class="nav-item">
            <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapseThree"
               aria-expanded="true" aria-controls="collapseTwo">
                <i class="fas fa-fw fa-percent"></i>
                <span>Budget</span>
            </a>
            <div id="collapseThree" class="collapse" aria-labelledby="headingTwo" data-parent="#accordionSidebar">
                <div class="bg-white py-2 collapse-inner rounded">
                    <h6 class="collapse-header">Actions:</h6>
                    <a class="collapse-item" href="<%=request.getContextPath()%>/budgets">View</a>
                    <a class="collapse-item" href="<%=request.getContextPath()%>/add-budget">Add</a>
                </div>
            </div>
        </li>

        <!-- Divider -->
        <hr class="sidebar-divider d-none d-md-block">

        <!-- Sidebar Toggler (Sidebar) -->
        <div class="text-center d-none d-md-inline">
            <button class="rounded-circle border-0" id="sidebarToggle"></button>
        </div>

    </ul>
    <!-- End of Sidebar -->

    <!-- Content Wrapper -->
    <div id="content-wrapper" class="d-flex flex-column">

        <!-- Main Content -->
        <div id="content">

            <!-- Topbar -->
            <nav class="navbar navbar-expand navbar-light bg-white topbar mb-4 static-top shadow">

                <!-- Sidebar Toggle (Topbar) -->
                <button id="sidebarToggleTop" class="btn btn-link d-md-none rounded-circle mr-3">
                    <i class="fa fa-bars"></i>
                </button>

                <!-- Topbar Navbar -->
                <ul class="navbar-nav ml-auto">

                    <!-- Nav Item - User Information -->
                    <li class="nav-item dropdown no-arrow">
                        <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button"
                           data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <span class="mr-2 d-none d-lg-inline text-gray-600
                                    small"><%=session.getAttribute("firstName")%>
                                <%=session.getAttribute("lastName")%></span>
                            <img class="img-profile rounded-circle"
                                 src="img/undraw_profile.svg">
                        </a>
                        <!-- Dropdown - User Information -->
                        <div class="dropdown-menu dropdown-menu-right shadow animated--grow-in"
                             aria-labelledby="userDropdown">
                            <a class="dropdown-item" href="#" data-toggle="modal" data-target="#logoutModal">
                                <i class="fas fa-sign-out-alt fa-sm fa-fw mr-2 text-gray-400"></i>
                                Logout
                            </a>
                        </div>
                    </li>

                </ul>

            </nav>
            <!-- End of Topbar -->

            <!-- Begin Page Content -->
            <div class="container-fluid">

                <!-- Page Heading -->
                <%
                    if ("Income".equals(request.getAttribute("transactionType"))) {
                %>
                <h1 class="h3 mb-4 text-gray-800">Add Income</h1>
                <p class="mb-4">Create new income</p>
                <%
                } else {
                %>
                <h1 class="h3 mb-4 text-gray-800">Add Expense</h1>
                <p class="mb-4">Create new expense</p>
                <%
                    }
                %>

                <div class="row">

                    <div class="col-lg-6">

                        <!-- Circle Buttons -->
                        <div class="card shadow mb-4">
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-primary">Transaction Details</h6>
                            </div>
                            <div class="card-body">
                                <%
                                    if (request.getAttribute("errorMsg") != null) {
                                %>
                                <div class="text-center">
                                    <h2 class="h5 text-danger mb-4"><%=request.getAttribute("errorMsg")%>
                                    </h2>
                                </div>
                                <%
                                        request.removeAttribute("errorMsg");
                                    }
                                %>
                                <%
                                    if (request.getAttribute("msg") != null) {
                                %>
                                <div class="text-center">
                                    <h2 class="h5 text-success mb-4"><%=request.getAttribute("msg")%>
                                    </h2>
                                </div>
                                <%
                                        request.removeAttribute("msg");
                                    }
                                %>
                                <form class="user" action="<%=request.getContextPath()%>/transactions" method="post">
                                    <div class="form-group row">
                                        <div class="col-sm-6 mb-3 mb-sm-0">
                                            <select class="form-select btn-user" style="width: 100%" name="category">
                                                <option selected>Category</option>
                                                <%
                                                    if (request.getAttribute("categories") != null) {
                                                        List<AbstractCategory> categories = (List<AbstractCategory>) request
                                                                .getAttribute("categories");
                                                        if (categories.size() > 0) {
                                                            for (AbstractCategory category : categories) {
                                                %>
                                                <option value="<%=category.getCategoryId()%>"><%=category
                                                        .getCategoryName()%>
                                                </option>
                                                <%
                                                            }
                                                        }
                                                        request.removeAttribute("categories");
                                                    }
                                                %>
                                            </select>
                                        </div>
                                        <div class="col-sm-6">
                                            <input type="text" class="form-control form-control-user"
                                                   id="amountInput"
                                                   placeholder="Amount" name="amount">
                                        </div>
                                    </div>
                                    <div class="form-group row">
                                        <div class="col-sm-6 mb-3 mb-sm-0">
                                            <div class="input-group date">
                                                <input type="text" class="form-control form-control-user"
                                                       id="datepicker" placeholder="Date" name="date">
                                            </div>
                                        </div>
                                        <div class="col-sm-6">
                                            <input type="text" class="form-control form-control-user"
                                                   id="noteInput"
                                                   placeholder="Note" name="note">
                                        </div>
                                    </div>
                                    <hr>
                                    <div class="form-group row">
                                        <div class="col-sm-6 mb-3 mb-sm-0">
                                            <div class="form-check form-switch">
                                                <input class="form-check-input" type="checkbox" id="recurrentSwitch"
                                                       name="recurrence">
                                                <label class="form-check-label" for="recurrentSwitch">Recurrent
                                                    event</label>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group row recurrence-group">
                                        <div class="col-sm-6 mb-3 mb-sm-0">
                                            <select class="form-select btn-user" style="width: 100%" name="frequency">
                                                <option value="DAILY">Daily</option>
                                                <option value="WEEKLY">Weekly</option>
                                                <option value="Monthly">Monthly</option>
                                                <option value="Monthly">Yearly</option>
                                            </select>
                                        </div>
                                        <div class="col-sm-6">
                                            <input type="text" class="form-control form-control-user"
                                                   id="occurrenceCount"
                                                   placeholder="Occurrences" name="occurrenceCount">
                                        </div>
                                    </div>
                                    <input type="hidden" name="transactionType"
                                           value="<%=request.getAttribute("transactionType")%>">
                                    <button type="submit" class="btn btn-primary btn-user">
                                        Create
                                    </button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
            <!-- /.container-fluid -->

        </div>
        <!-- End of Main Content -->

        <!-- Footer -->
        <footer class="sticky-footer bg-white">
            <div class="container my-auto">
                <div class="copyright text-center my-auto">
                    <span>Copyright &copy; PF Manager 2022</span>
                </div>
            </div>
        </footer>
        <!-- End of Footer -->

    </div>
    <!-- End of Content Wrapper -->

</div>
<!-- End of Page Wrapper -->

<!-- Scroll to Top Button-->
<a class="scroll-to-top rounded" href="#page-top">
    <i class="fas fa-angle-up"></i>
</a>

<!-- Logout Modal-->
<div class="modal fade" id="logoutModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel">Ready to Leave?</h5>
                <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">×</span>
                </button>
            </div>
            <div class="modal-body">Select "Logout" below if you are ready to end your current session.</div>
            <div class="modal-footer">
                <button class="btn btn-secondary" type="button" data-dismiss="modal">Cancel</button>
                <a class="btn btn-primary" href="<%=request.getContextPath()%>/logout">Logout</a>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap core JavaScript-->
<script src="vendor/jquery/jquery.min.js"></script>
<script src="vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

<!-- Core plugin JavaScript-->
<script src="vendor/jquery-easing/jquery.easing.min.js"></script>

<!-- Custom scripts for all pages-->
<script src="js/sb-admin-2.min.js"></script>

<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/js/bootstrap-datepicker.min.js"></script>

<script>
    $(document).ready(function () {
        $('#datepicker').datepicker();
        $('.recurrence-group').hide();
        $('#recurrentSwitch').change(function () {
            if (this.checked) {
                $('.recurrence-group').show();
            } else {
                $('.recurrence-group').hide();
            }
        });
    });
</script>
</body>

</html>
