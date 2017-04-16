package org.roundrobin.socket;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

/**
 * Servlet implementation class InitServlet
 */
@WebServlet(urlPatterns={"/InitServlet"},loadOnStartup=1)

public class InitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InitServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public void init(ServletConfig config) throws ServletException {
    	try {
			CategoriesPojo.loadProperties(config.getServletContext().getRealPath("WEB-INF/data/categories.properties"));
			System.out.println("Loaded Properties");
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

}
