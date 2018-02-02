package managers;

import javax.servlet.ServletContext;

public class UserManager {


    public static UserManager getUserManagerFromContext(ServletContext context) {
        if (context.getAttribute("USER_MANAGER") == null) {
            context.setAttribute("USER_MANAGER", new UserManager());
        }

        return (UserManager)context.getAttribute("USER_MANAGER");
    }
}
