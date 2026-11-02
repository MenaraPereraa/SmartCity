<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="layout/header.jspf" %>

<div class="mb-3">
    <h1 class="h2 mb-0 fw-bold">🗺 Zone Commands</h1>
    <small class="sc-pattern-badge">Composite Pattern</small>
    <p class="text-muted mt-1 mb-0">
        Commands propagate down the hierarchy:
        <span class="badge text-bg-primary">City</span>
        <i class="bi bi-arrow-right mx-1"></i>
        <span class="badge text-bg-info text-dark">District</span>
        <i class="bi bi-arrow-right mx-1"></i>
        <span class="badge text-bg-success">Street</span>
        <i class="bi bi-arrow-right mx-1"></i>
        <span class="badge text-bg-secondary">Building</span>
    </p>
</div>

<div class="row g-4">
    <%-- LEFT: Zone Hierarchy Tree --%>
    <div class="col-lg-5">
        <div class="card shadow-sm h-100">
            <div class="card-header d-flex align-items-center gap-2">
                <i class="bi bi-diagram-3 text-primary"></i>
                <span class="fw-semibold">Zone Hierarchy</span>
                <small class="ms-auto text-muted">Click to select</small>
            </div>
            <div class="card-body">
                <%-- ZoneTree.render() from app.js fills this --%>
                <div id="zoneTreeContainer" class="sc-zone-tree"></div>

                <input type="hidden" id="selectedZoneId">

                <div class="mt-3 pt-2 border-top">
                    <i class="bi bi-info-circle text-muted me-1"></i>
                    <span id="selectedZoneLabel" class="text-muted small">
                        Select a zone above to broadcast a command
                    </span>
                </div>
            </div>
        </div>
    </div>

    <%-- RIGHT: Command Panel + Logs --%>
    <div class="col-lg-7">
        <%-- Command Panel --%>
        <div class="card shadow-sm mb-4">
            <div class="card-header d-flex align-items-center gap-2">
                <i class="bi bi-broadcast text-warning"></i>
                <span class="fw-semibold">Broadcast Command</span>
            </div>
            <div class="card-body">
                <form method="post" action="${ctx}/app/zones" id="commandForm">
                    <input type="hidden" name="_csrf" value="<c:out value='${csrfToken}'/>">
                    <input type="hidden" name="commandZoneId" id="commandZoneId" value="">

                    <div class="mb-3">
                        <label class="form-label fw-medium" for="commandSelect">
                            <i class="bi bi-lightning me-1"></i>Command
                        </label>
                        <select class="form-select" id="commandSelect" name="command" required>
                            <option value="">— Select command —</option>
                            <option value="POWER_ON">
                                <i class="bi bi-power"></i> POWER_ON — Turn on all devices in zone
                            </option>
                            <option value="POWER_OFF">POWER_OFF — Turn off all devices in zone</option>
                            <option value="REPORT_USAGE">REPORT_USAGE — Request usage report</option>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-medium" for="buildingId">
                            <i class="bi bi-building me-1"></i>Building Filter
                            <span class="text-muted fw-normal">(optional)</span>
                        </label>
                        <select class="form-select" id="buildingId" name="buildingId">
                            <option value="">All buildings in selected zone</option>
                        </select>
                        <div class="form-text">
                            Leave blank to apply to all buildings in the zone and sub-zones.
                        </div>
                    </div>

                    <div id="commandZoneAlert" class="alert alert-warning py-2 d-none">
                        <i class="bi bi-exclamation-triangle me-1"></i>
                        Please select a zone from the tree before applying a command.
                    </div>

                    <button type="submit" class="btn btn-primary w-100">
                        <i class="bi bi-broadcast me-2"></i>Apply to Zone
                    </button>
                </form>
            </div>
        </div>

        <%-- Command Logs --%>
        <div class="card shadow-sm">
            <div class="card-header d-flex align-items-center gap-2">
                <i class="bi bi-clock-history text-secondary"></i>
                <span class="fw-semibold">Command Log</span>
                <span class="badge text-bg-secondary ms-auto">Last 20</span>
            </div>
            <div class="card-body p-0">
                <c:choose>
                    <c:when test="${empty commandLogs}">
                        <div class="p-4 text-center text-muted">
                            <i class="bi bi-inbox fs-2 d-block mb-2"></i>
                            No commands issued yet.
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="table-responsive" style="max-height: 360px; overflow-y: auto;">
                            <table class="table table-sm table-hover mb-0 align-middle">
                                <thead class="table-light sticky-top">
                                    <tr>
                                        <th class="ps-3" style="font-size:.75rem;">Time</th>
                                        <th style="font-size:.75rem;">Command</th>
                                        <th style="font-size:.75rem;">Zone</th>
                                        <th style="font-size:.75rem;">Building</th>
                                        <th style="font-size:.75rem;">Issued By</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="log" items="${commandLogs}">
                                        <tr>
                                            <td class="ps-3 text-muted" style="font-size:.75rem; white-space:nowrap;">
                                                <c:out value="${log.executedAt}"/>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${log.command eq 'POWER_ON'}">
                                                        <span class="badge text-bg-success" style="font-size:.75rem;">POWER_ON</span>
                                                    </c:when>
                                                    <c:when test="${log.command eq 'POWER_OFF'}">
                                                        <span class="badge text-bg-danger" style="font-size:.75rem;">POWER_OFF</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge text-bg-info text-dark" style="font-size:.75rem;">
                                                            <c:out value="${log.command}"/>
                                                        </span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td style="font-size:.8rem;">
                                                <c:out value="${log.zoneName}"/>
                                            </td>
                                            <td style="font-size:.8rem;" class="text-muted">
                                                <c:choose>
                                                    <c:when test="${not empty log.buildingName}">
                                                        <c:out value="${log.buildingName}"/>
                                                    </c:when>
                                                    <c:otherwise>All</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td style="font-size:.8rem;" class="text-muted">
                                                <c:out value="${log.userName}"/>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>

<%-- Embed zone data for ZoneTree in app.js --%>
<script type="application/json" id="zoneTreeData"><c:out value="${zonesJson}" escapeXml="false"/></script>

<script src="${ctx}/assets/zones.js"></script>
<%@ include file="layout/footer.jspf" %>
