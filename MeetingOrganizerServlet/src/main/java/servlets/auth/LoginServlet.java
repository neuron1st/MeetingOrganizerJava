package servlets.auth;

import dto.user.UserModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.UserService;
import utils.JspPathCreator;
import utils.PasswordHash;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import static utils.UrlPathGetter.LOGIN;
import static utils.UrlPathGetter.MEETINGS;

@WebServlet(LOGIN)
public class LoginServlet extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(!Objects.isNull(request.getSession().getAttribute("user"))) {
            response.sendRedirect(request.getContextPath());
            return;
        }
        request.getRequestDispatcher(JspPathCreator.getPath("login")).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Optional<String> error = userService.checkLogin(request.getParameter("email"), request.getParameter("password"));

        if (error.isEmpty()) {
            UserModel user = userService.getByEmail(request.getParameter("email")).orElseThrow();
            request.getSession().setAttribute("user", user);
            response.sendRedirect(request.getContextPath() + MEETINGS);
        } else {
            request.setAttribute("message", error.get());
            request.getRequestDispatcher(JspPathCreator.getPath("login")).forward(request, response);
        }
    }
}
