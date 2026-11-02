<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/views/layout/header.jspf" %>

<div class="mx-auto max-w-6xl space-y-8">

    <header class="flex flex-wrap items-end justify-between gap-4">
        <div>
            <h1 class="text-2xl font-bold tracking-tight text-slate-900">Dashboard</h1>
            <p class="mt-1 text-sm text-slate-500">Welcome back, <c:out value="${sessionScope.user.firstName}"/></p>
        </div>
        <a href="${ctx}/app/complaints/new"
           class="inline-flex items-center gap-2 rounded-lg bg-indigo-600 px-4 py-2.5 text-sm font-semibold text-white shadow-sm transition hover:bg-indigo-500">
            <i class="bi bi-plus-lg"></i> Report a problem
        </a>
    </header>

    <section class="grid grid-cols-1 gap-4 sm:grid-cols-3">
        <c:set var="statLabel" value="Open"/><c:set var="statValue" value="${stats.open}"/>
        <c:set var="statIcon" value="bi-inbox"/><c:set var="statChip" value="bg-indigo-50 text-indigo-600"/>
        <%@ include file="/WEB-INF/views/layout/stat-card.jspf" %>

        <c:set var="statLabel" value="Escalated"/><c:set var="statValue" value="${stats.escalated}"/>
        <c:set var="statIcon" value="bi-arrow-up-circle"/><c:set var="statChip" value="bg-amber-50 text-amber-600"/>
        <%@ include file="/WEB-INF/views/layout/stat-card.jspf" %>

        <c:set var="statLabel" value="Resolved"/><c:set var="statValue" value="${stats.resolved}"/>
        <c:set var="statIcon" value="bi-check-circle"/><c:set var="statChip" value="bg-emerald-50 text-emerald-600"/>
        <%@ include file="/WEB-INF/views/layout/stat-card.jspf" %>
    </section>

    <section>
        <div class="mb-3 flex items-center justify-between">
            <h2 class="text-base font-semibold text-slate-900">My recent complaints</h2>
            <a href="${ctx}/app/my-complaints" class="text-sm font-semibold text-indigo-600 hover:text-indigo-500">View all <i class="bi bi-arrow-right"></i></a>
        </div>
        <c:set var="rows" value="${stats.recent}"/>
        <c:set var="showReporter" value="${false}"/>
        <c:set var="emptyText" value="You haven't reported anything yet."/>
        <%@ include file="/WEB-INF/views/layout/complaint-table.jspf" %>
    </section>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jspf" %>
