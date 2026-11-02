package lk.menara.smartcity360.entity;


import javax.persistence.*;

@Entity
@Table(name = "zone")
public class Zone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false)
    private ZoneLevel level;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "parent_id")
    private Integer parentId;

    public enum ZoneLevel { CITY, DISTRICT, STREET }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public ZoneLevel getLevel() { return level; }
    public void setLevel(ZoneLevel level) { this.level = level; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getParentId() { return parentId; }
    public void setParentId(Integer parentId) { this.parentId = parentId; }
}
