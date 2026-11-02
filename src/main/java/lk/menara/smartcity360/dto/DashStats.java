package lk.menara.smartcity360.dto;

import java.util.List;

public class DashStats {

    private int open;
    private int escalated;
    private int resolved;
    private int unresolved;
    private List<ComplaintView> recent;
    private String scopeLabel;

    // Chart data, stored as valid JSON arrays (double quotes)
    private String trendLabels = "[]";
    private String submittedData = "[]";
    private String resolvedData = "[]";
    private String categoryLabels = "[]";
    private String categoryCounts = "[]";

    public DashStats() {}

    public int getOpen() { return open; }
    public void setOpen(int open) { this.open = open; }

    public int getEscalated() { return escalated; }
    public void setEscalated(int escalated) { this.escalated = escalated; }

    public int getResolved() { return resolved; }
    public void setResolved(int resolved) { this.resolved = resolved; }

    public int getUnresolved() { return unresolved; }
    public void setUnresolved(int unresolved) { this.unresolved = unresolved; }

    public List<ComplaintView> getRecent() { return recent; }
    public void setRecent(List<ComplaintView> recent) { this.recent = recent; }

    public String getScopeLabel() { return scopeLabel; }
    public void setScopeLabel(String scopeLabel) { this.scopeLabel = scopeLabel; }

    public String getTrendLabels() { return trendLabels; }
    public void setTrendLabels(String trendLabels) { this.trendLabels = trendLabels; }

    public String getSubmittedData() { return submittedData; }
    public void setSubmittedData(String submittedData) { this.submittedData = submittedData; }

    public String getResolvedData() { return resolvedData; }
    public void setResolvedData(String resolvedData) { this.resolvedData = resolvedData; }

    public String getCategoryLabels() { return categoryLabels; }
    public void setCategoryLabels(String categoryLabels) { this.categoryLabels = categoryLabels; }

    public String getCategoryCounts() { return categoryCounts; }
    public void setCategoryCounts(String categoryCounts) { this.categoryCounts = categoryCounts; }
}