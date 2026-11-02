<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/views/layout/header.jspf" %>

<div class="mx-auto max-w-6xl space-y-8">

    <header>
        <h1 class="text-2xl font-bold tracking-tight text-slate-900">Dashboard</h1>
        <p class="mt-1 text-sm text-slate-500">Welcome back, <c:out value="${sessionScope.user.firstName}"/></p>
    </header>

    <section class="grid grid-cols-1 gap-4 sm:grid-cols-2">
        <c:set var="statLabel" value="Waiting for you"/><c:set var="statValue" value="${stats.waiting}"/>
        <c:set var="statIcon" value="bi-hourglass-split"/><c:set var="statChip" value="bg-indigo-50 text-indigo-600"/>
        <%@ include file="/WEB-INF/views/layout/stat-card.jspf" %>

        <c:set var="statLabel" value="Resolved"/><c:set var="statValue" value="${stats.resolved}"/>
        <c:set var="statIcon" value="bi-check-circle"/><c:set var="statChip" value="bg-emerald-50 text-emerald-600"/>
        <%@ include file="/WEB-INF/views/layout/stat-card.jspf" %>
    </section>

    <section>
        <div class="mb-3 flex items-center justify-between">
            <h2 class="text-base font-semibold text-slate-900">Waiting at your level</h2>
            <a href="${ctx}/app/inbox" class="text-sm font-semibold text-indigo-600 hover:text-indigo-500">Open inbox <i class="bi bi-arrow-right"></i></a>
        </div>
        <c:set var="rows" value="${stats.recent}"/>
        <c:set var="showReporter" value="${true}"/>
        <c:set var="emptyText" value="No complaints are waiting for you."/>
        <%@ include file="/WEB-INF/views/layout/complaint-table.jspf" %>
    </section>

    <%@ include file="/WEB-INF/views/layout/module-links.jspf" %>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jspf" %>
