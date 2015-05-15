package at.yawk.mojangnames;

import at.yawk.mojangapi.EndpointProvider;
import at.yawk.mojangapi.NameHistory;
import at.yawk.mojangapi.Profile;
import java.io.IOException;
import java.time.Instant;
import java.util.UUID;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author yawkat
 */
@Controller
public class Sites {
    @Autowired EndpointProvider endpointProvider;

    @RequestMapping("/{uuid:[0-9a-f]{32}}")
    void noDashesUUID(HttpServletResponse response, @PathVariable String uuid) throws IOException {
        String withDashes =
                uuid.substring(0, 8) + '-' +
                uuid.substring(8, 12) + '-' +
                uuid.substring(12, 16) + '-' +
                uuid.substring(16, 20) + '-' +
                uuid.substring(20, 32);
        response.sendRedirect(withDashes);
    }

    @RequestMapping("/{uuid:[0-9a-f]{8}-(?:[0-9a-f]{4}-){3}[0-9a-f]{12}}")
    ModelAndView history(@PathVariable UUID uuid) throws IOException, InterruptedException {
        NameHistory history = endpointProvider.nameHistory().call(uuid);
        return new ModelAndView("name_history")
                .addObject("history", history)
                .addObject("uuid", uuid)
                .addObject("uuid_no_dashes", uuid.toString().replace("-", ""));
    }

    @RequestMapping("/{name:\\w+}")
    ModelAndView name(@PathVariable String name) throws IOException, InterruptedException {
        Profile profile = endpointProvider.profileByName().call(name);
        if (profile == null) { throw new NoSuchUserException(); }
        return new ModelAndView("profile")
                .addObject("profile", profile)
                .addObject("name", name)
                .addObject("time", Instant.now().getEpochSecond());
    }
}
