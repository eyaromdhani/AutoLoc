package tn.esprit.autoloc.io;

public class RapportFactory {

    private RapportFactory() {
    }

    public static IRapport creer(Class<?> clazz) {
        if (clazz.isAnnotationPresent(RapportConsole.class)) {
            return new RapportConsoleImpl();
        }
        return new RapportSilencieux();
    }
}