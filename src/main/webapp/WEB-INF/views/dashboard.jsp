<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/views/layout/header.jspf" %>

<div class="mx-auto max-w-7xl space-y-8">

    <%-- ── Dashboard Header ── --%>
    <div class="flex flex-wrap items-center justify-between gap-4">
        <div>
            <h1 class="text-2xl font-extrabold tracking-tight text-slate-900 flex items-center gap-2">
                <i class="bi bi-speedometer2 text-indigo-600"></i>
                Dashboard Overview
            </h1>
            <p class="text-sm text-slate-500 mt-1">
                Welcome back, <c:out value="${sessionScope.user.firstName}"/> &bull; <i class="bi bi-geo-alt me-1"></i><c:out value="${stats.scopeLabel}"/>
            </p>
        </div>
        <c:if test="${sessionScope.user.resident}">
            <a class="inline-flex items-center gap-2 rounded-xl bg-indigo-600 px-4 py-2.5 text-sm font-semibold text-white shadow-sm transition hover:bg-indigo-500" href="${ctx}/app/complaints/new">
                <i class="bi bi-plus-circle"></i>Report Problem
            </a>
        </c:if>
    </div>

    <%-- ── Stats Grid ── --%>
    <div class="grid grid-cols-2 gap-4 lg:grid-cols-4">
        <div class="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm border-l-4 border-l-indigo-600">
            <div class="text-xs font-semibold uppercase tracking-wider text-slate-400">Open</div>
            <div class="mt-2 text-3xl font-extrabold text-indigo-600"><c:out value="${stats.open}"/></div>
        </div>
        <div class="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm border-l-4 border-l-amber-500">
            <div class="text-xs font-semibold uppercase tracking-wider text-slate-400">Escalated</div>
            <div class="mt-2 text-3xl font-extrabold text-amber-500"><c:out value="${stats.escalated}"/></div>
        </div>
        <div class="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm border-l-4 border-l-emerald-500">
            <div class="text-xs font-semibold uppercase tracking-wider text-slate-400">Resolved</div>
            <div class="mt-2 text-3xl font-extrabold text-emerald-600"><c:out value="${stats.resolved}"/></div>
        </div>
        <div class="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm border-l-4 border-l-rose-500">
            <div class="text-xs font-semibold uppercase tracking-wider text-slate-400">Unresolved</div>
            <div class="mt-2 text-3xl font-extrabold text-rose-600"><c:out value="${stats.unresolved}"/></div>
        </div>
    </div>

    <%-- ── Charts Section (data comes from data-* attributes, drawn by assets/js/dashboard.js) ── --%>
    <div class="grid grid-cols-1 gap-6 lg:grid-cols-12">
        <div class="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm lg:col-span-8">
            <div class="mb-4">
                <h3 class="text-base font-bold text-slate-900">Complaint Activity Trend</h3>
                <p class="text-xs text-slate-500">Last 7 days: submitted vs resolved</p>
            </div>
            <div class="relative h-64 w-full">
                <canvas id="complaintTrendChart"
                        data-labels="<c:out value='${stats.trendLabels}'/>"
                        data-submitted="<c:out value='${stats.submittedData}'/>"
                        data-resolved="<c:out value='${stats.resolvedData}'/>"></canvas>
            </div>
        </div>

        <div class="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm lg:col-span-4 flex flex-col">
            <div class="mb-4">
                <h3 class="text-base font-bold text-slate-900">Status Breakdown</h3>
                <p class="text-xs text-slate-500">Current distribution</p>
            </div>
            <div class="relative h-56 w-full">
                <canvas id="complaintDonut"
                        data-open="${stats.open}"
                        data-escalated="${stats.escalated}"
                        data-resolved="${stats.resolved}"
                        data-unresolved="${stats.unresolved}"></canvas>
            </div>
        </div>
    </div>

    <div class="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
        <div class="mb-4">
            <h3 class="text-base font-bold text-slate-900">Complaints by Category</h3>
            <p class="text-xs text-slate-500">Top categories in your scope</p>
        </div>
        <div class="relative h-64 w-full">
            <canvas id="complaintCategoryChart"
                    data-labels="<c:out value='${stats.categoryLabels}'/>"
                    data-counts="<c:out value='${stats.categoryCounts}'/>"></canvas>
        </div>
    </div>

    <%-- ── Quick Actions (Staff Only) ── --%>
    <c:if test="${sessionScope.user.staff}">
        <div class="grid grid-cols-1 gap-4 md:grid-cols-3">
            <c:if test="${sessionScope.user.canAccessEnergy}">
                <a href="${ctx}/app/energy" class="flex items-center gap-4 rounded-2xl border border-slate-200 bg-white p-4 shadow-sm transition hover:border-indigo-200 hover:shadow-md">
                    <div class="grid h-12 w-12 flex-none place-items-center rounded-xl bg-indigo-50 text-indigo-600">
                        <i class="bi bi-lightning-charge-fill text-xl"></i>
                    </div>
                    <div>
                        <div class="font-bold text-slate-900">Energy Management</div>
                        <div class="text-xs text-slate-500">Meter readings &amp; streams</div>
                    </div>
                </a>
            </c:if>
            <c:if test="${sessionScope.user.canAccessStreetLights}">
                <a href="${ctx}/app/street-lights" class="flex items-center gap-4 rounded-2xl border border-slate-200 bg-white p-4 shadow-sm transition hover:border-amber-200 hover:shadow-md">
                    <div class="grid h-12 w-12 flex-none place-items-center rounded-xl bg-amber-50 text-amber-600">
                        <i class="bi bi-lightbulb-fill text-xl"></i>
                    </div>
                    <div>
                        <div class="font-bold text-slate-900">Street Lights</div>
                        <div class="text-xs text-slate-500">View &amp; control lights</div>
                    </div>
                </a>
            </c:if>
            <c:if test="${sessionScope.user.canAccessZoneCommand}">
                <a href="${ctx}/app/zones" class="flex items-center gap-4 rounded-2xl border border-slate-200 bg-white p-4 shadow-sm transition hover:border-emerald-200 hover:shadow-md">
                    <div class="grid h-12 w-12 flex-none place-items-center rounded-xl bg-emerald-50 text-emerald-600">
                        <i class="bi bi-diagram-3-fill text-xl"></i>
                    </div>
                    <div>
                        <div class="font-bold text-slate-900">Zone Commands</div>
                        <div class="text-xs text-slate-500">Broadcast to zone tree</div>
                    </div>
                </a>
            </c:if>
        </div>
    </c:if>

    <%-- ── Recent Complaints / Queue Table ── --%>
    <div class="rounded-2xl border border-slate-200 bg-white shadow-sm overflow-hidden">
        <div class="flex items-center justify-between border-b border-slate-100 px-6 py-4">
            <h3 class="font-bold text-slate-900 flex items-center gap-2">
                <i class="bi bi-clock-history text-indigo-600"></i>
                <c:choose>
                    <c:when test="${sessionScope.user.staff}">Waiting at Your Level</c:when>
                    <c:otherwise>Recent Complaints</c:otherwise>
                </c:choose>
            </h3>
            <div>
                <c:if test="${sessionScope.user.staff}">
                    <a href="${ctx}/app/inbox" class="text-sm font-semibold text-indigo-600 hover:text-indigo-500">View All</a>
                </c:if>
                <c:if test="${sessionScope.user.resident}">
                    <a href="${ctx}/app/my-complaints" class="text-sm font-semibold text-indigo-600 hover:text-indigo-500">View All</a>
                </c:if>
            </div>
        </div>
        <div class="p-0">
            <c:set var="rows" value="${stats.recent}"/>
            <c:set var="showReporter" value="${sessionScope.user.staff}"/>
            <c:set var="emptyText" value="Nothing to show yet."/>
            <%@ include file="/WEB-INF/views/layout/complaint-table.jspf" %>
        </div>
    </div>

    <%-- ── Feature map ── --%>
    <div class="pt-2">
        <%@ include file="/WEB-INF/views/layout/badge-map.jspf" %>
    </div>
</div>

<%-- Chart.js first, then our own script. Remove any other dashboard.js include from footer.jspf. --%>
<script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.7/dist/chart.umd.min.js"></script>
<script src="${ctx}/assets/js/dashboard.js"></script>

<%@ include file="/WEB-INF/views/layout/footer.jspf" %>
