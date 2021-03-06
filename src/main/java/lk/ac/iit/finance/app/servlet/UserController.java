package lk.ac.iit.finance.app.servlet;

import lk.ac.iit.finance.app.dao.UserDAO;
import lk.ac.iit.finance.app.model.User;
import lk.ac.iit.finance.app.util.CategoryUtil;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User controller servlet.
 */
@WebServlet("/register")
public class UserController extends HttpServlet {

    private static final long serialVersionUID = 6530318633036168643L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("register.jsp");
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        registerUser(req, resp);
    }

    private void registerUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        User user = new User.UserBuilder().firstName(req.getParameter("firstName"))
                .lastName(req.getParameter("lastName")).username(req.getParameter("username"))
                .password(req.getParameter("password")).build();

        UserDAO.getInstance().registerUser(user);
        CategoryUtil.addDefaultCategories(user.getUserId());

        RequestDispatcher dispatcher = req.getRequestDispatcher("login.jsp");
        dispatcher.forward(req, resp);
    }
}
