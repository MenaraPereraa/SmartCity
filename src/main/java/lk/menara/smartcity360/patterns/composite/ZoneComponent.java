package lk.menara.smartcity360.patterns.composite;

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> da3b151e8a1de2211f81daef7f2563e22c505db6
import lk.menara.smartcity360.model.enums.CommandType;
import java.util.List;

public interface ZoneComponent {
    String getName();

    double getResourceUsage();

    void executeCommand(CommandType command, List<String> log);
<<<<<<< HEAD
}
=======
public interface ZoneComponent {
    void executeCommand(String command);
    double getResourceUsage();
}
>>>>>>> main
=======
}
>>>>>>> da3b151e8a1de2211f81daef7f2563e22c505db6
