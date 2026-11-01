<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/views/layout/header.jspf" %>
<h1 class="h3 mb-1">Welcome, <c:out value="${sessionScope.user.firstName}"/></h1>
<p class="text-muted mb-4"><c:out value="${stats.scopeLabel}"/></p>

<div class="row g-3 mb-4">
    <div class="col-6 col-lg-3">
        <div class="card sc-stat open"><div class="card-body">
            <div class="sc-stat-number"><c:out value="${stats.open}"/></div><div class="text-muted">Open</div>
        </div></div>
    </div>
    <div class="col-6 col-lg-3">
        <div class="card sc-stat escalated"><div class="card-body">
            <div class="sc-stat-number"><c:out value="${stats.escalated}"/></div><div class="text-muted">Escalated</div>
        </div></div>
    </div>
    <div class="col-6 col-lg-3">
        <div class="card sc-stat resolved"><div class="card-body">
            <div class="sc-stat-number"><c:out value="${stats.resolved}"/></div><div class="text-muted">Resolved</div>
        </div></div>
    </div>
    <div class="col-6 col-lg-3">
        <div class="card sc-stat unresolved"><div class="card-body">
            <div class="sc-stat-number"><c:out value="${stats.unresolved}"/></div><div class="text-muted">Unresolved</div>
        </div></div>
    </div>
</div>

<div class="d-flex justify-content-between align-items-center mb-2">
    <h2 class="h5 mb-0">
        <c:choose>
            <c:when test="${sessionScope.user.staff}">Waiting at your level</c:when>
            <c:otherwise>Recent complaints</c:otherwise>
        </c:choose>
    </h2>
    <c:if test="${sessionScope.user.resident}">
        <a class="btn btn-primary btn-sm" href="${ctx}/app/complaints/new">New complaint</a>
    </c:if>
</div>
<c:set var="rows" value="${stats.recent}"/>
<c:set var="showReporter" value="${sessionScope.user.staff}"/>
<c:set var="emptyText" value="Nothing to show yet."/>
<%@ include file="/WEB-INF/views/layout/complaint-table.jspf" %>
<%@ include file="/WEB-INF/views/layout/footer.jspf" %>
