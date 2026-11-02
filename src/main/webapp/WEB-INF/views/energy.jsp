<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ include file="layout/header.jspf" %>

<c:set var="activeCount" value="0"/>
<c:set var="kwhTotal" value="0"/>
<c:forEach var="meter" items="${meters}">
    <c:if test="${meter.active}"><c:set var="activeCount" value="${activeCount + 1}"/></c:if>
    <c:if test="${not empty meter.latestKwh}"><c:set var="kwhTotal" value="${kwhTotal + meter.latestKwh}"/></c:if>
</c:forEach>

<%-- Chart library (remove this line if your footer already loads Chart.js) --%>
<script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.1/dist/chart.umd.min.js"></script>

<div class="mx-auto max-w-6xl pb-16 text-slate-800">

    <%-- ───────── Header ───────── --%>
    <header class="mb-8 flex flex-wrap items-end justify-between gap-4">
        <div>
            <h1 class="text-3xl font-extrabold tracking-tight text-slate-900">Energy</h1>
            <p class="mt-1 text-slate-500">Record readings and send meter data through secure layers.</p>
            <p class="mt-3 flex flex-wrap items-center gap-x-4 gap-y-1 text-sm text-slate-500">
                <span><b class="font-bold text-slate-800" id="statTotal">${empty meters ? 0 : meters.size()}</b> meters</span>
                <span><b class="font-bold text-emerald-600" id="statActive">${activeCount}</b> active</span>
                <span><b class="font-bold text-slate-800" id="statKwh"><fmt:formatNumber value="${kwhTotal}" maxFractionDigits="1"/></b> kWh in total</span>
                <span class="inline-flex items-center gap-1.5 rounded-full bg-violet-50 px-2.5 py-0.5 text-xs font-semibold text-violet-700 ring-1 ring-inset ring-violet-600/10">
                    <i class="bi bi-layers"></i>
                </span>
            </p>
        </div>
        <button type="button" id="openAddMeter"
                class="inline-flex items-center gap-2 rounded-xl border border-slate-200 bg-white px-4 py-2.5 text-sm font-bold shadow-sm hover:bg-slate-50 focus:outline-none focus-visible:ring-2 focus-visible:ring-indigo-400">
            <i class="bi bi-plus-lg"></i> Add meter
        </button>
    </header>

    <%-- ───────── Message after an action ───────── --%>
    <c:if test="${not empty flash}">
        <div id="flash" role="status"
             class="mb-6 flex items-start gap-2 rounded-2xl border px-4 py-3 text-sm ${flash.type eq 'error' ? 'border-red-200 bg-red-50 text-red-700' : 'border-emerald-200 bg-emerald-50 text-emerald-700'}">
            <i class="bi ${flash.type eq 'error' ? 'bi-exclamation-circle-fill' : 'bi-check-circle-fill'} mt-0.5"></i>
            <span><c:out value="${flash.text}"/></span>
        </div>
    </c:if>

    <%-- ───────── Stream lab ───────── --%>
    <section id="lab" class="rounded-3xl border border-slate-200/80 bg-white p-4 shadow-[0_1px_2px_rgba(15,23,42,.04),0_16px_40px_-20px_rgba(15,23,42,.18)] sm:p-5">
        <form method="post" action="${ctx}/app/energy" id="labForm">
            <input type="hidden" name="_csrf" value="<c:out value='${csrfToken}'/>">
            <input type="hidden" name="action" value="simulateStream">

            <div class="mb-3 flex items-center gap-3">
                <span class="grid h-9 w-9 place-items-center rounded-xl bg-gradient-to-br from-indigo-500 to-violet-500 text-white shadow-md shadow-indigo-500/30">
                    <i class="bi bi-stars"></i>
                </span>
                <div>
                    <h2 class="text-sm font-extrabold">Stream lab</h2>
                    <p class="text-xs text-slate-500">Pick a meter and the layers to wrap around its data. Then run it.</p>
                </div>
            </div>

            <label for="labPayload" class="sr-only">Meter data</label>
            <textarea id="labPayload" name="payload" rows="3"
                      placeholder="Paste meter data here. Leave it empty to use the latest reading."
                      class="w-full resize-none rounded-2xl border-0 bg-slate-50 px-4 py-3 text-sm placeholder:text-slate-400 focus:bg-white focus:outline-none focus:ring-2 focus:ring-indigo-400"></textarea>

            <div class="mt-3 flex flex-wrap items-center gap-2">
                <label for="labMeter" class="sr-only">Meter</label>
                <select id="labMeter" name="meterId" required
                        class="rounded-full border border-slate-200 bg-white py-1.5 pl-3 pr-8 text-sm font-semibold text-slate-700 focus:outline-none focus:ring-2 focus:ring-indigo-400">
                    <c:forEach var="meter" items="${meters}">
                        <option value="${meter.id}">
                            <c:out value="${meter.meterCode}"/> · <c:out value="${meter.buildingName}"/><c:if test="${not meter.active}"> (inactive)</c:if>
                        </option>
                    </c:forEach>
                </select>

                <label class="inline-flex cursor-pointer select-none items-center gap-1.5 rounded-full border border-slate-200 bg-white px-3 py-1.5 text-sm font-semibold text-slate-600 transition hover:border-slate-300 has-[:checked]:border-indigo-300 has-[:checked]:bg-indigo-50 has-[:checked]:text-indigo-700 has-[:focus-visible]:ring-2 has-[:focus-visible]:ring-indigo-400">
                    <input type="checkbox" name="compress" value="true" data-layer="Compression" class="sr-only" checked>
                    <i class="bi bi-file-zip"></i> Compression
                </label>
                <label class="inline-flex cursor-pointer select-none items-center gap-1.5 rounded-full border border-slate-200 bg-white px-3 py-1.5 text-sm font-semibold text-slate-600 transition hover:border-slate-300 has-[:checked]:border-indigo-300 has-[:checked]:bg-indigo-50 has-[:checked]:text-indigo-700 has-[:focus-visible]:ring-2 has-[:focus-visible]:ring-indigo-400">
                    <input type="checkbox" name="encrypt" value="true" data-layer="Encryption" class="sr-only" checked>
                    <i class="bi bi-shield-lock"></i> Encryption
                </label>
                <label class="inline-flex cursor-pointer select-none items-center gap-1.5 rounded-full border border-slate-200 bg-white px-3 py-1.5 text-sm font-semibold text-slate-600 transition hover:border-slate-300 has-[:checked]:border-indigo-300 has-[:checked]:bg-indigo-50 has-[:checked]:text-indigo-700 has-[:focus-visible]:ring-2 has-[:focus-visible]:ring-indigo-400">
                    <input type="checkbox" name="authenticate" value="true" data-layer="Authentication" class="sr-only" checked>
                    <i class="bi bi-person-check"></i> Authentication
                </label>

                <span class="flex-1"></span>

                <button type="submit" id="labRun"
                        class="inline-flex items-center gap-2 rounded-full bg-gradient-to-r from-indigo-500 to-violet-500 px-5 py-2 text-sm font-bold text-white shadow-md shadow-indigo-500/30 transition hover:brightness-110 focus:outline-none focus-visible:ring-2 focus-visible:ring-indigo-400 focus-visible:ring-offset-2 disabled:opacity-60">
                    <i class="bi bi-send-fill"></i> Run
                </button>
            </div>

            <%-- Token (only for the Authentication layer) --%>
            <div id="tokenRow" class="mt-3 flex flex-wrap items-center gap-2 rounded-2xl bg-slate-50 px-3 py-2">
                <i class="bi bi-key text-slate-400"></i>
                <label for="labToken" class="text-sm font-semibold text-slate-600">Access token</label>
                <input type="text" id="labToken" name="token" autocomplete="off" placeholder="Enter the token" value="<c:out value='${streamTokenHint}'/>"
                       class="min-w-0 flex-1 rounded-lg border border-slate-200 bg-white px-3 py-1.5 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-400">
                <c:if test="${not empty streamTokenHint}">
                    <button type="button" id="fillToken" data-token="<c:out value='${streamTokenHint}'/>"
                            class="text-xs font-semibold text-indigo-600 hover:underline">Reset token</button>
                </c:if>
            </div>

            <%-- Live preview of the decorator chain --%>
            <div class="mt-4 flex flex-wrap items-center gap-1.5 text-xs" aria-label="Pipeline preview">
                <span class="mr-1 font-semibold text-slate-500">Pipeline</span>
                <span class="rounded-lg bg-slate-100 px-2.5 py-1 font-bold text-slate-600">Meter data</span>
                <span data-node="Compression" class="inline-flex items-center gap-1.5">
                    <i class="bi bi-chevron-right text-[10px] text-slate-300"></i>
                    <span class="rounded-lg bg-indigo-50 px-2.5 py-1 font-bold text-indigo-700">Compression</span>
                </span>
                <span data-node="Encryption" class="inline-flex items-center gap-1.5">
                    <i class="bi bi-chevron-right text-[10px] text-slate-300"></i>
                    <span class="rounded-lg bg-indigo-50 px-2.5 py-1 font-bold text-indigo-700">Encryption</span>
                </span>
                <span data-node="Authentication" class="inline-flex items-center gap-1.5">
                    <i class="bi bi-chevron-right text-[10px] text-slate-300"></i>
                    <span class="rounded-lg bg-indigo-50 px-2.5 py-1 font-bold text-indigo-700">Authentication</span>
                </span>
            </div>
        </form>
    </section>

    <%-- ───────── Result of the last run ───────── --%>
    <c:if test="${not empty streamResult}">
        <section id="labResult" class="mt-4 rounded-3xl border border-slate-200/80 bg-white p-4 shadow-[0_1px_2px_rgba(15,23,42,.04),0_16px_40px_-20px_rgba(15,23,42,.18)] sm:p-5">
            <div class="mb-3 flex flex-wrap items-center justify-between gap-3">
                <div class="flex items-center gap-3">
                    <span class="grid h-9 w-9 place-items-center rounded-xl text-white shadow-md ${streamResult.ok ? 'bg-gradient-to-br from-emerald-500 to-teal-500 shadow-emerald-500/30' : 'bg-gradient-to-br from-red-500 to-rose-500 shadow-red-500/30'}">
                        <i class="bi ${streamResult.ok ? 'bi-check2-circle' : 'bi-shield-exclamation'}"></i>
                    </span>
                    <div>
                        <h2 class="text-sm font-extrabold">Result</h2>
                        <p class="text-xs text-slate-500">Meter <c:out value="${streamResult.meterCode}"/></p>
                    </div>
                </div>
                <c:choose>
                    <c:when test="${streamResult.ok}">
                        <span class="inline-flex items-center gap-1.5 rounded-full bg-emerald-50 px-2.5 py-0.5 text-xs font-bold text-emerald-700 ring-1 ring-inset ring-emerald-600/20">
                            <i class="bi bi-check-circle-fill"></i> Stream secured
                        </span>
                    </c:when>
                    <c:otherwise>
                        <span class="inline-flex items-center gap-1.5 rounded-full bg-red-50 px-2.5 py-0.5 text-xs font-bold text-red-700 ring-1 ring-inset ring-red-600/20">
                            <i class="bi bi-x-circle-fill"></i> Blocked
                        </span>
                    </c:otherwise>
                </c:choose>
            </div>

            <div class="mb-4 flex flex-wrap items-center gap-1.5 text-xs">
                <span class="rounded-lg bg-slate-100 px-2.5 py-1 font-bold text-slate-600">Meter data</span>
                <c:forEach var="layer" items="${streamResult.layers}">
                    <i class="bi bi-chevron-right text-[10px] text-slate-300"></i>
                    <span class="rounded-lg bg-indigo-50 px-2.5 py-1 font-bold text-indigo-700"><c:out value="${layer}"/></span>
                </c:forEach>
                <c:if test="${empty streamResult.layers}">
                    <span class="ml-1 text-slate-400">No layers selected. The data was sent as it is.</span>
                </c:if>
            </div>

            <c:choose>
                <c:when test="${streamResult.ok}">
                    <div class="grid gap-3 md:grid-cols-2">
                        <div>
                            <div class="mb-1 flex items-center justify-between text-xs font-semibold text-slate-500">
                                <span>Original data</span>
                                <span><c:out value="${streamResult.originalSize}"/> characters</span>
                            </div>
                            <pre id="resOriginal" class="max-h-40 overflow-auto whitespace-pre-wrap break-all rounded-2xl bg-slate-50 p-3 font-mono text-xs text-slate-700"><c:out value="${streamResult.original}"/></pre>
                        </div>
                        <div>
                            <div class="mb-1 flex items-center justify-between text-xs font-semibold text-slate-500">
                                <span>Secured output</span>
                                <span class="flex items-center gap-3">
                                    <span><c:out value="${streamResult.outputSize}"/> characters</span>
                                    <button type="button" class="js-copy text-indigo-600 hover:underline" data-target="resOutput">Copy</button>
                                </span>
                            </div>
                            <pre id="resOutput" class="max-h-40 overflow-auto whitespace-pre-wrap break-all rounded-2xl bg-slate-900 p-3 font-mono text-xs text-emerald-300"><c:out value="${streamResult.output}"/></pre>
                        </div>
                    </div>

                    <div class="mt-3 flex flex-wrap items-center gap-2 text-sm">
                        <c:choose>
                            <c:when test="${streamResult.roundTrip}">
                                <span class="inline-flex items-center gap-1.5 text-emerald-700"><i class="bi bi-check2-all"></i> Decoded back to the same data. Nothing was lost.</span>
                            </c:when>
                            <c:otherwise>
                                <span class="inline-flex items-center gap-1.5 text-red-700"><i class="bi bi-exclamation-triangle"></i> The decoded data is different from the original.</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="flex items-start gap-2 rounded-2xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
                        <i class="bi bi-shield-exclamation mt-0.5"></i>
                        <span><c:out value="${streamResult.error}"/> The Authentication layer only lets data through when the token is correct.</span>
                    </div>
                </c:otherwise>
            </c:choose>
        </section>
    </c:if>

    <%-- ───────── Meters ───────── --%>
    <section class="mt-12">
        <div class="mb-4 flex flex-wrap items-center justify-between gap-3">
            <h2 class="text-lg font-extrabold">Meters</h2>
            <div class="flex flex-wrap items-center gap-2">
                <div class="inline-flex gap-0.5 rounded-full bg-slate-200/70 p-1" role="group" aria-label="Filter meters">
                    <button type="button" data-filter="all"      class="rounded-full px-3.5 py-1 text-sm font-semibold">All</button>
                    <button type="button" data-filter="active"   class="rounded-full px-3.5 py-1 text-sm font-semibold">Active</button>
                    <button type="button" data-filter="inactive" class="rounded-full px-3.5 py-1 text-sm font-semibold">Inactive</button>
                </div>
                <label class="relative block">
                    <i class="bi bi-search absolute left-3.5 top-1/2 -translate-y-1/2 text-slate-400"></i>
                    <input type="search" id="meterSearch" placeholder="Search" autocomplete="off"
                           class="w-44 rounded-full border border-slate-200 bg-white py-1.5 pl-9 pr-3 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-400 sm:w-56">
                </label>
            </div>
        </div>

        <c:choose>
            <c:when test="${empty meters}">
                <div class="rounded-3xl border border-dashed border-slate-300 bg-white px-5 py-14 text-center text-slate-500">
                    <i class="bi bi-speedometer text-4xl text-slate-300"></i>
                    <h3 class="mt-2 font-bold text-slate-900">No meters yet</h3>
                    <p class="mt-1 text-sm">Add a meter to start recording readings.</p>
                </div>
            </c:when>
            <c:otherwise>
                <div class="grid grid-cols-[repeat(auto-fit,minmax(280px,1fr))] gap-4" id="metersGrid">
                    <c:forEach var="meter" items="${meters}">
                        <article id="meter-${meter.id}"
                                 class="flex flex-col rounded-3xl border border-slate-200/80 bg-white p-5 transition hover:shadow-[0_16px_40px_-24px_rgba(15,23,42,.35)]"
                                 data-meter-card
                                 data-active="${meter.active}"
                                 data-kwh="${empty meter.latestKwh ? 0 : meter.latestKwh}"
                                 data-search="<c:out value='${meter.meterCode} ${meter.buildingName}'/>">

                            <div class="flex items-start justify-between gap-2">
                                <div class="min-w-0">
                                    <div class="font-extrabold"><c:out value="${meter.meterCode}"/></div>
                                    <div class="truncate text-sm text-slate-500"><c:out value="${meter.buildingName}"/></div>
                                </div>

                                    <%-- Click to switch on or off --%>
                                <form method="post" action="${ctx}/app/energy">
                                    <input type="hidden" name="_csrf" value="<c:out value='${csrfToken}'/>">
                                    <input type="hidden" name="action" value="toggleActive">
                                    <input type="hidden" name="meterId" value="${meter.id}">
                                    <c:choose>
                                        <c:when test="${meter.active}">
                                            <button type="submit" title="Click to deactivate"
                                                    class="inline-flex items-center gap-1.5 rounded-full bg-emerald-50 px-2.5 py-1 text-xs font-bold text-emerald-700 ring-1 ring-inset ring-emerald-600/20 hover:bg-emerald-100">
                                                <span class="h-1.5 w-1.5 rounded-full bg-emerald-500"></span>Active
                                            </button>
                                        </c:when>
                                        <c:otherwise>
                                            <button type="submit" title="Click to activate"
                                                    class="inline-flex items-center gap-1.5 rounded-full bg-slate-100 px-2.5 py-1 text-xs font-bold text-slate-500 ring-1 ring-inset ring-slate-400/20 hover:bg-slate-200">
                                                <span class="h-1.5 w-1.5 rounded-full bg-slate-400"></span>Inactive
                                            </button>
                                        </c:otherwise>
                                    </c:choose>
                                </form>
                            </div>

                            <div class="mb-3 mt-5 flex items-baseline gap-1.5">
                                <c:choose>
                                    <c:when test="${not empty meter.latestKwh}">
                                        <span class="text-4xl font-extrabold leading-none tracking-tight tabular-nums"><c:out value="${meter.latestKwh}"/></span>
                                        <span class="font-semibold text-slate-400">kWh</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="italic text-slate-400">No readings yet</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <div class="h-1.5 overflow-hidden rounded-full bg-slate-100" aria-hidden="true">
                                <div class="js-gauge h-full w-0 rounded-full bg-gradient-to-r from-indigo-400 to-violet-500 transition-all duration-700"></div>
                            </div>

                            <div class="mb-5 mt-3 flex flex-wrap gap-x-4 gap-y-1 text-xs text-slate-500">
                                <span><i class="bi bi-lightning me-1"></i>
                                    <c:choose>
                                        <c:when test="${not empty meter.latestVoltage}"><c:out value="${meter.latestVoltage}"/> V</c:when>
                                        <c:otherwise>— V</c:otherwise>
                                    </c:choose>
                                </span>
                                <span><i class="bi bi-clock me-1"></i>
                                    <c:choose>
                                        <c:when test="${not empty meter.lastReadAt}"><c:out value="${meter.lastReadAt}"/></c:when>
                                        <c:otherwise>Never read</c:otherwise>
                                    </c:choose>
                                </span>
                            </div>

                            <div class="mt-auto flex flex-wrap gap-2">
                                <button type="button"
                                        class="js-add-reading inline-flex items-center gap-1.5 rounded-full bg-slate-900 px-3.5 py-1.5 text-sm font-bold text-white hover:bg-slate-700 focus:outline-none focus-visible:ring-2 focus-visible:ring-indigo-400 focus-visible:ring-offset-2"
                                        data-meter-id="${meter.id}"
                                        data-meter-code="<c:out value='${meter.meterCode}'/>">
                                    <i class="bi bi-plus-lg"></i> Add reading
                                </button>
                                <button type="button"
                                        class="js-use-in-lab inline-flex items-center gap-1.5 rounded-full border border-slate-200 bg-white px-3.5 py-1.5 text-sm font-bold text-slate-700 hover:bg-slate-50 focus:outline-none focus-visible:ring-2 focus-visible:ring-indigo-400 focus-visible:ring-offset-2"
                                        data-meter-id="${meter.id}">
                                    <i class="bi bi-stars text-violet-500"></i> Use in lab
                                </button>
                            </div>
                        </article>
                    </c:forEach>
                </div>

                <div id="noMatch" class="mt-4 hidden rounded-3xl border border-dashed border-slate-300 bg-white px-5 py-10 text-center text-slate-500">
                    <i class="bi bi-funnel text-3xl text-slate-300"></i>
                    <h3 class="mt-2 font-bold text-slate-900">No meters match</h3>
                    <p class="mt-1 text-sm">Try a different filter or search word.</p>
                </div>
            </c:otherwise>
        </c:choose>
    </section>

    <%-- ───────── Chart + logs ───────── --%>
    <div class="mt-10 grid items-start gap-6 lg:grid-cols-[minmax(0,1fr)_360px]">

        <section class="rounded-3xl border border-slate-200/80 bg-white p-5">
            <div class="mb-3 flex items-baseline justify-between gap-2">
                <h2 class="font-extrabold">Energy by meter</h2>
                <span class="text-xs text-slate-500">Latest kWh reading</span>
            </div>
            <div class="relative h-72"><canvas id="energyChart"></canvas></div>

            <script type="application/json" id="energyChartData">
                {
                    "labels": [<c:forEach var="meter" items="${meters}" varStatus="s">"<c:out value='${meter.meterCode}'/>"<c:if test="${!s.last}">,</c:if></c:forEach>],
                "kwh":    [<c:forEach var="meter" items="${meters}" varStatus="s"><c:choose><c:when test="${not empty meter.latestKwh}">${meter.latestKwh}</c:when><c:otherwise>0</c:otherwise></c:choose><c:if test="${!s.last}">,</c:if></c:forEach>]
            }
            </script>
        </section>

        <section class="rounded-3xl border border-slate-200/80 bg-white p-5">
            <div class="mb-4 flex items-baseline justify-between gap-2">
                <h2 class="font-extrabold">Stream logs</h2>
                <span class="text-xs text-slate-500">Layers used each time</span>
            </div>

            <c:choose>
                <c:when test="${empty streamLogs}">
                    <div class="px-3 py-8 text-center text-slate-500">
                        <i class="bi bi-broadcast-pin text-3xl text-slate-300"></i>
                        <h3 class="mt-2 font-bold text-slate-900">No streams yet</h3>
                        <p class="mt-1 text-sm">Run the stream lab to see each log here.</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <ol class="max-h-[420px] overflow-y-auto pr-1">
                        <c:forEach var="log" items="${streamLogs}">
                            <li class="relative ml-1.5 border-l-2 border-slate-200 pb-5 pl-5 last:border-transparent last:pb-1">
                                <span class="absolute -left-[7px] top-0.5 h-3 w-3 rounded-full border-[3px] border-indigo-500 bg-white"></span>
                                <div class="flex items-baseline justify-between gap-2">
                                    <strong class="text-sm"><c:out value="${log.meterCode}"/></strong>
                                    <time class="text-[11px] tabular-nums text-slate-500"><c:out value="${log.createdAt}"/></time>
                                </div>
                                <div class="mt-2 flex flex-wrap items-center gap-1">
                                    <span class="rounded-md bg-slate-100 px-2 py-0.5 text-[11px] font-bold text-slate-500">Data</span>
                                    <c:forEach var="layer" items="${log.layers}">
                                        <i class="bi bi-chevron-right text-[10px] text-slate-300"></i>
                                        <span class="rounded-md bg-indigo-50 px-2 py-0.5 text-[11px] font-bold text-indigo-700"><c:out value="${layer}"/></span>
                                    </c:forEach>
                                </div>
                            </li>
                        </c:forEach>
                    </ol>
                </c:otherwise>
            </c:choose>
        </section>
    </div>
</div>

<%-- ───────── Modal: add reading ───────── --%>
<div id="addReadingModal" class="fixed inset-0 z-[1080] hidden items-center justify-center p-4" role="dialog" aria-modal="true" aria-labelledby="addReadingTitle">
    <div class="absolute inset-0 bg-slate-900/40 backdrop-blur-sm" data-modal-close></div>
    <form method="post" action="${ctx}/app/energy" class="relative w-full max-w-md rounded-3xl bg-white p-6 shadow-2xl">
        <input type="hidden" name="_csrf" value="<c:out value='${csrfToken}'/>">
        <input type="hidden" name="action" value="addReading">
        <input type="hidden" name="meterId" id="readingMeterId">

        <div class="mb-5 flex items-start justify-between gap-3">
            <div>
                <h3 id="addReadingTitle" class="text-lg font-extrabold">Add reading</h3>
                <p class="mt-0.5 text-sm text-slate-500" id="readingMeterCode">Meter</p>
            </div>
            <button type="button" data-modal-close aria-label="Close" class="grid h-8 w-8 place-items-center rounded-lg text-slate-500 hover:bg-slate-100"><i class="bi bi-x-lg"></i></button>
        </div>

        <div class="space-y-4">
            <div>
                <label for="kwh" class="mb-1 block text-sm font-bold">Energy</label>
                <div class="flex">
                    <input type="number" id="kwh" name="kwh" step="0.001" min="0" max="9999999.999" placeholder="245.750" required
                           class="w-full rounded-l-xl border border-slate-200 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-400">
                    <span class="grid place-items-center rounded-r-xl border border-l-0 border-slate-200 bg-slate-50 px-3 text-sm font-semibold text-slate-500">kWh</span>
                </div>
            </div>
            <div>
                <label for="voltage" class="mb-1 block text-sm font-bold">Voltage</label>
                <div class="flex">
                    <input type="number" id="voltage" name="voltage" step="0.01" min="0" max="9999.99" placeholder="230.00" required
                           class="w-full rounded-l-xl border border-slate-200 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-400">
                    <span class="grid place-items-center rounded-r-xl border border-l-0 border-slate-200 bg-slate-50 px-3 text-sm font-semibold text-slate-500">V</span>
                </div>
            </div>
        </div>

        <div class="mt-6 flex justify-end gap-2">
            <button type="button" data-modal-close class="rounded-full border border-slate-200 px-4 py-2 text-sm font-bold hover:bg-slate-50">Cancel</button>
            <button type="submit" class="rounded-full bg-slate-900 px-5 py-2 text-sm font-bold text-white hover:bg-slate-700">Save reading</button>
        </div>
    </form>
</div>

<%-- ───────── Modal: add meter ───────── --%>
<div id="addMeterModal" class="fixed inset-0 z-[1080] hidden items-center justify-center p-4" role="dialog" aria-modal="true" aria-labelledby="addMeterTitle">
    <div class="absolute inset-0 bg-slate-900/40 backdrop-blur-sm" data-modal-close></div>
    <form method="post" action="${ctx}/app/energy" class="relative w-full max-w-md rounded-3xl bg-white p-6 shadow-2xl">
        <input type="hidden" name="_csrf" value="<c:out value='${csrfToken}'/>">
        <input type="hidden" name="action" value="addMeter">

        <div class="mb-5 flex items-start justify-between gap-3">
            <div>
                <h3 id="addMeterTitle" class="text-lg font-extrabold">Add meter</h3>
                <p class="mt-0.5 text-sm text-slate-500">Choose where the new meter is installed.</p>
            </div>
            <button type="button" data-modal-close aria-label="Close" class="grid h-8 w-8 place-items-center rounded-lg text-slate-500 hover:bg-slate-100"><i class="bi bi-x-lg"></i></button>
        </div>

        <div class="space-y-4">
            <div class="flex items-start gap-2 rounded-2xl bg-slate-50 px-3 py-2.5 text-xs text-slate-500">
                <i class="bi bi-magic mt-0.5 text-violet-500"></i>
                <span>The meter code is created for you. It looks like <b class="text-slate-700">EM-COL-003</b>: the city, then the next free number.</span>
            </div>
            <div>
                <label for="buildingId" class="mb-1 block text-sm font-bold">Building</label>
                <select id="buildingId" name="buildingId" required
                        class="w-full rounded-xl border border-slate-200 bg-white px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-400">
                    <c:choose>
                        <c:when test="${empty buildings}">
                            <option value="">No buildings found</option>
                        </c:when>
                        <c:otherwise>
                            <option value="">Choose a building</option>
                            <c:forEach var="b" items="${buildings}">
                                <option value="${b.key}"><c:out value="${b.value}"/></option>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </select>
                <c:if test="${empty buildings}">
                    <p class="mt-1 text-xs text-amber-600">Add a building (and its zone) in the database first.</p>
                </c:if>
            </div>
        </div>

        <div class="mt-6 flex justify-end gap-2">
            <button type="button" data-modal-close class="rounded-full border border-slate-200 px-4 py-2 text-sm font-bold hover:bg-slate-50">Cancel</button>
            <button type="submit" ${empty buildings ? 'disabled' : ''} class="rounded-full bg-slate-900 px-5 py-2 text-sm font-bold text-white hover:bg-slate-700 disabled:opacity-50">Add meter</button>
        </div>
    </form>
</div>

<script>
    /**
     * SmartCity360 – Energy page JS
     * - Stream lab: layer chips, live pipeline preview, token field
     * - Meter filter + search, summary numbers, gauges
     * - Modals: add reading, add meter
     * - kWh bar chart (Chart.js)
     */
    'use strict';

    (function () {
        const $  = (sel, root = document) => root.querySelector(sel);
        const $$ = (sel, root = document) => Array.from(root.querySelectorAll(sel));
        const state = { status: 'all', query: '' };

        /* ── Summary numbers + gauges ─────────────────────────────── */
        const cards = () => $$('[data-meter-card]');

        function fillSummary() {
            const all = cards();
            const values = all.map(c => parseFloat(c.dataset.kwh) || 0);
            const sum = values.reduce((a, b) => a + b, 0);
            const max = Math.max(1, ...values);

            setText('statTotal', all.length);
            setText('statActive', all.filter(c => c.dataset.active === 'true').length);
            setText('statKwh', sum.toLocaleString(undefined, { maximumFractionDigits: 1 }));

            all.forEach((card, i) => {
                const bar = $('.js-gauge', card);
                if (bar) requestAnimationFrame(() => { bar.style.width = Math.round((values[i] / max) * 100) + '%'; });
            });
        }

        function setText(id, value) {
            const el = document.getElementById(id);
            if (el) el.textContent = value;
        }

        /* ── Filter + search ──────────────────────────────────────── */
        const ON  = ['bg-white', 'text-slate-900', 'shadow-sm'];
        const OFF = ['text-slate-500', 'hover:text-slate-900'];

        function applyFilters() {
            let shown = 0;
            cards().forEach(card => {
                const isActive = card.dataset.active === 'true';
                const okStatus = state.status === 'all' ||
                    (state.status === 'active' && isActive) ||
                    (state.status === 'inactive' && !isActive);
                const okQuery = !state.query || (card.dataset.search || '').toLowerCase().includes(state.query);
                const visible = okStatus && okQuery;
                card.classList.toggle('hidden', !visible);
                if (visible) shown++;
            });
            const none = document.getElementById('noMatch');
            if (none) none.classList.toggle('hidden', shown !== 0 || cards().length === 0);
        }

        function setFilter(status) {
            state.status = status;
            $$('[data-filter]').forEach(btn => {
                const on = btn.dataset.filter === status;
                ON.forEach(c => btn.classList.toggle(c, on));
                OFF.forEach(c => btn.classList.toggle(c, !on));
            });
            applyFilters();
        }

        /* ── Modals ───────────────────────────────────────────────── */
        function openModal(id) {
            const m = document.getElementById(id);
            if (!m) return;
            m.classList.remove('hidden');
            m.classList.add('flex');
            document.body.style.overflow = 'hidden';
            const first = $('input:not([type="hidden"])', m);
            if (first) first.focus();
        }

        function closeModals() {
            $$('[role="dialog"]').forEach(m => { m.classList.add('hidden'); m.classList.remove('flex'); });
            document.body.style.overflow = '';
        }

        function openReadingModal(meterId, meterCode) {
            const m = document.getElementById('addReadingModal');
            if (!m) return;
            $('form', m).reset();
            document.getElementById('readingMeterId').value = meterId;
            document.getElementById('readingMeterCode').textContent = meterCode || ('Meter #' + meterId);
            openModal('addReadingModal');
        }

        /* ── Stream lab ───────────────────────────────────────────── */
        function syncPipeline() {
            $$('#labForm input[data-layer]').forEach(box => {
                const node = $('[data-node="' + box.dataset.layer + '"]');
                if (node) node.classList.toggle('hidden', !box.checked);
            });

            const auth = $('#labForm input[data-layer="Authentication"]');
            const row = document.getElementById('tokenRow');
            const token = document.getElementById('labToken');
            if (auth && row && token) {
                row.classList.toggle('hidden', !auth.checked);
                row.classList.toggle('flex', auth.checked);
                token.required = auth.checked;
            }
        }

        function initLab() {
            const form = document.getElementById('labForm');
            if (!form) return;

            $$('input[data-layer]', form).forEach(box => box.addEventListener('change', syncPipeline));
            syncPipeline();

            const fill = document.getElementById('fillToken');
            if (fill) fill.addEventListener('click', () => {
                const t = document.getElementById('labToken');
                t.value = fill.dataset.token;
                t.focus();
            });

            // Ctrl + Enter sends the form
            const payload = document.getElementById('labPayload');
            if (payload) payload.addEventListener('keydown', e => {
                if ((e.ctrlKey || e.metaKey) && e.key === 'Enter') form.requestSubmit();
            });

            form.addEventListener('submit', () => {
                const btn = document.getElementById('labRun');
                // disable after the browser has collected the form data
                if (btn) setTimeout(() => { btn.disabled = true; btn.innerHTML = '<i class="bi bi-hourglass-split"></i> Running…'; }, 0);
            });

            // "Use in lab" button on a meter card
            $$('.js-use-in-lab').forEach(btn => btn.addEventListener('click', () => {
                const select = document.getElementById('labMeter');
                if (select) select.value = btn.dataset.meterId;
                document.getElementById('lab').scrollIntoView({ behavior: 'smooth', block: 'center' });
                if (payload) payload.focus({ preventScroll: true });
            }));

            // Copy button in the result
            $$('.js-copy').forEach(btn => btn.addEventListener('click', () => {
                const target = document.getElementById(btn.dataset.target);
                if (!target || !navigator.clipboard) return;
                navigator.clipboard.writeText(target.textContent).then(() => {
                    const old = btn.textContent;
                    btn.textContent = 'Copied';
                    setTimeout(() => { btn.textContent = old; }, 1500);
                });
            }));

            // show the result when the page loads after a run
            const result = document.getElementById('labResult');
            if (result) result.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
        }

        /* ── Chart ────────────────────────────────────────────────── */
        function drawChart() {
            const dataEl = document.getElementById('energyChartData');
            const canvas = document.getElementById('energyChart');
            if (!dataEl || !canvas || !window.Chart) return;

            try {
                const data = JSON.parse(dataEl.textContent);
                const grad = canvas.getContext('2d').createLinearGradient(0, 0, 0, 280);
                grad.addColorStop(0, '#818cf8');
                grad.addColorStop(1, '#8b5cf6');
                Chart.defaults.color = '#64748b';

                new Chart(canvas, {
                    type: 'bar',
                    data: {
                        labels: data.labels || [],
                        datasets: [{ label: 'kWh', data: data.kwh || [], backgroundColor: grad, borderRadius: 10, borderSkipped: false, maxBarThickness: 44 }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        plugins: {
                            legend: { display: false },
                            tooltip: { backgroundColor: '#0f172a', padding: 10, cornerRadius: 10, displayColors: false, callbacks: { label: c => c.parsed.y + ' kWh' } }
                        },
                        scales: {
                            y: { beginAtZero: true, border: { display: false }, grid: { color: '#eef1f6' }, ticks: { callback: v => v + ' kWh' } },
                            x: { grid: { display: false }, border: { display: false } }
                        }
                    }
                });
            } catch (e) {
                console.error('Energy chart error:', e);
            }
        }

        /* ── Start ────────────────────────────────────────────────── */
        document.addEventListener('DOMContentLoaded', () => {
            fillSummary();
            drawChart();
            setFilter('all');
            initLab();

            $$('[data-filter]').forEach(btn => btn.addEventListener('click', () => setFilter(btn.dataset.filter)));

            const search = document.getElementById('meterSearch');
            if (search) search.addEventListener('input', () => {
                state.query = search.value.trim().toLowerCase();
                applyFilters();
            });

            $$('.js-add-reading').forEach(btn => btn.addEventListener('click', () => openReadingModal(btn.dataset.meterId, btn.dataset.meterCode)));

            const addMeter = document.getElementById('openAddMeter');
            if (addMeter) addMeter.addEventListener('click', () => {
                $('form', document.getElementById('addMeterModal')).reset();
                openModal('addMeterModal');
            });

            $$('[data-modal-close]').forEach(el => el.addEventListener('click', closeModals));
            document.addEventListener('keydown', e => { if (e.key === 'Escape') closeModals(); });
        });
    })();

</script>
<%@ include file="layout/footer.jspf" %>
