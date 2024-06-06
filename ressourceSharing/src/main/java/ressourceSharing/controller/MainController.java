package ressourceSharing.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;
@Controller
public class MainController {
    @GetMapping("")
    public String AfficherHomePage(HttpSession session)
    {
        if (session.getAttribute("utilisateur") == null)
        {
            return "redirect:/login";
        }
        return "index";
    }

    @GetMapping("/emptySession")
    public ResponseEntity<Void> emptySession(HttpSession session)
    {
        session.removeAttribute("error");
        session.removeAttribute("success");
        return ResponseEntity.ok().build();
    }
}
