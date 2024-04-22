package servlets;

import dto.user.CreateUserModel;
import dto.user.UserModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.UserService;
import utils.JspPathCreator;

import java.io.IOException;

import static utils.UrlPathGetter.LOGIN;
import static utils.UrlPathGetter.REGISTRATION;

@WebServlet(REGISTRATION)
public class RegisterServlet extends HttpServlet {
    private static final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(JspPathCreator.getPath("registration")).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            UserModel model = userService.create(CreateUserModel.builder()
                    .fullName(fullName)
                    .email(email)
                    .password(password)
                    .build());
            response.sendRedirect(request.getContextPath() + LOGIN);
        } catch (IllegalArgumentException e) {
            String message = e.getMessage();
            request.setAttribute("message", message);
            request.getRequestDispatcher(JspPathCreator.getPath("registration")).forward(request, response);
        }
    }
}
