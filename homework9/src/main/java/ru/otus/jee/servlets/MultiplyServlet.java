package ru.otus.jee.servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class MultiplyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        try {
            int operand1 = Integer.parseInt(req.getParameter("operand1"));
            int operand2 = Integer.parseInt(req.getParameter("operand2"));
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println(operand1 * operand2);
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

    }
}
