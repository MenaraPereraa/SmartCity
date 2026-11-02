<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="layout/header.jspf" %>

<div class="d-flex align-items-start justify-content-between mb-2">
    <div>
        <h1 class="h2 mb-0 fw-bold">💡 Street Lights</h1>
        <small class="sc-pattern-badge">Flyweight Pattern</small>
        <p class="text-muted mt-1 mb-0">
            Shared model specs (intrinsic) + unique location (extrinsic)
        </p>
    </div>
    <div class="text-end">
        <span class="badge text-bg-secondary fs-6" id="lightStats"></span>
    </div>
</div>

<%-- Filter Bar --%>
<div class="card mb-4 shadow-sm">
    <div class="card-body py-2">
        <div class="row g-2 align-items-center">
            <div class="col-auto">
                <label class="form-label fw-medium mb-0" for="zoneFilter">
                    <i class="bi bi-diagram-3 me-1"></i>Zone:
                </label>
            </div>
            <div class="col-sm-4 col-md-3">
                <select class="form-select form-select-sm" id="zoneFilter">
                    <option value="all">All Zones</option>
                    <%-- Unique zone options built from lights list --%>
                    <c:forEach var="light" items="${lights}">
                        <option value="${light.id}" data-zone="${light.zoneName}">
                            <c:out value="${light.zoneName}"/>
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-auto ms-2">
                <label class="form-label fw-medium mb-0" for="statusFilter">
                    <i class="bi bi-toggle-on me-1"></i>Status:
                </label>
            </div>
            <div class="col-sm-3 col-md-2">
                <select class="form-select form-select-sm" id="statusFilter">
                    <option value="all">All</option>
                    <option value="on">On</option>
                    <option value="off">Off</option>
                </select>
            </div>
        </div>
    </div>
</div>

<%-- Light Grid --%>
<c:choose>
    <c:when test="${empty lights}">
        <div class="alert alert-info d-flex align-items-center gap-2" role="alert">
            <i class="bi bi-lightbulb-off fs-5"></i>
            <span>No street lights registered in the system yet.</span>
        </div>
    </c:when>
    <c:otherwise>
        <div class="sc-light-grid">
            <c:forEach var="light" items="${lights}">
                <div class="sc-light-card"
                     id="light-${light.id}"
                     data-zone-id="${light.id}"
                     data-zone-name="<c:out value='${light.zoneName}'/>"
                     data-is-on="${light.on}">

                    <div class="d-flex justify-content-between align-items-center mb-1">
                        <div class="d-flex align-items-center gap-2">
                            <div class="sc-light-indicator ${light.on ? 'on' : ''}"></div>
                            <span class="fw-semibold" style="font-size:.9rem;">Light #${light.id}</span>
                        </div>
                        <c:choose>
                            <c:when test="${light.on}">
                                <span class="badge text-bg-warning text-dark">ON</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge text-bg-secondary">OFF</span>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div class="small fw-semibold mb-1">
                        <i class="bi bi-cpu me-1 text-muted"></i>
                        <c:out value="${light.model}"/>
                        (<c:out value="${light.watts}"/> W)
                    </div>

                    <div class="sc-light-coords mb-1">
                        <i class="bi bi-geo-alt me-1"></i>
                        <c:out value="${light.latitude}"/>, <c:out value="${light.longitude}"/>
                    </div>

                    <div class="small text-muted mb-2">
                        <i class="bi bi-diagram-3 me-1"></i>
                        <c:out value="${light.zoneName}"/>
                    </div>

                    <div class="d-flex gap-1 flex-wrap mb-3">
                        <span class="badge text-bg-info text-dark" style="font-size:.7rem;">
                            <i class="bi bi-brightness-high me-1"></i>
                            <c:out value="${light.brightnessLumens}"/> lm
                        </span>
                        <span class="badge text-bg-light border text-dark" style="font-size:.7rem;">
                            <i class="bi bi-thermometer-half me-1"></i>
                            <c:out value="${light.colourTempKelvin}"/> K
                        </span>
                    </div>

                    <c:choose>
                        <c:when test="${light.on}">
                            <button class="btn btn-sm btn-outline-danger w-100"
                                    onclick="toggleLight(${light.id}, true, '${ctx}')">
                                <i class="bi bi-toggle-off me-1"></i>Turn Off
                            </button>
                        </c:when>
                        <c:otherwise>
                            <button class="btn btn-sm btn-success w-100"
                                    onclick="toggleLight(${light.id}, false, '${ctx}')">
                                <i class="bi bi-toggle-on me-1"></i>Turn On
                            </button>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:forEach>
        </div>
    </c:otherwise>
</c:choose>

<script src="${ctx}/assets/street-lights.js"></script>
<%@ include file="layout/footer.jspf" %>
