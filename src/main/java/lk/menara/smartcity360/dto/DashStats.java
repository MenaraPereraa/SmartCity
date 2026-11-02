package lk.menara.smartcity360.dto;

import java.util.List;

public class DashStats {

    private int open;
    private int escalated;
    private int resolved;
    private int unresolved;
    private List<ComplaintView> recent;

    // Only filled for roles that show a trend chart (valid JSON arrays)
    private String trendLabels = "[]";
    private String submittedData = "[]";
    private String resolvedData = "[]";

    public DashStats() {}

    public int getOpen() { return open; }
    public void setOpen(int open) { this.open = open; }

    public int getEscalated() { return escalated; }
    public void setEscalated(int escalated) { this.escalated = escalated; }

    public int getResolved() { return resolved; }
    public void setResolved(int resolved) { this.resolved = resolved; }

    public int getUnresolved() { return unresolved; }
    public void setUnresolved(int unresolved) { this.unresolved = unresolved; }

    /** Everything still waiting for someone to act on it. */
    public int getWaiting() { return open + escalated; }

    public List<ComplaintView> getRecent() { return recent; }
    public void setRecent(List<ComplaintView> recent) { this.recent = recent; }

    public String getTrendLabels() { return trendLabels; }
    public void setTrendLabels(String trendLabels) { this.trendLabels = trendLabels; }

    public String getSubmittedData() { return submittedData; }
    public void setSubmittedData(String submittedData) { this.submittedData = submittedData; }

    public String getResolvedData() { return resolvedData; }
    public void setResolvedData(String resolvedData) { this.resolvedData = resolvedData; }
}