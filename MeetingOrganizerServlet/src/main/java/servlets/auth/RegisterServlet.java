package servlets.auth;

import dto.user.CreateUserModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mappers.user.CreateUserMapper;
import mappers.user.UserMapper;
import repositories.UserRepository;
import services.UserService;
import utils.ConnectionManager;
import utils.JspPathCreator;
import validators.UserValidator;

import java.io.IOException;

import static utils.UrlPathGetter.LOGIN;
import static utils.UrlPathGetter.REGISTRATION;

@WebServlet(REGISTRATION)
public class RegisterServlet extends HttpServlet {
    private final UserService userService = new UserService(
            new UserRepository(new ConnectionManager()),
            new UserMapper(),
            new CreateUserMapper()
    );
    private final UserValidator userValidator = new UserValidator();

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
            CreateUserModel model = CreateUserModel.builder()
                    .fullName(fullName)
                    .email(email)
                    .password(password)
                    .build();

            userValidator.validate(model);

            userService.create(model);

            response.sendRedirect(request.getContextPath() + LOGIN);
        } catch (IllegalArgumentException e) {
            request.setAttribute("fullName", fullName);
            request.setAttribute("email", email);
            request.setAttribute("password", password);
            request.setAttribute("message", e.getMessage());
            request.getRequestDispatcher(JspPathCreator.getPath("registration")).forward(request, response);
        }
    }
}
