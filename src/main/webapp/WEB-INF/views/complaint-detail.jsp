<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/views/layout/header.jspf" %>
<%@ include file="/WEB-INF/views/layout/badge-map.jspf" %>
<c:set var="v" value="${detail.view}"/>
<c:set var="stCls"  value="${empty badgeMap[v.statusBadge]   ? badgeDefault : badgeMap[v.statusBadge]}"/>
<c:set var="sevCls" value="${empty badgeMap[v.severityBadge] ? badgeDefault : badgeMap[v.severityBadge]}"/>

<div class="mx-auto max-w-6xl px-4 pb-12 pt-2 text-slate-800">

    <%-- Back link --%>
    <div class="mb-4">
        <c:choose>
            <c:when test="${sessionScope.user.staff}">
                <a href="${ctx}/app/inbox" class="inline-flex items-center gap-1.5 text-sm font-semibold text-slate-500 hover:text-slate-900">
                    <i class="bi bi-arrow-left"></i> Back to inbox
                </a>
            </c:when>
            <c:otherwise>
                <a href="${ctx}/app/my-complaints" class="inline-flex items-center gap-1.5 text-sm font-semibold text-slate-500 hover:text-slate-900">
                    <i class="bi bi-arrow-left"></i> Back to my complaints
                </a>
            </c:otherwise>
        </c:choose>
    </div>

    <%-- Title + badges --%>
    <div class="mb-6 flex flex-wrap items-center gap-3">
        <h1 class="text-2xl font-extrabold tracking-tight text-slate-900">Complaint #${v.id}</h1>
        <span class="inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-bold ring-1 ring-inset ${stCls}">
            <c:out value="${v.statusLabel}"/>
        </span>
        <span class="inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-bold ring-1 ring-inset ${sevCls}">
            <c:out value="${v.severityLabel}"/> (${v.severity})
        </span>
    </div>

    <div class="grid items-start gap-6 lg:grid-cols-[minmax(0,1fr)_380px]">

        <%-- LEFT: details + action --%>
        <div class="space-y-6">

            <section class="rounded-2xl border border-slate-200 bg-white p-6">
                <h2 class="mb-4 text-base font-extrabold">Details</h2>
                <dl class="grid gap-x-6 gap-y-4 sm:grid-cols-2">
                    <div>
                        <dt class="text-xs font-medium text-slate-500">Category</dt>
                        <dd class="mt-0.5 text-sm font-semibold"><c:out value="${v.categoryName}"/></dd>
                    </div>
                    <div>
                        <dt class="text-xs font-medium text-slate-500">Location</dt>
                        <dd class="mt-0.5 text-sm font-semibold"><c:out value="${detail.zonePath}"/></dd>
                    </div>
                    <div>
                        <dt class="text-xs font-medium text-slate-500">Reported by</dt>
                        <dd class="mt-0.5 text-sm font-semibold"><c:out value="${v.reporterName}"/></dd>
                    </div>
                    <div>
                        <dt class="text-xs font-medium text-slate-500">Currently with</dt>
                        <dd class="mt-0.5 text-sm font-semibold"><c:out value="${v.currentLevel}"/></dd>
                    </div>
                    <div>
                        <dt class="text-xs font-medium text-slate-500">Submitted</dt>
                        <dd class="mt-0.5 text-sm font-semibold"><c:out value="${v.createdAtText}"/></dd>
                    </div>
                    <c:if test="${not empty v.resolvedAtText}">
                        <div>
                            <dt class="text-xs font-medium text-slate-500">Resolved</dt>
                            <dd class="mt-0.5 text-sm font-semibold"><c:out value="${v.resolvedAtText}"/></dd>
                        </div>
                    </c:if>
                </dl>

                <div class="mt-6 border-t border-slate-100 pt-5">
                    <h3 class="mb-2 text-xs font-medium text-slate-500">Description</h3>
                    <p class="whitespace-pre-line break-words text-sm leading-relaxed text-slate-700"><c:out value="${v.description}"/></p>
                </div>
            </section>

            <c:if test="${detail.canAct}">
                <section class="rounded-2xl border border-slate-200 bg-white p-6">
                    <h2 class="mb-4 text-base font-extrabold">Your action</h2>
                    <form method="post" action="${ctx}/app/complaints/action" id="complaintActionForm">
                        <input type="hidden" name="_csrf" value="<c:out value='${csrfToken}'/>">
                        <input type="hidden" name="id" value="${v.id}">

                        <label for="remarks" class="mb-1 block text-sm font-bold">Note <span class="font-normal text-slate-400">(required)</span></label>
                        <textarea id="remarks" name="remarks" rows="3" maxlength="255" required
                                  placeholder="What did you do, or why are you taking this action?"
                                  class="w-full rounded-xl border border-slate-200 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"></textarea>

                        <div class="mt-4 flex flex-wrap gap-2">
                            <c:if test="${detail.canResolve}">
                                <button type="submit" name="action" value="resolve"
                                        class="inline-flex items-center gap-1.5 rounded-xl bg-emerald-600 px-4 py-2 text-sm font-bold text-white hover:bg-emerald-700 focus:outline-none focus-visible:ring-2 focus-visible:ring-emerald-500 focus-visible:ring-offset-2">
                                    <i class="bi bi-check2-circle"></i> Mark as resolved
                                </button>
                            </c:if>
                            <c:if test="${detail.canEscalate}">
                                <button type="submit" name="action" value="escalate"
                                        class="inline-flex items-center gap-1.5 rounded-xl bg-amber-500 px-4 py-2 text-sm font-bold text-white hover:bg-amber-600 focus:outline-none focus-visible:ring-2 focus-visible:ring-amber-500 focus-visible:ring-offset-2">
                                    <i class="bi bi-arrow-up-circle"></i> Escalate to <c:out value="${detail.nextLevelName}"/>
                                </button>
                            </c:if>
                            <c:if test="${detail.canMarkUnresolved}">
                                <button type="submit" name="action" value="unresolved"
                                        class="inline-flex items-center gap-1.5 rounded-xl border border-red-200 bg-white px-4 py-2 text-sm font-bold text-red-600 hover:bg-red-50 focus:outline-none focus-visible:ring-2 focus-visible:ring-red-500 focus-visible:ring-offset-2">
                                    <i class="bi bi-x-circle"></i> Mark unresolved
                                </button>
                            </c:if>
                        </div>
                    </form>
                </section>
            </c:if>
        </div>

        <%-- RIGHT: history timeline --%>
        <aside class="rounded-2xl border border-slate-200 bg-white p-6 lg:sticky lg:top-4">
            <h2 class="mb-5 text-base font-extrabold">History</h2>
            <ol>
                <c:forEach var="e" items="${detail.timeline}">
                    <c:set var="evCls" value="${empty badgeMap[e.badge] ? badgeDefault : badgeMap[e.badge]}"/>
                    <c:set var="evDot" value="${empty dotMap[e.badge]   ? 'border-slate-400' : dotMap[e.badge]}"/>
                    <li class="relative ml-1.5 border-l-2 border-slate-200 pb-6 pl-5 last:border-transparent last:pb-0">
                        <span class="absolute -left-[7px] top-1 h-3 w-3 rounded-full border-[3px] bg-white ${evDot}"></span>
                        <span class="inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-bold ring-1 ring-inset ${evCls}">
                            <c:out value="${e.title}"/>
                        </span>
                        <div class="mt-1.5 text-xs text-slate-500">
                            <c:out value="${e.actor}"/> &middot; <c:out value="${e.whenText}"/>
                        </div>
                        <c:if test="${not empty e.remarks}">
                            <p class="mt-2 whitespace-pre-line break-words rounded-lg bg-slate-50 px-3 py-2 text-sm text-slate-700">
                                <c:out value="${e.remarks}"/>
                            </p>
                        </c:if>
                    </li>
                </c:forEach>
            </ol>
        </aside>
    </div>
</div>

<script src="${ctx}/assets/complaint-detail.js"></script>
<%@ include file="/WEB-INF/views/layout/footer.jspf" %>
