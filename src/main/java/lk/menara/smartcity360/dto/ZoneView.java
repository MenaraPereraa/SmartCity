package lk.menara.smartcity360.dto;

/**
 * View model for a zone row (used for JSON serialization in JSPs).
 */
public class ZoneView {

    private int id;
    private String level;
    private String name;
    private Integer parentId;

    public ZoneView() {}

    public ZoneView(int id, String level, String name, Integer parentId) {
        this.id = id;
        this.level = level;
        this.name = name;
        this.parentId = parentId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getParentId() { return parentId; }
    public void setParentId(Integer parentId) { this.parentId = parentId; }
}
