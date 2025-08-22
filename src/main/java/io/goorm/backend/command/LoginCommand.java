package io.goorm.backend.command;

import io.goorm.backend.User;
import io.goorm.backend.UserDAO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * 로그인 처리 Command
 */
public class LoginCommand implements Command {

  @Override
  public String execute(HttpServletRequest request, HttpServletResponse response) {
    try {
      if (request.getMethod().equals("GET")) {
        return "/user/login.jsp";
      } else {
        request.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
          request.setAttribute("error", "아이디와 비밀번호를 모두 입력해주세요.");
          return "/user/login.jsp";
        }

        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserByUsername(username.trim());

        if (user == null) {
          request.setAttribute("error", "존재하지 않는 아이디입니다.");
          return "/user/login.jsp";
        }

        // MD5 해시 비교
        String hashedPassword = hashPassword(password);
        if (!user.getPassword().equals(hashedPassword)) {
          request.setAttribute("error", "비밀번호가 일치하지 않습니다.");
          return "/user/login.jsp";
        }

        HttpSession session = request.getSession();
        session.setAttribute("user", user);

        response.sendRedirect(request.getContextPath() + "/");
        return null;
      }
    } catch (Exception e) {
      request.setAttribute("error", "로그인 처리 중 오류가 발생했습니다: " + e.getMessage());
      return "/user/login.jsp";
    }
  }

  private String hashPassword(String password) {
    try {
      password = password.trim();
      MessageDigest md = MessageDigest.getInstance("MD5");
      byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
      StringBuilder sb = new StringBuilder();
      for (byte b : hash) {
        sb.append(String.format("%02x", b));
      }
      return sb.toString();
    } catch (Exception e) {
      e.printStackTrace();
      return password;
    }
  }
}
