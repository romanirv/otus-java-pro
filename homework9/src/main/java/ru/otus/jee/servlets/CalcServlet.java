package ru.otus.jee.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CalcServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(CalcServlet.class);

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().printf("<html><body>");
        resp.getWriter().println("Calc servlet");
        resp.getWriter().printf("</body></html>");
        resp.getWriter().close();
    }

    @Override
    public void init() throws ServletException {
        logger.debug("First servlet initialized");
    }
}