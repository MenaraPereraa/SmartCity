<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/views/layout/header.jspf" %>
<div class="d-flex justify-content-between align-items-center mb-3">
    <h1 class="h3 mb-0"><c:out value="${listTitle}"/></h1>
    <c:if test="${sessionScope.user.resident}">
        <a class="btn btn-primary" href="${ctx}/app/complaints/new">New complaint</a>
    </c:if>
</div>

<c:if test="${inboxTabs}">
    <ul class="nav nav-tabs mb-3">
        <li class="nav-item">
            <a class="nav-link ${openTab ? 'active' : ''}" href="${ctx}/app/inbox?view=open">Open</a>
        </li>
        <li class="nav-item">
            <a class="nav-link ${openTab ? '' : 'active'}" href="${ctx}/app/inbox?view=closed">Closed</a>
        </li>
    </ul>
</c:if>

<c:set var="rows" value="${complaints}"/>
<%@ include file="/WEB-INF/views/layout/complaint-table.jspf" %>
<%@ include file="/WEB-INF/views/layout/footer.jspf" %>
