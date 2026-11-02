<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/views/layout/header.jspf" %>

<div class="mx-auto max-w-2xl px-4 pb-12 pt-2 text-slate-800">

    <a href="${ctx}/app/my-complaints" class="mb-4 inline-flex items-center gap-1.5 text-sm font-semibold text-slate-500 hover:text-slate-900">
        <i class="bi bi-arrow-left"></i> Back to my complaints
    </a>

    <div class="rounded-2xl border border-slate-200 bg-white p-6 sm:p-8">
        <h1 class="text-2xl font-extrabold tracking-tight text-slate-900">Report a problem</h1>
        <p class="mt-1 text-sm text-slate-500">Tell us what is wrong and where. We will send it to the right team.</p>

        <c:if test="${not empty error}">
            <div class="mt-5 flex items-start gap-2 rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700" role="alert">
                <i class="bi bi-exclamation-triangle-fill mt-0.5"></i>
                <span><c:out value="${error}"/></span>
            </div>
        </c:if>

        <form method="post" action="${ctx}/app/complaints/new" class="mt-6 space-y-6">
            <input type="hidden" name="_csrf" value="<c:out value='${csrfToken}'/>">

            <div class="grid gap-4 sm:grid-cols-2">
                <div>
                    <label for="categoryId" class="mb-1 block text-sm font-bold">Category</label>
                    <select id="categoryId" name="categoryId" required
                            class="w-full rounded-xl border border-slate-200 bg-white px-3 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500">
                        <option value="">Choose...</option>
                        <c:forEach var="o" items="${categories}">
                            <option value="${o.value}" ${o.value eq param.categoryId ? 'selected' : ''}><c:out value="${o.label}"/></option>
                        </c:forEach>
                    </select>
                </div>
                <div>
                    <label for="zoneId" class="mb-1 block text-sm font-bold">Where is it?</label>
                    <select id="zoneId" name="zoneId" required
                            class="w-full rounded-xl border border-slate-200 bg-white px-3 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500">
                        <option value="">Choose...</option>
                        <c:forEach var="o" items="${zones}">
                            <option value="${o.value}" ${o.value eq param.zoneId ? 'selected' : ''}><c:out value="${o.label}"/></option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <fieldset>
                <legend class="mb-2 text-sm font-bold">How urgent is it?</legend>
                <div class="grid gap-3 sm:grid-cols-3">

                    <label class="cursor-pointer">
                        <input class="peer sr-only" type="radio" name="urgency" id="urgMinor" value="MINOR"
                        ${(empty param.urgency or param.urgency eq 'MINOR') ? 'checked' : ''}>
                        <div class="h-full rounded-xl border border-slate-200 p-4 transition hover:border-slate-300 peer-checked:border-emerald-500 peer-checked:bg-emerald-50 peer-checked:ring-2 peer-checked:ring-emerald-500/30 peer-focus-visible:ring-2 peer-focus-visible:ring-blue-500">
                            <div class="flex items-center gap-2 text-sm font-bold"><span class="h-2.5 w-2.5 rounded-full bg-emerald-500"></span>Minor</div>
                            <p class="mt-1 text-xs text-slate-500">Routine. Handled by local staff.</p>
                        </div>
                    </label>

                    <label class="cursor-pointer">
                        <input class="peer sr-only" type="radio" name="urgency" id="urgSerious" value="SERIOUS"
                        ${param.urgency eq 'SERIOUS' ? 'checked' : ''}>
                        <div class="h-full rounded-xl border border-slate-200 p-4 transition hover:border-slate-300 peer-checked:border-amber-500 peer-checked:bg-amber-50 peer-checked:ring-2 peer-checked:ring-amber-500/30 peer-focus-visible:ring-2 peer-focus-visible:ring-blue-500">
                            <div class="flex items-center gap-2 text-sm font-bold"><span class="h-2.5 w-2.5 rounded-full bg-amber-500"></span>Serious</div>
                            <p class="mt-1 text-xs text-slate-500">Needs a supervisor.</p>
                        </div>
                    </label>

                    <label class="cursor-pointer">
                        <input class="peer sr-only" type="radio" name="urgency" id="urgCritical" value="CRITICAL"
                        ${param.urgency eq 'CRITICAL' ? 'checked' : ''}>
                        <div class="h-full rounded-xl border border-slate-200 p-4 transition hover:border-slate-300 peer-checked:border-red-500 peer-checked:bg-red-50 peer-checked:ring-2 peer-checked:ring-red-500/30 peer-focus-visible:ring-2 peer-focus-visible:ring-blue-500">
                            <div class="flex items-center gap-2 text-sm font-bold"><span class="h-2.5 w-2.5 rounded-full bg-red-500"></span>Critical</div>
                            <p class="mt-1 text-xs text-slate-500">Danger or major damage. Goes to the city manager.</p>
                        </div>
                    </label>
                </div>
            </fieldset>

            <div>
                <label for="description" class="mb-1 block text-sm font-bold">Describe the problem</label>
                <textarea id="description" name="description" rows="5" maxlength="${maxDescription}" required
                          class="w-full rounded-xl border border-slate-200 px-3 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"><c:out value="${param.description}"/></textarea>
                <p class="mt-1 text-xs text-slate-500">
                    <span id="descCount">0</span> / ${maxDescription} characters
                </p>
            </div>

            <div class="flex flex-wrap items-center gap-3 border-t border-slate-100 pt-5">
                <button type="submit"
                        class="inline-flex items-center gap-2 rounded-xl bg-blue-600 px-5 py-2.5 text-sm font-bold text-white hover:bg-blue-700 focus:outline-none focus-visible:ring-2 focus-visible:ring-blue-500 focus-visible:ring-offset-2">
                    <i class="bi bi-send"></i> Submit complaint
                </button>
                <a href="${ctx}/app/my-complaints" class="rounded-xl px-4 py-2.5 text-sm font-bold text-slate-500 hover:bg-slate-100 hover:text-slate-900">Cancel</a>
            </div>
        </form>
    </div>
</div>

<script>
    (function () {
        var box = document.getElementById('description');
        var out = document.getElementById('descCount');
        if (!box || !out) return;
        var update = function () { out.textContent = box.value.length; };
        box.addEventListener('input', update);
        update();
    })();
</script>
<%@ include file="/WEB-INF/views/layout/footer.jspf" %>
