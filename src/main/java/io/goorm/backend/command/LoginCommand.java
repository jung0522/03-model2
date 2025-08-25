package io.goorm.backend.command;

import io.goorm.backend.User;
import io.goorm.backend.UserDAO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * 로그인 처리 Command (디버깅용 로그 추가)
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

        System.out.println("로그인 시도 - username: '" + username + "', password: '" + password + "'");

        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
          request.setAttribute("error", "아이디와 비밀번호를 모두 입력해주세요.");
          return "/user/login.jsp";
        }

        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserByUsername(username.trim());

        if (user == null) {
          System.out.println("DB 조회 실패: 사용자 없음");
          request.setAttribute("error", "존재하지 않는 아이디입니다.");
          return "/user/login.jsp";
        }

        System.out.println("DB에서 조회됨 - username: " + user.getUsername() + ", password: " + user.getPassword());

        // MD5 해시 비교
        String hashedPassword = hashPassword(password);
        System.out.println("입력 비밀번호 MD5 해시: " + hashedPassword);

        if (!user.getPassword().equals(hashedPassword)) {
          System.out.println("비밀번호 불일치");
          request.setAttribute("error", "비밀번호가 일치하지 않습니다.");
          return "/user/login.jsp";
        }

        HttpSession session = request.getSession();
        session.setAttribute("user", user);

        System.out.println("로그인 성공: " + user.getUsername());

        response.sendRedirect(request.getContextPath() + "/");
        return null;
      }
    } catch (Exception e) {
      e.printStackTrace();
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
