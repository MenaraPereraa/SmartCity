package lk.menara.smartcity360.patterns.composite;

public class TestRun {
    public static void main(String[] args) {
        ZoneComponent b1 = new Building("Colombo Tower A", 150.5);
        ZoneComponent b2 = new Building("Colombo Mall B", 300.0);
        ZoneComponent b3 = new Building("Galle Fort Shop", 80.0);

        ZoneComposite colomboStreet = new ZoneComposite("Colpetty Street");
        colomboStreet.add(b1);
        colomboStreet.add(b2);

        ZoneComposite galleStreet = new ZoneComposite("Fort Street");
        galleStreet.add(b3);

        ZoneComposite colomboCity = new ZoneComposite("Colombo City");
        colomboCity.add(colomboStreet);
        colomboCity.add(galleStreet);

        colomboCity.executeCommand("SHUTDOWN_POWER");

        System.out.println("Total Colombo City Power Usage: " + colomboCity.getResourceUsage() + " kWh");
    }
}