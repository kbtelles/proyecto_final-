package controlador;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "sr_login", urlPatterns = {"/sr_login"})
public class sr_login extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = request.getParameter("token");
        String usuario = request.getParameter("usuario");

        HttpSession sesion = request.getSession();
        sesion.setAttribute("jwt", token);
        sesion.setAttribute("usuario", usuario);

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
