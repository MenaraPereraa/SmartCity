<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="layout/header.jspf" %>

<div class="mb-3">
    <h1 class="h2 mb-0 fw-bold">🛡 City Policies</h1>
    <small class="sc-pattern-badge">Interpreter Pattern</small>
</div>

<div class="row g-4">
    <%-- LEFT: Add New Policy Rule --%>
    <div class="col-lg-5">
        <div class="card shadow-sm">
            <div class="card-header d-flex align-items-center gap-2">
                <i class="bi bi-plus-circle text-success"></i>
                <span class="fw-semibold">Define Policy Rule</span>
            </div>
            <div class="card-body">
                <form method="post" action="${ctx}/app/policy" id="policyForm">
                    <input type="hidden" name="_csrf" value="<c:out value='${csrfToken}'/>">
                    <input type="hidden" name="action" value="add">

                    <div class="mb-3">
                        <label class="form-label fw-medium" for="name">
                            <i class="bi bi-tag me-1"></i>Rule Name
                        </label>
                        <input type="text" class="form-control" id="name" name="name"
                               maxlength="100" placeholder="e.g. Noise Curfew Rule" required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-medium" for="zoneId">
                            <i class="bi bi-diagram-3 me-1"></i>Zone
                        </label>
                        <select class="form-select" id="zoneId" name="zoneId" required>
                            <option value="">— Select zone —</option>
                            <c:forEach var="zone" items="${zones}">
                                <option value="<c:out value='${zone.id}'/>">
                                    <c:out value="${zone.name}"/>
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="mb-2">
                        <label class="form-label fw-medium" for="expression">
                            <i class="bi bi-code-slash me-1"></i>Policy Expression (DSL)
                        </label>
                        <textarea class="form-control font-monospace" id="expression" name="expression"
                                  rows="3" placeholder="noise_level > 65" required
                                  style="font-size:.875rem;"></textarea>
                        <div id="expressionFeedback" class="form-text"></div>
                    </div>

                    <%-- Example expression buttons --%>
                    <div class="mb-3">
                        <div class="small text-muted mb-1 fw-medium">
                            <i class="bi bi-lightbulb me-1"></i>Examples (click to insert):
                        </div>
                        <div class="d-flex flex-wrap gap-1">
                            <button type="button" class="btn btn-outline-secondary btn-sm"
                                    data-expr="noise_level > 65">
                                noise_level &gt; 65
                            </button>
                            <button type="button" class="btn btn-outline-secondary btn-sm"
                                    data-expr="parking_duration > 120 AND zone_type == residential">
                                parking + zone
                            </button>
                            <button type="button" class="btn btn-outline-secondary btn-sm"
                                    data-expr="energy_usage > 500 OR voltage_drop > 20">
                                energy OR voltage
                            </button>
                            <button type="button" class="btn btn-outline-secondary btn-sm"
                                    data-expr="air_quality < 50 AND time_of_day == night">
                                air quality
                            </button>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-medium" for="violationMessage">
                            <i class="bi bi-exclamation-triangle me-1"></i>Violation Message
                            <span class="text-muted fw-normal">(optional)</span>
                        </label>
                        <input type="text" class="form-control" id="violationMessage" name="violationMessage"
                               maxlength="255" placeholder="e.g. Noise limit exceeded in residential zone">
                    </div>

                    <button type="submit" class="btn btn-success w-100 fw-semibold">
                        <i class="bi bi-shield-plus me-2"></i>Save Rule
                    </button>
                </form>
            </div>
        </div>
    </div>

    <%-- RIGHT: Policy Rules Table --%>
    <div class="col-lg-7">
        <div class="card shadow-sm">
            <div class="card-header d-flex align-items-center gap-2">
                <i class="bi bi-shield-check text-primary"></i>
                <span class="fw-semibold">Active Policy Rules</span>
                <span class="badge text-bg-secondary ms-auto">${fn:length(rules)} rules</span>
            </div>
            <div class="card-body p-0">
                <c:choose>
                    <c:when test="${empty rules}">
                        <div class="p-5 text-center text-muted">
                            <i class="bi bi-shield-exclamation fs-1 d-block mb-3 opacity-25"></i>
                            <p class="mb-1">No policy rules defined yet.</p>
                            <small>Use the form on the left to add your first rule.</small>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="table-responsive">
                            <table class="table table-hover mb-0 align-middle">
                                <thead class="table-light">
                                    <tr>
                                        <th class="ps-3">Name</th>
                                        <th>Zone</th>
                                        <th>Expression</th>
                                        <th>Status</th>
                                        <th class="text-end pe-3">Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="rule" items="${rules}">
                                        <tr>
                                            <td class="ps-3">
                                                <div class="fw-semibold"><c:out value="${rule.name}"/></div>
                                                <div class="small text-muted"><c:out value="${rule.createdAt}"/></div>
                                            </td>
                                            <td class="small text-muted">
                                                <i class="bi bi-diagram-3 me-1"></i>
                                                <c:out value="${rule.zoneName}"/>
                                            </td>
                                            <td style="max-width: 200px;">
                                                <code class="sc-policy-expr d-block text-break" style="font-size:.78rem;">
                                                    <c:out value="${rule.expression}"/>
                                                </code>
                                                <c:if test="${not empty rule.violationMessage}">
                                                    <div class="small text-muted mt-1 fst-italic">
                                                        <i class="bi bi-exclamation-triangle me-1"></i>
                                                        <c:out value="${rule.violationMessage}"/>
                                                    </div>
                                                </c:if>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${rule.active}">
                                                        <span class="sc-policy-active badge">
                                                            <i class="bi bi-check-circle me-1"></i>Active
                                                        </span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="sc-policy-inactive badge">
                                                            <i class="bi bi-pause-circle me-1"></i>Inactive
                                                        </span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td class="text-end pe-3">
                                                <div class="d-flex gap-1 justify-content-end">
                                                    <%-- Toggle --%>
                                                    <form method="post" action="${ctx}/app/policy" class="d-inline">
                                                        <input type="hidden" name="_csrf" value="<c:out value='${csrfToken}'/>">
                                                        <input type="hidden" name="action" value="toggle">
                                                        <input type="hidden" name="ruleId" value="${rule.id}">
                                                        <button type="submit" class="btn btn-sm ${rule.active ? 'btn-outline-warning' : 'btn-outline-success'}"
                                                                title="${rule.active ? 'Deactivate' : 'Activate'}">
                                                            <i class="bi ${rule.active ? 'bi-pause-fill' : 'bi-play-fill'}"></i>
                                                        </button>
                                                    </form>
                                                    <%-- Delete --%>
                                                    <form method="post" action="${ctx}/app/policy" class="d-inline">
                                                        <input type="hidden" name="_csrf" value="<c:out value='${csrfToken}'/>">
                                                        <input type="hidden" name="action" value="delete">
                                                        <input type="hidden" name="ruleId" value="${rule.id}">
                                                        <button type="submit" class="btn btn-sm btn-outline-danger"
                                                                title="Delete rule"
                                                                onclick="return confirmAction('Delete policy rule \'<c:out value="${rule.name}"/>\' permanently?')">
                                                            <i class="bi bi-trash3"></i>
                                                        </button>
                                                    </form>
                                                </div>
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

<script src="${ctx}/assets/policy.js"></script>
<%@ include file="layout/footer.jspf" %>
