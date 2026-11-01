<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/views/layout/header.jspf" %>
<c:set var="v" value="${detail.view}"/>
<div class="mb-3">
    <c:choose>
        <c:when test="${sessionScope.user.staff}"><a href="${ctx}/app/inbox">&larr; Back to inbox</a></c:when>
        <c:otherwise><a href="${ctx}/app/my-complaints">&larr; Back to my complaints</a></c:otherwise>
    </c:choose>
</div>

<div class="d-flex flex-wrap align-items-center gap-2 mb-3">
    <h1 class="h3 mb-0">Complaint #${v.id}</h1>
    <span class="badge text-bg-${v.statusBadge}"><c:out value="${v.statusLabel}"/></span>
    <span class="badge text-bg-${v.severityBadge}"><c:out value="${v.severityLabel}"/> (${v.severity})</span>
</div>

<div class="row g-4">
    <div class="col-lg-7">
        <div class="card shadow-sm mb-4">
            <div class="card-body">
                <h2 class="h6 text-uppercase text-muted">Details</h2>
                <dl class="row mb-3">
                    <dt class="col-sm-4">Category</dt><dd class="col-sm-8"><c:out value="${v.categoryName}"/></dd>
                    <dt class="col-sm-4">Location</dt><dd class="col-sm-8"><c:out value="${detail.zonePath}"/></dd>
                    <dt class="col-sm-4">Reported by</dt><dd class="col-sm-8"><c:out value="${v.reporterName}"/></dd>
                    <dt class="col-sm-4">Currently with</dt><dd class="col-sm-8"><c:out value="${v.currentLevel}"/></dd>
                    <dt class="col-sm-4">Submitted</dt><dd class="col-sm-8"><c:out value="${v.createdAtText}"/></dd>
                    <c:if test="${not empty v.resolvedAtText}">
                        <dt class="col-sm-4">Resolved</dt><dd class="col-sm-8"><c:out value="${v.resolvedAtText}"/></dd>
                    </c:if>
                </dl>
                <h2 class="h6 text-uppercase text-muted">Description</h2>
                <p class="sc-description mb-0"><c:out value="${v.description}"/></p>
            </div>
        </div>

        <c:if test="${detail.canAct}">
            <div class="card shadow-sm">
                <div class="card-body">
                    <h2 class="h6 text-uppercase text-muted">Your action</h2>
                    <form method="post" action="${ctx}/app/complaints/action">
                        <input type="hidden" name="_csrf" value="<c:out value='${csrfToken}'/>">
                        <input type="hidden" name="id" value="${v.id}">
                        <div class="mb-3">
                            <label class="form-label" for="remarks">Note (required)</label>
                            <textarea class="form-control" id="remarks" name="remarks" rows="3" maxlength="255" required></textarea>
                        </div>
                        <div class="d-flex flex-wrap gap-2">
                            <c:if test="${detail.canResolve}">
                                <button class="btn btn-success" type="submit" name="action" value="resolve">Mark as resolved</button>
                            </c:if>
                            <c:if test="${detail.canEscalate}">
                                <button class="btn btn-warning" type="submit" name="action" value="escalate">Escalate to <c:out value="${detail.nextLevelName}"/></button>
                            </c:if>
                            <c:if test="${detail.canMarkUnresolved}">
                                <button class="btn btn-outline-danger" type="submit" name="action" value="unresolved">Mark unresolved</button>
                            </c:if>
                        </div>
                    </form>
                </div>
            </div>
        </c:if>
    </div>

    <div class="col-lg-5">
        <div class="card shadow-sm">
            <div class="card-body">
                <h2 class="h6 text-uppercase text-muted mb-3">History</h2>
                <ol class="sc-timeline">
                    <c:forEach var="e" items="${detail.timeline}">
                        <li>
                            <span class="badge text-bg-${e.badge}"><c:out value="${e.title}"/></span>
                            <div class="small text-muted"><c:out value="${e.actor}"/> &middot; <c:out value="${e.whenText}"/></div>
                            <c:if test="${not empty e.remarks}"><div class="sc-description"><c:out value="${e.remarks}"/></div></c:if>
                        </li>
                    </c:forEach>
                </ol>
            </div>
        </div>
    </div>
</div>
<%@ include file="/WEB-INF/views/layout/footer.jspf" %>
