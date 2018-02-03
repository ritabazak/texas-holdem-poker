package managers;

import javax.servlet.ServletContext;
import engine.PokerEngine;

public class EngineManager {
    public static PokerEngine getEngineFromContext(ServletContext context) {
        if (context.getAttribute("ENGINE") == null) {
            context.setAttribute("ENGINE", new PokerEngine());

        }

        return (PokerEngine) context.getAttribute("ENGINE");
    }
}