package lk.menara.smartcity360.dto;

import java.util.List;

/**
 * View model for the complaint detail page.
 */
public class ComplaintDetailView {

    private ComplaintView view;
    private String zonePath;
    private List<EventView> timeline;
    private boolean canAct;
    private boolean canResolve;
    private boolean canEscalate;
    private boolean canMarkUnresolved;
    private String nextLevelName;

    public ComplaintDetailView() {}

    public ComplaintView getView() { return view; }
    public void setView(ComplaintView view) { this.view = view; }

    public String getZonePath() { return zonePath; }
    public void setZonePath(String zonePath) { this.zonePath = zonePath; }

    public List<EventView> getTimeline() { return timeline; }
    public void setTimeline(List<EventView> timeline) { this.timeline = timeline; }

    public boolean isCanAct() { return canAct; }
    public void setCanAct(boolean canAct) { this.canAct = canAct; }

    public boolean isCanResolve() { return canResolve; }
    public void setCanResolve(boolean canResolve) { this.canResolve = canResolve; }

    public boolean isCanEscalate() { return canEscalate; }
    public void setCanEscalate(boolean canEscalate) { this.canEscalate = canEscalate; }

    public boolean isCanMarkUnresolved() { return canMarkUnresolved; }
    public void setCanMarkUnresolved(boolean canMarkUnresolved) { this.canMarkUnresolved = canMarkUnresolved; }

    public String getNextLevelName() { return nextLevelName; }
    public void setNextLevelName(String nextLevelName) { this.nextLevelName = nextLevelName; }
}
