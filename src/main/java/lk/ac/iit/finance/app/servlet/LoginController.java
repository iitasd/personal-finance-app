package lk.ac.iit.finance.app.servlet;

import lk.ac.iit.finance.app.dao.UserDAO;
import lk.ac.iit.finance.app.model.AuthenticatedUser;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Login controller servlet.
 */
@WebServlet("/login")
public class LoginController extends HttpServlet {

    private static final long serialVersionUID = 9057027649778983955L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.sendRedirect("login.jsp");
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        authenticate(req, resp);
    }

    private void authenticate(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Optional<AuthenticatedUser> authenticatedUserOptional = UserDAO.getInstance()
                .authenticate(req.getParameter("username"), req.getParameter("password").toCharArray());

        if (!authenticatedUserOptional.isPresent()) {
            req.setAttribute("errorMsg", "Provided username or password is invalid!");
            RequestDispatcher dispatcher = req.getRequestDispatcher("login.jsp");
            dispatcher.forward(req, resp);
        }

        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        AuthenticatedUser authenticatedUser = authenticatedUserOptional.get();
        session = req.getSession(true);
        session.setAttribute("userId", authenticatedUser.getUserId());
        session.setAttribute("username", authenticatedUser.getUsername());
        session.setAttribute("firstName", authenticatedUser.getFirstName());
        session.setAttribute("lastName", authenticatedUser.getLastName());

        resp.sendRedirect("summary");
    }
}
