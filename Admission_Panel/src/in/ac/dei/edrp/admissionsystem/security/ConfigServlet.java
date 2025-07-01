package in.ac.dei.edrp.admissionsystem.security;


import java.io.*;

import javax.servlet.http.HttpServlet;

public class ConfigServlet extends HttpServlet {
    public void init() {
        try {
            InputStream input = getServletContext().getResourceAsStream("/WEB-INF/config.properties");
            if (input != null) {
                ConfigUtil.loadProperties(input);
                System.out.println("Properties loaded successfully");
            } else {
                throw new FileNotFoundException("config.properties not found!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}