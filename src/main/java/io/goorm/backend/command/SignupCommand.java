package io.goorm.backend.command;

import io.goorm.backend.User;
import io.goorm.backend.UserDAO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 회원가입 처리 Command
 */
public class SignupCommand implements Command {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            if (request.getMethod().equals("GET")) {
                return "/user/signup.jsp";
            } else {
                request.setCharacterEncoding("UTF-8");

                String username = request.getParameter("username");
                String password = request.getParameter("password");
                String confirmPassword = request.getParameter("confirmPassword");
                String name = request.getParameter("name");
                String email = request.getParameter("email");

                if (username == null || username.trim().isEmpty() ||
                        password == null || password.trim().isEmpty() ||
                        name == null || name.trim().isEmpty()) {
                    request.setAttribute("error", "필수 필드를 모두 입력해주세요.");
                    request.setAttribute("username", username);
                    request.setAttribute("name", name);
                    request.setAttribute("email", email);
                    return "/user/signup.jsp";
                }

                if (!password.equals(confirmPassword)) {
                    request.setAttribute("error", "비밀번호가 일치하지 않습니다.");
                    request.setAttribute("username", username);
                    request.setAttribute("name", name);
                    request.setAttribute("email", email);
                    return "/user/signup.jsp";
                }

                UserDAO userDAO = new UserDAO();
                if (userDAO.getUserByUsername(username) != null) {
                    request.setAttribute("error", "이미 사용 중인 아이디입니다.");
                    request.setAttribute("username", username);
                    request.setAttribute("name", name);
                    request.setAttribute("email", email);
                    return "/user/signup.jsp";
                }

                if (email != null && !email.trim().isEmpty() && userDAO.getUserByEmail(email) != null) {
                    request.setAttribute("error", "이미 사용 중인 이메일입니다.");
                    request.setAttribute("username", username);
                    request.setAttribute("name", name);
                    request.setAttribute("email", email);
                    return "/user/signup.jsp";
                }

                User user = new User();
                user.setUsername(username.trim());
                user.setPassword(password); // 해시는 DAO에서 처리
                user.setName(name.trim());
                user.setEmail(email != null ? email.trim() : null);

                if (userDAO.insertUser(user)) {
                    request.setAttribute("success", "회원가입이 완료되었습니다. 로그인해주세요.");
                    return "/user/login.jsp";
                } else {
                    request.setAttribute("error", "회원가입에 실패했습니다. 다시 시도해주세요.");
                    request.setAttribute("username", username);
                    request.setAttribute("name", name);
                    request.setAttribute("email", email);
                    return "/user/signup.jsp";
                }
            }
        } catch (Exception e) {
            request.setAttribute("error", "회원가입 처리 중 오류가 발생했습니다: " + e.getMessage());
            return "/user/signup.jsp";
        }
    }
}
