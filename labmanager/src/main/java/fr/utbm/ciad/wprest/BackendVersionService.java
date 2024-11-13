package fr.utbm.ciad.wprest;

import fr.utbm.ciad.labmanager.Constants;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BackendVersionService {

    /**
     * Gets the version of the backend API {@link Constants#MANAGER_MAJOR_VERSION}.
     * @return - the version of the backend
     */
    @GetMapping("/version")
    public String getVersion() {
        return Constants.MANAGER_MAJOR_VERSION;
    }
}
