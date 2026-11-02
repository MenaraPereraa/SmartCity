<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/views/layout/header.jspf" %>

<div class="mx-auto max-w-6xl space-y-8">

    <header>
        <h1 class="text-2xl font-bold tracking-tight text-slate-900">Dashboard</h1>
        <p class="mt-1 text-sm text-slate-500">Welcome back, <c:out value="${sessionScope.user.firstName}"/></p>
    </header>

    <section class="grid grid-cols-1 gap-4 sm:grid-cols-3">
        <c:set var="statLabel" value="Open"/><c:set var="statValue" value="${stats.open}"/>
        <c:set var="statIcon" value="bi-inbox"/><c:set var="statChip" value="bg-indigo-50 text-indigo-600"/>
        <%@ include file="/WEB-INF/views/layout/stat-card.jspf" %>

        <c:set var="statLabel" value="Escalated to you"/><c:set var="statValue" value="${stats.escalated}"/>
        <c:set var="statIcon" value="bi-arrow-up-circle"/><c:set var="statChip" value="bg-amber-50 text-amber-600"/>
        <%@ include file="/WEB-INF/views/layout/stat-card.jspf" %>

        <c:set var="statLabel" value="Resolved"/><c:set var="statValue" value="${stats.resolved}"/>
        <c:set var="statIcon" value="bi-check-circle"/><c:set var="statChip" value="bg-emerald-50 text-emerald-600"/>
        <%@ include file="/WEB-INF/views/layout/stat-card.jspf" %>
    </section>

    <section class="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
        <h2 class="text-base font-semibold text-slate-900">Last 7 days</h2>
        <p class="mb-4 text-xs text-slate-500">Complaints submitted and resolved</p>
        <div class="relative h-60 w-full">
            <canvas id="complaintTrendChart"
                    data-labels="<c:out value='${stats.trendLabels}'/>"
                    data-submitted="<c:out value='${stats.submittedData}'/>"
                    data-resolved="<c:out value='${stats.resolvedData}'/>"></canvas>
        </div>
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

<script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.7/dist/chart.umd.min.js"></script>
<script src="${ctx}/assets/js/dashboard.js"></script>

<%@ include file="/WEB-INF/views/layout/footer.jspf" %>
