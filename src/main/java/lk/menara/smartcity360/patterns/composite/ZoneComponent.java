package lk.menara.smartcity360.patterns.composite;

<<<<<<< HEAD
import lk.menara.smartcity360.model.enums.CommandType;

import java.util.List;

public interface ZoneComponent {
    String getName();

    double getResourceUsage();

    void executeCommand(CommandType command, List<String> log);
}
=======
public interface ZoneComponent {
    void executeCommand(String command);
    double getResourceUsage();
}
>>>>>>> main
