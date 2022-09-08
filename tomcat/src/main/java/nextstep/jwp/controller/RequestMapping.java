package nextstep.jwp.controller;

import java.util.stream.Stream;

public enum RequestMapping {

    INDEX("/", new IndexController()),
    LOGIN("/login", new LoginController()),
    REGISTER("/register", new RegisterController()),
    ;

    private static final ResourceController DEFAULT_CONTROLLER = new ResourceController();

    private final String path;
    private final Controller controller;

    RequestMapping(final String path, final Controller controller) {
        this.path = path;
        this.controller = controller;
    }

    public static Controller from(final String path) {
        return Stream.of(values())
                .filter(mapping -> mapping.equalsPath(path))
                .findAny()
                .map(mapping -> mapping.controller)
                .orElse(DEFAULT_CONTROLLER);
    }

    private boolean equalsPath(final String other) {
        return path.equals(other);
    }
}