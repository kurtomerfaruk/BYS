package tr.bel.gaziantep.bysweb.core.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 18.06.2025 10:01
 */
@Setter
@Getter
@Named
@ViewScoped
@Slf4j
public class MenuController implements java.io.Serializable{
    @Serial
    private static final long serialVersionUID = -4385432633087470640L;

    private String pageLink;

    public MenuController() {
        pageLink = "mainPage";
    }
}
