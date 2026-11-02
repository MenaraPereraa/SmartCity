<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/views/layout/header.jspf" %>

<div class="mx-auto max-w-6xl px-4 pb-12 pt-2 text-slate-800">

    <div class="mb-5 flex flex-wrap items-center justify-between gap-3">
        <div>
            <h1 class="text-2xl font-extrabold tracking-tight text-slate-900"><c:out value="${listTitle}"/></h1>
            <p class="mt-1 text-sm text-slate-500">
                <c:out value="${empty complaints ? 0 : complaints.size()}"/> complaints
            </p>
        </div>
        <c:if test="${sessionScope.user.resident}">
            <a href="${ctx}/app/complaints/new"
               class="inline-flex items-center gap-2 rounded-xl bg-blue-600 px-4 py-2.5 text-sm font-bold text-white shadow-sm hover:bg-blue-700 focus:outline-none focus-visible:ring-2 focus-visible:ring-blue-500 focus-visible:ring-offset-2">
                <i class="bi bi-plus-lg"></i> New complaint
            </a>
        </c:if>
    </div>

    <c:if test="${inboxTabs}">
        <div class="mb-4 inline-flex gap-0.5 rounded-xl bg-slate-200/70 p-1" role="tablist">
            <a href="${ctx}/app/inbox?view=open"
               class="rounded-lg px-4 py-1.5 text-sm font-semibold ${openTab ? 'bg-white text-slate-900 shadow-sm' : 'text-slate-500 hover:text-slate-900'}">Open</a>
            <a href="${ctx}/app/inbox?view=closed"
               class="rounded-lg px-4 py-1.5 text-sm font-semibold ${openTab ? 'text-slate-500 hover:text-slate-900' : 'bg-white text-slate-900 shadow-sm'}">Closed</a>
        </div>
    </c:if>

    <c:set var="rows" value="${complaints}"/>
    <%@ include file="/WEB-INF/views/layout/complaint-table.jspf" %>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jspf" %>
